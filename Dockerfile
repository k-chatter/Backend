FROM openjdk:17

COPY ./build/libs/*.jar app.jar
COPY src/main/resources/application.yml application.yml
COPY src/main/resources/application-dev.yml application-dev.yml

ENTRYPOINT ["java","-jar","/app.jar"]
