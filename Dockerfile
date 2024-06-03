FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ./target/lakeside-hotel-newversion-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]