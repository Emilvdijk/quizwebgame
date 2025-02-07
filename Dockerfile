FROM eclipse-temurin:23-jre-alpine
LABEL authors="Emil"
COPY target/quizwebgame-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
# some cool docker commands:
# docker build --tag=emilvdijk/quizwebgame:test .
# docker run -e "SPRING_PROFILES_ACTIVE=prod" -p 8080:8080 -t emilvdijk/quizwebgame:test