FROM adoptopenjdk:latest
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} bank-simulator-application.jar
ENTRYPOINT ["java", "-jar", "bank-simulator-application.jar"]