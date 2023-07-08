FROM maven:3.8.5-openjdk-17-slim AS builder
WORKDIR /bank_simulator_project
COPY . .
WORKDIR /bank_simulator_project/main
RUN mvn dependency:resolve clean package

FROM openjdk:17-alpine
WORKDIR /bank_simulator_project
COPY --from=builder ./bank_simulator_project/main/target/bank-simulator-0.0.1-SNAPSHOT.jar bank-simulator.jar
ENTRYPOINT ["java", "-jar", "bank-simulator.jar"]






