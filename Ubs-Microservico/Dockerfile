# Build with Maven image
FROM maven:3.9.0-eclipse-temurin-17 AS build
WORKDIR /app

# Cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source & package
COPY src/ src/
RUN mvn clean package -Dmaven.test.skip=true -B

# Runtime image
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8083
CMD ["java","-jar","app.jar"]