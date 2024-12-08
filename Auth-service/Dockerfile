# Use uma imagem base com Maven para compilar o projeto
FROM eclipse-temurin:17-jdk-jammy AS build

# Instala Maven e define o diretório de trabalho
WORKDIR /app

# Copia os arquivos do projeto para o container
COPY . .

# Roda o Maven para compilar o projeto e gerar o JAR
RUN ./mvnw clean package -DskipTests

# Fase final: usar apenas o JAR gerado
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copia o JAR gerado na fase anterior para a imagem final
COPY --from=build /app/target/MedMap-0.0.1-SNAPSHOT.jar app.jar

# Exponha a porta que o serviço irá utilizar
EXPOSE 8081

# Define o comando para rodar o JAR
CMD ["java", "-jar", "app.jar"]
