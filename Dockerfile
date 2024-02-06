FROM eclipse-temurin:17-jdk-alpine
RUN apk add --no-cache tzdata
ENV TZ Asia/Seoul
ARG JAR_FILE=build/libs/TestyPie-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} TestyPie.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/TestyPie.jar"]