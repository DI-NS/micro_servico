# Usuário Microserviço

## Descrição

O **Usuário Microserviço** é um serviço que utiliza Spring Boot para interagir com o microserviço de Medicamentos. Ele fornece endpoints para listar medicamentos disponíveis e buscar informações específicas sobre medicamentos pelo ID. A comunicação com o microserviço de Medicamentos é feita por meio do **Feign Client**, promovendo uma integração eficiente entre os microserviços.\
A aplicação está disponível [aqui](https://usuario-microservico.onrender.com/swagger-ui/index.html#).

---

## Sumário

- [Descrição](#descrição)
- [Visão Geral do Projeto](#visão-geral-do-projeto)
- [Árvore de Diretórios](#árvore-de-diretórios)
- [Funcionalidades Principais](#funcionalidades-principais)
- [Descrição dos Arquivos](#descrição-dos-arquivos)
    - [Controlador (controller/)](#controlador-controller)
    - [Serviços (service/)](#serviços-service)
    - [Modelo (model/)](#modelo-model)
    - [Feign Client](#feign-client)
    - [Exceções (exception/)](#exceções-exception)
    - [Testes](#testes)
- [Exemplo de Requisição](#exemplo-de-requisição)
    - [Listar Todos os Medicamentos](#listar-todos-os-medicamentos)
    - [Buscar Medicamento por ID](#buscar-medicamento-por-id)
- [Exemplos de Rotas Disponíveis](#exemplos-de-rotas-disponíveis)
- [Boas Práticas](#boas-práticas)
- [Propriedades de Configuração](#propriedades-de-configuração)
- [Documentação da API com Swagger/OpenAPI](#documentação-da-api-com-swaggeropenapi)
- [Testes e Cobertura de Código](#testes-e-cobertura-de-código)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Configurações do Feign Client](#configurações-do-feign-client)

---

## Visão Geral do Projeto

O **Usuário Microserviço** desempenha o papel de conectar clientes ao microserviço de Medicamentos, centralizando as solicitações relacionadas a medicamentos em uma API única e de fácil uso. Ele utiliza o **Spring Cloud OpenFeign** para realizar chamadas HTTP simplificadas e escaláveis ao microserviço de Medicamentos.

---

## Árvore de Diretórios

```bash
usuario-microservico
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── usuario
│   │   │           └── usuario_microservico
│   │   │               ├── Controller
│   │   │               │   └── UserController.java
│   │   │               ├── Model
│   │   │               │   └── Medicamento.java
│   │   │               ├── Service
│   │   │               │   └── UserService.java
│   │   │               ├── MedicamentoFeignClient.java
│   │   │               └── UsuarioMicroservicoApplication.java
│   │   └── resources
│   │       └── application.properties
│   └── test
│       ├── java
│       │   └── com
│       │       └── usuario
│       │           └── usuario_microservico
│       │               ├── Controller
│       │               │   └── UserControllerTest.java
│       │               ├── Model
│       │               │   └── MedicamentoTest.java
│       │               ├── Service
│       │               │   ├── UserServiceTest.java
│       │               │   ├── GlobalExceptionHandler.java
│       │               │   └── UsuarioMicroservicoApplicationTests.java
│       └── Resource
│           └── application.Test.properties
├── .github/
│   └── workflows/
│       └── ci.yml
├── Dockerfile
└── pom.xml
```

## Funcionalidades Principais

1. **Listar medicamentos disponíveis**:
    - Endpoint: `/usuarios/medicamentos`
    - Método: `GET`
    - Descrição: Retorna uma lista de todos os medicamentos disponíveis, utilizando o Feign Client para buscar os dados do microserviço de Medicamentos.

2. **Buscar medicamento pelo ID**:
    - Endpoint: `/usuarios/medicamentos/{id}`
    - Método: `GET`
    - Descrição: Retorna as informações de um medicamento específico com base no ID fornecido.

---

## Descrição dos Arquivos

### Controlador (controller/)

| Arquivo                  | Descrição                                                                                       |
|--------------------------|-------------------------------------------------------------------------------------------------|
| **UserController.java**  | Define os endpoints REST para listar todos os medicamentos e buscar um medicamento pelo ID.     |

### Serviços (service/)

| Arquivo               | Descrição                                                                                         |
|-----------------------|---------------------------------------------------------------------------------------------------|
| **UserService.java**  | Contém a lógica de negócios. Utiliza o Feign Client para obter dados do microserviço de Medicamentos. |

### Modelo (model/)

| Arquivo               | Descrição                                                        |
|-----------------------|------------------------------------------------------------------|
| **Medicamento.java**  | Representa a estrutura do medicamento com campos como `nome`, `descricao` e `endereco`.            |

### Feign Client

| Arquivo                       | Descrição                                                                 |
|-------------------------------|---------------------------------------------------------------------------|
| **MedicamentoFeignClient.java** | Interface que abstrai as chamadas HTTP para o microserviço de Medicamentos. |

### Exceções (exception/)

| Arquivo                      | Descrição                                                                                   |
|------------------------------|---------------------------------------------------------------------------------------------|
| **GlobalExceptionHandler.java** | Garante o tratamento centralizado de exceções, retornando mensagens apropriadas com códigos HTTP. |

### Testes

#### Controller (controller/)

| Arquivo                     | Descrição                                                                                      |
|-----------------------------|------------------------------------------------------------------------------------------------|
| **UserControllerTest.java** | Testa os endpoints de listagem de medicamentos e busca por ID, além de verificar o tratamento de exceções. |

#### Serviços (service/)

| Arquivo                | Descrição                                                                                   |
|------------------------|---------------------------------------------------------------------------------------------|
| **UserServiceTest.java** | Verifica as interações do serviço com o Feign Client e valida cenários de exceção. |

#### Modelo (model/)

| Arquivo                   | Descrição                                                                                      |
|---------------------------|------------------------------------------------------------------------------------------------|
| **MedicamentoTest.java**  | Testa os métodos `equals`, `toString` e getters/setters da entidade `Medicamento`.               |

---

## Documentação de Uso do Microserviço

### Exemplos de Rotas Disponíveis

| Rota                          | Método | Exemplo de Resposta                                        | Descrição                                          |
|-------------------------------|--------|------------------------------------------------------------|--------------------------------------------------|
| `/usuarios/medicamentos`      | `GET`  | `[{"nome": "Paracetamol" "endereco": "Rua Central, 100"}]` | Retorna uma lista de medicamentos disponíveis.   |
| `/usuarios/medicamentos/{id}` | `GET`  | `{"nome": "Paracetamol" "endereco": "Rua Central, 100"}`                                 | Retorna informações de um medicamento específico. |

### Exemplo de Requisição com `curl`

- Buscar todos os medicamentos:
    ```bash
    curl -X GET https://medicamento-microservico.onrender.com/medicamento
    ```
- Buscar medicamento pelo ID:
    ```bash
    curl -X GET https://medicamento-microservico.onrender.com/medicamento/1
    ```

### Tratamento de Erros

| Código HTTP | Descrição                  | Exemplo de Resposta                              |
|-------------|----------------------------|--------------------------------------------------|
| **404**     | Medicamento não encontrado | `{"message": "Medicamento não encontrado"}`       |
| **500**     | Erro interno no servidor   | `{"message": "Erro ao buscar medicamentos"}`      |

---

## Boas Práticas

- **Camadas bem definidas**:
    - Separação clara entre controlador, serviço e Feign Client.
    - Uso de `@Service` e `@RestController` para melhor organização.

- **Tratamento de Exceções**:
    - Implementação do `GlobalExceptionHandler` para capturar e gerenciar erros de forma centralizada.

- **Testes Abrangentes**:
    - Cobertura de testes para controlador, serviço e entidade.

- **Clean Code**:
    - Nomes de métodos e classes intuitivos.
    - Código modular e reutilizável.

- **Variáveis de Ambiente**:
    - Configuração de URLs e credenciais sensíveis em variáveis de ambiente para maior segurança.

---

## Propriedades de Configuração

As configurações do microserviço são gerenciadas nos arquivos `application.properties` e `application-test.properties`, permitindo flexibilidade para diferentes ambientes.

### Arquivo `application.properties`

Configurações gerais para o ambiente de produção:

```properties
# Nome da aplicação
spring.application.name=usuario-microservico

# Porta do servidor
server.port=8082

# Desabilitar configuração automática de DataSource, já que não há banco de dados local
spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
```

### Arquivo `application-test.properties`

Configurações específicas para o ambiente de teste, incluindo a URL do microserviço de medicamentos:

```properties
# URL do Feign Client para o microserviço de medicamentos em ambiente de teste
feign.client.medicamento.url= https://medicamento-microservico.onrender.com/medicamento

```

---

## Documentação da API com Swagger/OpenAPI

A API está documentada com Swagger/OpenAPI, permitindo explorar e testar as rotas de forma interativa.

### Como acessar

Para acessar a documentação, basta navegar até a URL da documentação em produção:

[Documentação Swagger](https://usuario-microservico.onrender.com/swagger-ui/index.html#)

---

## Testes e Cobertura de Código

Os testes são realizados usando JUnit e Mockito para garantir a funcionalidade e a integridade do sistema.

### Cobertura de Testes

| Elemento                | Missed Instructions | Coverage | Missed Branches | Coverage | Missed | Complexity | Missed | Lines | Missed | Methods | Missed | Classes |
|-------------------------|---------------------|----------|------------------|----------|--------|------------|--------|-------|--------|---------|--------|---------|
| **Total**               | 0 of 32             | 100%     | 0 of 0            | n/a      | 0      | 8          | 0      | 9     | 0      | 8       | 0      | 3       |
| **Controller**          | 0 of 12             | 100%     | 0 of 0            | n/a      | 0      | 3          | 0      | 3     | 0      | 3       | 0      | 1       |
| **Service**             | 0 of 12             | 100%     | 0 of 0            | n/a      | 0      | 3          | 0      | 3     | 0      | 3       | 0      | 1       |
| **Modelo**              | 0 of 8              | 100%     | 0 of 0            | n/a      | 0      | 2          | 0      | 3     | 0      | 2       | 0      | 1       |

### Geração de Relatórios de Cobertura

Use o JaCoCo para gerar relatórios de cobertura:

Execute os testes:
```bash
mvn clean test
```
Gere o relatório de cobertura:
```bash
mvn jacoco:report
```
O relatório será salvo em:
```bash
target/site/jacoco/index.html
```
A cobertura atual está em **100%**, atendendo às boas práticas de desenvolvimento.

---

## Tecnologias Utilizadas

| Tecnologia                      | Descrição                                           |
|---------------------------------|-----------------------------------------------------|
| **Java 17**                     | Linguagem principal.                                |
| **Spring Boot**                 | Framework para desenvolvimento.                    |
| **Spring Cloud OpenFeign**      | Comunicação entre microserviços.                    |
| **JUnit**                       | Framework de testes unitários.                     |
| **Mockito**                     | Framework de mock para testes.                      |
| **Swagger/OpenAPI**             | Documentação da API.                                |
| **Docker**                      | Containerização.                                    |
| **Maven**                       | Gerenciamento de dependências.                      |
| **Jakarta Persistence API (JPA)** | Mapeamento de dados.                               |
| **Lombok**                      | Redução de boilerplate no código.                   |

---

## Configurações do Feign Client

O `MedicamentoFeignClient` utiliza a URL base configurada para o microserviço de Medicamentos:

```
https://medicamento-microservico.onrender.com/medicamento
```