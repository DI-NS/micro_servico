# Use uma imagem base com Maven para compilar o projeto
FROM eclipse-temurin:17-jdk-jammy AS build

# Define o diretório de trabalho
WORKDIR /app

# Copia os arquivos do projeto para o container
COPY . .

# Garante que o script mvnw tenha permissão de execução
RUN chmod +x mvnw

# Roda o Maven para compilar o projeto e gerar o JAR
RUN ./mvnw clean package -DskipTests

# Fase final: usar apenas o JAR gerado
FROM eclipse-temurin:17-jdk-jammy

# Define o diretório de trabalho no container
WORKDIR /app

# Copia o JAR gerado na fase anterior para a imagem final
COPY --from=build /app/target/medicamento-microservico-0.0.1-SNAPSHOT.jar app.jar

# Exponha a porta que o serviço irá utilizar
EXPOSE 8080

# Define o comando para rodar o JAR
CMD ["java", "-jar", "app.jar"]
