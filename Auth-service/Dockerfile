# Use uma imagem base com Java JDK 17
FROM eclipse-temurin:17-jdk-jammy

# Define o diretório de trabalho no container
WORKDIR /app

# Copia o arquivo JAR gerado no diretório target para o container
COPY target/MedMap-0.0.1-SNAPSHOT.jar app.jar

# Exponha a porta que o serviço vai utilizar
EXPOSE 8081

# Define o comando para rodar o JAR
CMD ["java", "-jar", "app.jar"]
