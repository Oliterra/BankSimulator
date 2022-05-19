FROM maven:3.8.5-openjdk-17-slim
WORKDIR /bank_simulator_project
COPY . .
WORKDIR /bank_simulator_project/main
ENTRYPOINT ["mvn", "dependency:resolve", "spring-boot:run"]






