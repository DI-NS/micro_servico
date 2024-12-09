# Medicamento Microserviço

## Descrição
O **Medicamento Microserviço** é uma aplicação Spring Boot responsável por gerenciar medicamentos, oferecendo endpoints para criação, leitura, atualização e deleção de registros de medicamentos. Este microserviço comunica-se com o **Auth-service** para autenticação e autorização via JWT (JSON Web Tokens). A aplicação segue princípios **SOLID** e **Clean Code**, garantindo facilidade de manutenção e extensibilidade. \
A aplicação está disponível [aqui](https://medicamento-microservico.onrender.com/swagger-ui/index.html#).

---

## Sumário

- [Visão Geral do Projeto](#visão-geral-do-projeto)
- [Árvore de Diretórios](#árvore-de-diretórios)
- [Descrição dos Arquivos](#descrição-dos-arquivos)
    - [Configurações (config/)](#configurações-config)
    - [Controlador (controller/)](#controlador-controller)
    - [Modelo (model/)](#modelo-model)
    - [Repositório (repository/)](#repositório-repository)
    - [Serviços (service/)](#serviços-service)
    - [Classe Principal (MedicamentoMicroservicoApplication.java)](#classe-principal-medicamentomicroservicoapplicationjava)
    - [Recursos de Configuração (resources/)](#recursos-de-configuração-resources)
    - [Testes (test/)](#testes-test)
- [Testes e Cobertura de Código](#testes-e-cobertura-de-código)
- [Documentação da API com Swagger/OpenAPI](#documentação-da-api-com-swaggeropenapi)
- [Autenticação e Segurança JWT](#autenticação-e-seguranca-jwt)
- [CI/CD com GitHub Actions](#cicd-com-github-actions)
- [Integração com o Banco de Dados no Supabase](#integração-com-o-banco-de-dados-no-supabase)
- [Relatório de Cobertura de Testes](#relatório-de-cobertura-de-testes)
- [Detalhes Adicionais](#detalhes-adicionais)
    - [Fluxo de Operações](#fluxo-de-operações)
    - [Propriedades de Configuração](#propriedades-de-configuração)
    - [Exemplos de Rotas Disponíveis](#exemplos-de-rotas-disponíveis)

---

## Visão Geral do Projeto

O microserviço **Medicamento Microserviço** é responsável pelo gerenciamento de medicamentos, fornecendo rotas para criar, listar, buscar, atualizar e deletar registros de medicamentos. A aplicação está integrada ao **Auth-service** para garantir que apenas usuários autenticados possam realizar operações sensíveis. A comunicação com o banco de dados PostgreSQL está realizada através do **Supabase**, proporcionando uma solução escalável e confiável para armazenamento de dados.

**Principais Tecnologias:**

| Tecnologia       | Uso                                  |
|------------------|--------------------------------------|
| Java 17          | Linguagem principal                  |
| Spring Boot      | Framework principal                  |
| Spring Security  | Segurança e Autenticação JWT         |
| JUnit, Mockito   | Testes unitários                     |
| Swagger/OpenAPI  | Documentação da API                  |
| Maven            | Gerenciamento de dependências        |
| GitHub Actions   | CI/CD Pipeline                       |
| Docker           | Containerização da aplicação         |
| Supabase         | Banco de dados PostgreSQL gerenciado  |

---

## Árvore de Diretórios

```bash
Medicamento-Microservico/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── medicamento/
│   │   │           └── medicamento_microservico/
│   │   │               ├── config/
│   │   │               │   ├── OpenApiConfig.java
│   │   │               │   └── SecurityConfig.java
│   │   │               ├── controller/
│   │   │               │   └── MedicamentoController.java
│   │   │               ├── exception/
│   │   │               │   └── [Exceções se houver]
│   │   │               ├── model/
│   │   │               │   └── Medicamento.java
│   │   │               ├── repository/
│   │   │               │   └── MedicamentoRepository.java
│   │   │               ├── service/
│   │   │               │   ├── AuthService.java
│   │   │               │   ├── JwtAuthenticationFilter.java
│   │   │               │   ├── JwtSecretProvider.java
│   │   │               │   └── TokenService.java
│   │   │               └── MedicamentoMicroservicoApplication.java
│   │   └── resources/
│   │       ├── application.yml
│   │       └── application-test.properties
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── medicamento/
│       │           └── medicamento_microservico/
│       │               ├── Config/
│       │               │   ├── OpenApiConfigTest.java
│       │               │   └── SecurityConfigTest.java
│       │               ├── Controller/
│       │               │   └── MedicamentoControllerTest.java
│       │               ├── Model/
│       │               │   └── MedicamentoTest.java
│       │               ├── Repository/
│       │               │   └── MedicamentoRepositoryTest.java
│       │               └── Service/
│       │                   ├── AuthServiceTest.java
│       │                   ├── JwtAuthenticationFilterTest.java
│       │                   ├── JwtSecretProviderTest.java
│       │                   └── TokenServiceTest.java
│       └── resources/
│           └── application-test.properties
├── .github/
│   └── workflows/
│       └── ci.yml
├── Dockerfile
└── pom.xml
```

---

## Descrição dos Arquivos

### Configurações (config/)

| Arquivo              | Descrição                                                                                      |
|----------------------|------------------------------------------------------------------------------------------------|
| OpenApiConfig.java   | Configura o Swagger/OpenAPI, definindo o esquema de segurança e detalhes da documentação.     |
| SecurityConfig.java  | Configura o Spring Security, definindo endpoints públicos e protegidos com JWT.               |

### Controlador (controller/)

| Arquivo                 | Descrição                                                                                                |
|-------------------------|----------------------------------------------------------------------------------------------------------|
| MedicamentoController.java | Controlador REST responsável pelas rotas de gerenciamento de medicamentos (`/medicamento`).            |

### Modelo (model/)

| Arquivo        | Descrição                                                                                                            |
|----------------|----------------------------------------------------------------------------------------------------------------------|
| Medicamento.java | Entidade que representa o Medicamento, com campos `nome`, `descricao`, `endereco` e `id`.                           |

### Repositório (repository/)

| Arquivo                   | Descrição                                                      |
|---------------------------|----------------------------------------------------------------|
| MedicamentoRepository.java | Interface do Spring Data JPA para interagir com a entidade `Medicamento`. |

### Serviços (service/)

| Arquivo                     | Descrição                                                                                                 |
|-----------------------------|-----------------------------------------------------------------------------------------------------------|
| AuthService.java            | Serviço responsável por interagir com o **Auth-service** para validação e obtenção de informações de autenticação. |
| JwtAuthenticationFilter.java | Filtro que intercepta requisições protegidas, extrai e valida JWT, autenticando o contexto.               |
| JwtSecretProvider.java      | Fornece o segredo usado para assinar os JWT.                                                            |
| TokenService.java           | Responsável por criar o JWT com claims (`cnes`) e expiração.                                            |

### Classe Principal (MedicamentoMicroservicoApplication.java)

| Arquivo                             | Descrição                                                             |
|-------------------------------------|-----------------------------------------------------------------------|
| MedicamentoMicroservicoApplication.java | Classe principal do projeto. Inicializa o contexto Spring Boot.       |

### Recursos de Configuração (resources/)

| Arquivo                      | Descrição                                                                                               |
|------------------------------|---------------------------------------------------------------------------------------------------------|
| application.yml              | Configurações gerais do projeto, como porta do servidor, configurações do JWT e do banco (em produção). |
| application-test.properties  | Configurações específicas para testes, por exemplo, uso de H2 in-memory, desabilitando PostgreSQL.      |

### Testes (test/)

Cada teste valida o comportamento de seus respectivos componentes:

| Arquivo                          | Descrição                                                       |
|----------------------------------|-----------------------------------------------------------------|
| OpenApiConfigTest.java           | Testa a configuração do Swagger/OpenAPI.                       |
| SecurityConfigTest.java          | Verifica as regras de segurança e acesso aos endpoints.         |
| MedicamentoControllerTest.java    | Testes para as rotas de gerenciamento de medicamentos.         |
| MedicamentoTest.java              | Testes da entidade `Medicamento`.                               |
| MedicamentoRepositoryTest.java    | Testa operações do repositório `MedicamentoRepository`.         |
| AuthServiceTest.java             | Testes da interação com o **Auth-service** no `AuthService`.    |
| JwtAuthenticationFilterTest.java | Testa o filtro JWT para autenticação de requisições.            |
| JwtSecretProviderTest.java       | Testa o provedor de segredos JWT.                                |
| TokenServiceTest.java            | Testa a geração e validação de tokens JWT no `TokenService`.      |

---

## Testes e Cobertura de Código

Os testes foram executados utilizando **JUnit** e **Mockito**. O **Jacoco** é empregado para gerar relatórios de cobertura, garantindo que mais de 90% do código esteja coberto pelos testes.

**Comandos para Gerar o Relatório:**

```bash
mvn clean test
mvn jacoco:report
```

Após a execução, o relatório estará disponível em `target/site/jacoco/index.html`.

---

## Documentação da API com Swagger/OpenAPI

A documentação interativa da API está disponível através do **Swagger UI** e do documento **OpenAPI**.

| Endpoint                   | Descrição                                                                 |
|----------------------------|---------------------------------------------------------------------------|
| `/v3/api-docs`             | Documento OpenAPI em formato JSON, descrevendo todas as rotas e modelos.  |
| `/swagger-ui.html`         | Interface interativa do Swagger UI para explorar e testar as rotas da API. |

**Como Acessar:**

1. **Documento JSON:** Navegue até https://usuario-microservico.onrender.com/v3/api-docs  para obter o documento OpenAPI em JSON.
2. **Swagger UI:** Acesse https://usuario-microservico.onrender.com/swagger-ui.html para interagir com a documentação de forma visual e testar as rotas diretamente pela interface.

---

## Autenticação e Segurança JWT

A segurança da API é implementada utilizando **Spring Security** com autenticação baseada em **JWT**. A configuração detalhada está nos arquivos `SecurityConfig.java` e `JwtAuthenticationFilter.java`.

| Arquivo                     | Descrição                                                                                                         |
|-----------------------------|-------------------------------------------------------------------------------------------------------------------|
| SecurityConfig.java         | Define as regras de segurança, especificando quais endpoints são públicos e quais exigem autenticação via JWT.    |
| JwtAuthenticationFilter.java | Filtro que intercepta requisições, extrai o token JWT do cabeçalho `Authorization`, valida e autentica o contexto. |
| JwtSecretProvider.java      | Fornece o segredo utilizado para assinar e validar os tokens JWT.                                                 |
| TokenService.java           | Gera tokens JWT com informações específicas e define a expiração dos tokens.                                     |

**Configuração de Segurança:**

- **Endpoints Públicos:** As rotas GET de `/medicamento`, `/swagger-ui/**`, `/v3/api-docs/**`, e recursos estáticos relacionados ao Swagger são públicas, permitindo acesso sem autenticação.
- **Endpoints Protegidos:** As rotas POST, PUT e DELETE de `/medicamento/**` exigem autenticação via JWT.

**Fluxo de Autenticação:**

1. **Intercepção da Requisição:**
    - O `JwtAuthenticationFilter` intercepta todas as requisições protegidas.

2. **Extração e Validação do Token:**
    - Extrai o token JWT do cabeçalho `Authorization: Bearer <token>`.
    - Valida a assinatura e a expiração do token.

3. **Autenticação do Usuário:**
    - Se o token for válido, autentica o usuário no contexto de segurança.
    - Se inválido ou ausente, retorna um erro 401 Unauthorized.

---

## CI/CD com GitHub Actions

O pipeline de **CI/CD** está configurado utilizando **GitHub Actions**, garantindo integração contínua e entrega contínua da aplicação. O arquivo de configuração está localizado em `.github/workflows/ci.yml`.

**Pipeline de CI:**

| Passo                        | Descrição                                                                                              |
|------------------------------|--------------------------------------------------------------------------------------------------------|
| **Trigger**                  | O pipeline é acionado em pushs e pull requests para a branch `main`.                                 |
| **Checkout do Código**       | Utiliza a ação `actions/checkout@v3` para obter o código-fonte do repositório.                         |
| **Configurar Java**          | Configura o JDK 17 utilizando `actions/setup-java@v3` com a distribuição `temurin`.                   |
| **Cache do Maven**           | Utiliza `actions/cache@v3` para armazenar o cache das dependências Maven, melhorando a performance.    |
| **Build com Maven**          | Executa `mvn clean install` para compilar o projeto e instalar as dependências.                        |
| **Executar Testes**          | Executa `mvn test` para rodar os testes unitários.                                                     |

**Arquivo de Configuração (`.github/workflows/ci.yml`):**

```yaml
name: CI Pipeline Medicamento-microservico

on:
  push:
    branches:
      - main
  pull_request:
    branches:
      - main

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      # Checkout do código-fonte
      - name: Checkout Code
        uses: actions/checkout@v3

      # Configurar Java
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'

      # Cache do Maven para melhorar performance
      - name: Cache Maven
        uses: actions/cache@v3
        with:
          path: ~/.m2/repository
          key: ${{ runner.os }}-maven-${{ hashFiles('**/pom.xml') }}
          restore-keys: |
            ${{ runner.os }}-maven-

      # Build do Projeto
      - name: Build with Maven
        run: mvn clean install

      # Executar Testes
      - name: Run Tests
        run: mvn test
```

---

## Integração com o Banco de Dados no Supabase

A aplicação está configurada para utilizar o **Supabase** como banco de dados PostgreSQL em produção. As configurações estão definidas no arquivo `application.yml`.

**Configurações de Banco de Dados (`application.yml`):**

```yaml
spring:
  application:
    name: medicamento-microservico

  datasource:
    url: jdbc:postgresql://aws-0-sa-east-1.pooler.supabase.com:6543/postgres
    driver-class-name: org.postgresql.Driver
    username: postgres.kytegxoynfsmmgpdncwx
    password: Madmap@@021
    initialization-mode: always
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate.dialect: org.hibernate.dialect.PostgreSQLDialect
    show-sql: true
  liquibase:
    enabled: false
server:
  port: 8080

springdoc:
  api-docs:
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html

jwt:
  secret: ${JWT_SECRET:XPdF3BA71M1oV1ZkkHXVkX4FZSeC1lwX4Ltnv2HmHHA=}
```

**Descrição das Propriedades:**

| Propriedade                   | Descrição                                                      |
|-------------------------------|----------------------------------------------------------------|
| `spring.datasource.url`       | URL de conexão com o banco de dados PostgreSQL no Supabase.    |
| `spring.datasource.driver-class-name` | Driver JDBC para PostgreSQL.                                     |
| `spring.datasource.username`  | Nome de usuário para autenticação no banco de dados.           |
| `spring.datasource.password`  | Senha para autenticação no banco de dados.                     |
| `spring.jpa.hibernate.ddl-auto` | Define a estratégia de criação do esquema do banco de dados. `update` permite que o Hibernate atualize o esquema conforme as entidades. |
| `spring.jpa.properties.hibernate.dialect` | Dialeto do Hibernate para PostgreSQL.                                |
| `spring.jpa.show-sql`         | Habilita a exibição de queries SQL no console para depuração.   |
| `spring.liquibase.enabled`    | Desabilita o uso do Liquibase para migrações de banco de dados. |
| `server.port`                 | Porta em que o servidor será executado.                        |
| `springdoc.api-docs.path`     | Caminho para o documento OpenAPI em JSON.                      |
| `springdoc.swagger-ui.path`   | Caminho para a interface interativa do Swagger UI.             |
| `jwt.secret`                  | Segredo utilizado para assinar e validar tokens JWT.            |

**Considerações:**

- **Segurança das Credenciais:** As informações sensíveis, como `username` e `password`, devem ser armazenadas de forma segura, preferencialmente utilizando variáveis de ambiente ou serviços de gerenciamento de segredos.
- **Conexão Segura:** A conexão com o Supabase deve utilizar protocolos seguros (e.g., SSL) para proteger os dados em trânsito.

---

## Relatório de Cobertura de Testes

A cobertura de testes é fundamental para assegurar a qualidade e a confiabilidade do código. O projeto utiliza **Jacoco** para gerar relatórios detalhados de cobertura.

### Geração do Relatório

Após a execução dos testes, o relatório de cobertura pode ser gerado com os seguintes comandos:

```bash
mvn clean test
mvn jacoco:report
```

### Acesso ao Relatório

O relatório gerado estará disponível em `target/site/jacoco/index.html`. Abra este arquivo em um navegador para visualizar detalhes sobre a cobertura de testes, incluindo:

| Elemento                 | Cobertura          |
|--------------------------|--------------------|
| **Missed Instructions**  | 5 of 433           |
| **Missed Branches**      | 7 of 50            |
| **Missed Lines**         | 1 of 110           |
| **Missed Methods**       | 0 of 26            |
| **Missed Classes**       | 1 of 7             |

**Resumo:**

Com uma cobertura total de aproximadamente **98%**, o projeto assegura que a maior parte do código está sendo validamente testada, reduzindo riscos de bugs e facilitando futuras manutenções.

---

## Detalhes Adicionais

### Fluxo de Operações

1. **Criação de Medicamento (`POST /medicamento`):**
    - **Dados Enviados:** `nome`, `descricao`, `endereco`.
    - **Processo:**
        - Validação dos campos obrigatórios.
        - Verificação de duplicação pelo `nome` e `descricao`.
        - Salvamento no banco de dados.
    - **Resposta:** Objeto `Medicamento` criado.

2. **Listagem de Medicamentos (`GET /medicamento`):**
    - **Processo:**
        - Recupera todos os registros de medicamentos do banco de dados.
    - **Resposta:** Lista de objetos `Medicamento`.

3. **Busca por ID (`GET /medicamento/{id}`):**
    - **Processo:**
        - Recupera o medicamento pelo `id` fornecido.
        - Retorna erro se não encontrado.
    - **Resposta:** Objeto `Medicamento` correspondente ao `id`.

4. **Atualização de Medicamento (`PUT /medicamento/{id}`):**
    - **Dados Enviados:** `nome`, `descricao`, `endereco`.
    - **Processo:**
        - Validação do `id` e dos dados.
        - Atualização dos campos no registro existente.
    - **Resposta:** Objeto `Medicamento` atualizado.

5. **Deleção de Medicamento (`DELETE /medicamento/{id}`):**
    - **Processo:**
        - Verificação da existência do `id`.
        - Remoção do registro do banco de dados.
    - **Resposta:** Status HTTP 204 No Content.

### Propriedades de Configuração

| Propriedade               | Ambiente  | Descrição                                                                                   |
|---------------------------|-----------|---------------------------------------------------------------------------------------------|
| `spring.datasource.url`   | Produção  | URL de conexão com o banco de dados PostgreSQL no Supabase.                                 |
| `spring.datasource.url`   | Testes    | `jdbc:h2:mem:testdb` para uso de banco de dados H2 in-memory durante testes.                 |
| `jwt.secret`              | Todos     | Segredo utilizado para assinar e validar tokens JWT. Deve ser mantido seguro e não exposto.  |
| `server.port`             | Todos     | Porta em que o servidor será executado (default: 8080).                                      |

**Arquivos de Configuração:**

| Arquivo                      | Ambiente  | Descrição                                                                                               |
|------------------------------|-----------|---------------------------------------------------------------------------------------------------------|
| `application.yml`            | Produção  | Configurações gerais do projeto, incluindo conexão com Supabase e propriedades do JWT.                |
| `application-test.properties`| Testes    | Configurações específicas para testes, como uso do H2 in-memory e desabilitação de segurança.            |

### Exemplos de Rotas Disponíveis

| Rota                     | Método | Autenticação | Descrição                                                        |
|--------------------------|--------|--------------|------------------------------------------------------------------|
| `/medicamento`           | POST   | Sim          | Cria um novo medicamento, requer `nome`, `descricao`, `endereco`. |
| `/medicamento`           | GET    | Não          | Lista todos os medicamentos registrados.                         |
| `/medicamento/{id}`      | GET    | Não          | Busca um medicamento específico pelo `id`.                       |
| `/medicamento/{id}`      | PUT    | Sim          | Atualiza as informações de um medicamento existente.             |
| `/medicamento/{id}`      | DELETE | Sim          | Deleta um medicamento pelo `id`.                                 |
| `/swagger-ui.html`       | GET    | Não          | Documentação interativa da API                                   |
| `/v3/api-docs`           | GET    | Não          | Documento OpenAPI em JSON                                         |
| `/h2-console`            | GET    | Não          | Console H2 (apenas para desenvolvimento e testes)                 |

**Exemplos de Requisições:**

1. **Criação de Medicamento:**

   ```bash
   curl -X POST https://medicamento-microservico.onrender.com/swagger-ui/index.html#/medicamento \
   -H 'Content-Type: application/json' \
   -H 'Authorization: Bearer <token>' \
   -d '{"nome":"Paracetamol","descricao":"Analgésico","endereco":"Rua ABC"}'
   ```

2. **Listagem de Medicamentos:**

   ```bash
   curl -X GET https://medicamento-microservico.onrender.com/medicamento 
   ```

3. **Busca por ID:**

   ```bash
   curl -X GET https://medicamento-microservico.onrender.com/medicamento/1
   ```

4. **Atualização de Medicamento:**

   ```bash
   curl -X PUT https://medicamento-microservico.onrender.com/medicamento/1 \
   -H 'Content-Type: application/json' \
   -H 'Authorization: Bearer <token>' \
   -d '{"nome":"Ibuprofeno","descricao":"Antiinflamatório","endereco":"Rua DEF"}'
   ```

5. **Deleção de Medicamento:**

   ```bash
   curl -X DELETE https://medicamento-microservico.onrender.com/medicamento/1 \
   -H 'Authorization: Bearer <token>'
   ```

---

## Conclusão

O **Medicamento Microserviço** oferece uma solução eficiente para o gerenciamento de medicamentos, incorporando práticas modernas de desenvolvimento como autenticação segura via JWT, integração contínua com GitHub Actions, e documentação interativa com Swagger. A alta cobertura de testes assegura a qualidade e confiabilidade da aplicação, tornando-a pronta para produção com integração ao banco de dados Supabase.

Para mais detalhes sobre o código, navegue pela [Árvore de Diretórios](#árvore-de-diretórios) e consulte os arquivos específicos conforme necessário.