package com.example.testypie.domain.survey.service;

import com.example.testypie.domain.category.entity.Category;
import com.example.testypie.domain.category.service.CategoryService;
import com.example.testypie.domain.survey.dto.OptionCreateRequestDTO;
import com.example.testypie.domain.survey.entity.Option;
import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.product.service.ProductService;
import com.example.testypie.domain.survey.dto.QuestionCreateRequestDTO;
import com.example.testypie.domain.survey.entity.Question;
import com.example.testypie.domain.survey.entity.QuestionType;
import com.example.testypie.domain.survey.dto.SurveyCreateRequestDTO;
import com.example.testypie.domain.survey.dto.SurveyCreateResponseDTO;
import com.example.testypie.domain.survey.dto.SurveyReadResponseDTO;
import com.example.testypie.domain.survey.entity.Survey;
import com.example.testypie.domain.survey.repository.SurveyRepository;
import com.example.testypie.domain.user.entity.User;
import com.example.testypie.global.exception.ErrorCode;
import com.example.testypie.global.exception.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final ProductService productService;
    private final CategoryService categoryService;

    @Transactional
    public SurveyCreateResponseDTO addSurvey(SurveyCreateRequestDTO req, Long product_id, User user, Long childCategory_id, String parentCategory_name) {

        Product product = productService.getUserProduct(product_id, user);
        Category category = categoryService.getCategory(childCategory_id, parentCategory_name);
        if (!category.getId().equals(product.getCategory().getId())) {
            throw new GlobalExceptionHandler.CustomException(ErrorCode.SELECT_PRODUCT_NOT_FOUND);
        }

        Survey survey = Survey.builder()
                .user(user)
                .title(req.title())
                .createdAt(LocalDateTime.now())
                .product(product)
                .build();

        List<Question> questions = new ArrayList<>();
        for (QuestionCreateRequestDTO questionDTO : req.questionList()) {
            Question question = Question.builder()
                    .text(questionDTO.text())
                    .questionType(questionDTO.questionType())
                    .survey(survey)
                    .build();

            if (question.getQuestionType() == QuestionType.MULTI_CHOICE) {
                for (OptionCreateRequestDTO optionDTO : questionDTO.optionList()) {
                    Option option = Option.builder()
                            .text(optionDTO.text())
                            .question(question)
                            .build();
                    question.getOptionList().add(option);
                }
            }
            questions.add(question);
        }

        survey.setQuestionList(questions);
        Survey savedSurvey = surveyRepository.save(survey);

        product.setSurvey(savedSurvey);

        return new SurveyCreateResponseDTO(savedSurvey);
    }

    @Transactional(readOnly = true)
    public SurveyReadResponseDTO getSurvey(Long productId, Long childCategoryId, String parentCategoryName) {

        Product product = productService.findProduct(productId);
        Category category = categoryService.getCategory(childCategoryId, parentCategoryName);
        if (!category.getId().equals(product.getCategory().getId())) {
            throw new GlobalExceptionHandler.CustomException(ErrorCode.SELECT_PRODUCT_CATEGORY_NOT_FOUND);
        }

        checkSurveyUser(product.getSurvey(), productId);

        return SurveyReadResponseDTO.of(product.getSurvey());
    }

    @Transactional(readOnly = true)
    public Survey getSurveyById(Long id) {
        return surveyRepository.findById(id)
                .orElseThrow(() -> new GlobalExceptionHandler.CustomException(ErrorCode.SELECT_SURVEY_NOT_FOUND));
    }

    public void checkSurveyUser(Survey survey, Long product_id) {
        if (!survey.getProduct().getId().equals(product_id)) { // product 생성자와 유저가 일치하지 않으면
            throw new GlobalExceptionHandler.CustomException(ErrorCode.SELECT_SURVEY_INVALID_AUTHORIZATION);
        }
    }
}

