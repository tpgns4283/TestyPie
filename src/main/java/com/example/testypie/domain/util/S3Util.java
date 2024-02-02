package com.example.testypie.domain.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Util {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Getter
    @RequiredArgsConstructor // final필드를 초기화하기 위해 사용
    public enum FilePath { // 파일 경로를 나타내는 상수를 정의
        PROFILE("profile/"),
        BOARD("board/"),
        COMMENT("comment/");
        private final String path; // 경로를 저장하는 final 필드
    }

    private static ObjectMetadata setObjectMetadata(MultipartFile multipartFile) {
        // 업로드할 파일의 메타데이터를 설정하는 메소드
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());
        return metadata;
    }

    public String uploadFile(MultipartFile multipartFile, FilePath filePath) {  // 업로드는 잘 됨
        // 업로드할 파일이 존재하지 않거나 비어있으면 null 반환
        if (multipartFile == null || multipartFile.isEmpty()) {
            return null;
        }
        // 업로드할 파일의 고유한 파일명 생성
        String fileName = createFileName(multipartFile.getOriginalFilename());
        // 파일명을 UTF-8로 디코딩
        fileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
        log.warn("업로드 파일 디코딩 완료 : " + filePath + fileName);
        // 업로드할 파일의 메타데이터 생성
        ObjectMetadata metadata = setObjectMetadata(multipartFile);
        try {
            // S3에 파일 업로드
            amazonS3Client.putObject(bucketName, filePath.getPath() + fileName,
                    multipartFile.getInputStream(), metadata);
        } catch (Exception e) {
            // 업로드 중에 예외 발생 시 전역 예외(GlobalException) 발생
            throw new IllegalArgumentException("파일 업로드 실패");
        }
        // 업로드한 파일의 URL 반환
        return getFileUrl(fileName, filePath);
    }

    public void deleteFile(String fileUrl, FilePath filePath) {
        // 주어진 파일 URL로부터 파일명을 추출
        String fileName = getFileNameFromFileUrl(fileUrl, filePath);
        log.info("fileName: {}", fileName);
        // 파일명을 UTF-8로 디코딩
        try {
            fileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8.toString());
            System.out.println(fileName);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("ㅎㅇ1");
        }
        // 파일명이 비어있거나 해당 파일이 존재하지 않으면 예외 발생
        log.warn("bucketName: {" + bucketName + "}");
        log.warn("fileKey: {" + filePath.getPath() + fileName + "}");

        if (fileName.isBlank() || !amazonS3Client.doesObjectExist(bucketName,
                filePath.getPath() + fileName)) {
            throw new IllegalArgumentException("삭제할 파일이 없거나 파일명이 없습니다.");
        }
        // S3에서 파일 삭제
        amazonS3Client.deleteObject(bucketName, filePath.getPath() + fileName);
    }

    public String getFileUrl(String fileName, FilePath filePath) {
        // AWS S3 클라이언트를 사용하여 주어진 버킷, 파일 경로 및 파일명에 해당하는 파일의 URL을 얻어옴
        return amazonS3Client.getUrl(bucketName, filePath.getPath() + fileName).toString();
    }

    private String getFileNameFromFileUrl(String fileUrl, FilePath filePath) {
        // 파일 URL에서 파일 경로 다음의 문자열부터 파일명의 끝까지 추출하여 반환
        return fileUrl.substring(fileUrl.lastIndexOf(filePath.getPath()) +
                filePath.getPath().length());
    }

    private String createFileName(String fileName) {
        // UUID를 사용하여 고유한 문자열을 생성하고, 주어진 파일명과 연결하여 반환
        return UUID.randomUUID().toString().concat(fileName);
    }
}
