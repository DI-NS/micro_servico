# MedMap API

## Descrição
A **MedMap API** é uma aplicação Spring Boot que implementa autenticação e autorização via JWT (JSON Web Tokens). O objetivo é gerenciar UBSs (Unidades Básicas de Saúde) com endpoints para cadastro, autenticação e geração de tokens de acesso. A API segue princípios **SOLID** e **Clean Code** para facilitar manutenção e extensibilidade.\
A aplicação está disponível [aqui](https://auth-service-yyj4.onrender.com/swagger-ui/index.html#/).
---

## Sumário

- [Visão Geral do Projeto](#visão-geral-do-projeto)
- [Árvore de Diretórios](#árvore-de-diretórios)
- [Descrição dos Arquivos](#descrição-dos-arquivos)
  - [Configurações (config/)](#configurações-config)
  - [Controlador (controller/)](#controlador-controller)
  - [Exceções (exception/)](#exceções-exception)
  - [Modelo (model/)](#modelo-model)
  - [Repositório (repository/)](#repositório-repository)
  - [Serviços (service/)](#serviços-service)
  - [Classe Principal (MedMapApplication.java)](#classe-principal-medmapapplicationjava)
  - [Recursos de Configuração (resources/)](#recursos-de-configuração-resources)
  - [Testes (test/)](#testes-test)
- [Testes e Cobertura de Código](#testes-e-cobertura-de-código)
- [Documentação da API com Swagger/OpenAPI](#documentação-da-api-com-swaggeropenapi)
- [Autenticação e Segurança JWT](#autenticação-e-seguranca-jwt)
- [CI/CD com GitHub Actions](#cicd-com-github-actions)
- [Integração com o Banco de Dados no Supabase](#integração-com-o-banco-de-dados-no-supabase)
- [Relatório de Cobertura de Testes](#relatório-de-cobertura-de-testes)
- [Detalhes Adicionais](#detalhes-adicionais)
  - [Fluxo de Autenticação](#fluxo-de-autenticação)
  - [Propriedades de Configuração](#propriedades-de-configuração)
  - [Exemplos de Rotas Disponíveis](#exemplos-de-rotas-disponíveis)

---

## Visão Geral do Projeto

O microserviço **MedMap** é responsável por autenticação e registro de UBS (Unidades Básicas de Saúde), fornecendo rotas para registrar novas UBS, realizar login, proteger rotas sensíveis via JWT e documentar a API via Swagger. A aplicação está preparada para uso com PostgreSQL (ou Supabase) em produção e H2 em testes, além de possuir pipeline CI/CD e testes unitários com alta cobertura.

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

---

## Árvore de Diretórios

```bash
MedMap/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── MedMap/
│   │   │   │   ├── config/
│   │   │   │   │   ├── OpenApiConfig.java
│   │   │   │   │   └── SecurityConfig.java
│   │   │   │   ├── controller/
│   │   │   │   │   └── AuthController.java
│   │   │   │   ├── exception/
│   │   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   │   ├── InvalidCredentialsException.java
│   │   │   │   │   └── UserAlreadyExistsException.java
│   │   │   │   ├── model/
│   │   │   │   │   └── User.java
│   │   │   │   ├── repository/
│   │   │   │   │   └── UserRepository.java
│   │   │   │   ├── service/
│   │   │   │   │   ├── AuthService.java
│   │   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   │   ├── JwtSecretProvider.java
│   │   │   │   │   └── TokenService.java
│   │   │   │   └── MedMapApplication.java
│   │   ├── resources/
│   │   │   ├── application.yml
│   │   │   └── application-test.properties
│   ├── test/
│   │   ├── java/
│   │   │   ├── MedMap/
│   │   │   │   ├── config/
│   │   │   │   │   ├── ConfigTest.java
│   │   │   │   │   └── OpenApiConfigTest.java
│   │   │   │   ├── controller/
│   │   │   │   │   └── AuthControllerTest.java
│   │   │   │   ├── exception/
│   │   │   │   │   ├── GlobalExceptionHandlerTest.java
│   │   │   │   │   └── JwtAuthenticationFilterTest.java
│   │   │   │   ├── model/
│   │   │   │   │   └── UserTest.java
│   │   │   │   ├── repository/
│   │   │   │   │   └── UserRepositoryTest.java
│   │   │   │   ├── service/
│   │   │   │   │   ├── AuthServiceTest.java
│   │   │   │   │   ├── JwtSecretProviderTest.java
│   │   │   │   │   └── TokenServiceTest.java
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
| OpenApiConfig.java   | Configura o Swagger/OpenAPI, definindo o título, descrição e versão da API.                    |
| SecurityConfig.java  | Configura o Spring Security, definindo endpoints públicos e protegidos com JWT.                 |

### Controlador (controller/)

| Arquivo             | Descrição                                                                                               |
|---------------------|---------------------------------------------------------------------------------------------------------|
| AuthController.java | Controlador REST responsável pelas rotas de autenticação `/auth/register` e `/auth/login`.                |

### Exceções (exception/)

| Arquivo                       | Descrição                                                                                                                     |
|-------------------------------|-------------------------------------------------------------------------------------------------------------------------------|
| GlobalExceptionHandler.java    | Tratador global de exceções. Captura exceções personalizadas e retorna códigos HTTP adequados.                                 |
| InvalidCredentialsException.java | Exceção lançada quando as credenciais fornecidas no login são inválidas.                                                   |
| UserAlreadyExistsException.java | Exceção lançada quando se tenta registrar uma UBS já existente (mesmo CNES).                                                 |

### Modelo (model/)

| Arquivo    | Descrição                                                                                                            |
|------------|----------------------------------------------------------------------------------------------------------------------|
| User.java  | Entidade que representa a UBS, com campos `nomeUbs`, `cnes`, `address`, `hashedPassword`.                          |

### Repositório (repository/)

| Arquivo             | Descrição                                                        |
|---------------------|------------------------------------------------------------------|
| UserRepository.java | Interface do Spring Data JPA para interagir com a entidade `User`. |

### Serviços (service/)

| Arquivo                     | Descrição                                                                                                 |
|-----------------------------|-----------------------------------------------------------------------------------------------------------|
| AuthService.java            | Lógica de autenticação: registra usuários, valida credenciais, gera tokens via `TokenService`.           |
| JwtAuthenticationFilter.java | Filtro que intercepta requisições protegidas, extrai e valida JWT, autenticando o contexto.               |
| JwtSecretProvider.java      | Fornece o segredo usado para assinar os JWT.                                                            |
| TokenService.java           | Responsável por criar o JWT com claims (`cnes`) e expiração.                                            |

### Classe Principal (MedMapApplication.java)

| Arquivo                | Descrição                                                             |
|------------------------|-----------------------------------------------------------------------|
| MedMapApplication.java | Classe principal do projeto. Inicializa o contexto Spring Boot.       |

### Recursos de Configuração (resources/)

| Arquivo                      | Descrição                                                                                               |
|------------------------------|---------------------------------------------------------------------------------------------------------|
| application.yml              | Configurações gerais do projeto, como porta do servidor, configurações do JWT e do banco (em produção). |
| application-test.properties  | Configurações específicas para testes, por exemplo, uso de H2 in-memory, desabilitando PostgreSQL.      |

### Testes (test/)

Cada teste valida o comportamento de seus respectivos componentes:

| Arquivo                          | Descrição                                                       |
|----------------------------------|-----------------------------------------------------------------|
| ConfigTest.java                  | Testa se o contexto carrega as configurações corretamente.      |
| OpenApiConfigTest.java           | Verifica se o Swagger/OpenAPI é configurado adequadamente.      |
| AuthControllerTest.java          | Testes para as rotas de autenticação (registro/login).          |
| GlobalExceptionHandlerTest.java  | Verifica o tratamento global de exceções.                       |
| JwtAuthenticationFilterTest.java | Testa o filtro JWT (rotas públicas e protegidas).               |
| UserTest.java                    | Testes da entidade `User`.                                      |
| UserRepositoryTest.java          | Testa operações do repositório `UserRepository`.                |
| AuthServiceTest.java             | Testes da lógica de autenticação em `AuthService`.              |
| JwtSecretProviderTest.java       | Testa o provedor de segredos JWT.                                |
| TokenServiceTest.java            | Testa a geração de tokens JWT em `TokenService`.                 |

---

## Testes e Cobertura de Código

Os testes foram executados usando **JUnit** e **Mockito**. O **Jacoco** é utilizado para gerar relatórios de cobertura. A cobertura ultrapassa 90%, conforme exigido. Acesse o relatório em `target/site/jacoco/index.html` após rodar:

```bash
mvn clean test
mvn jacoco:report
```

A cobertura final apresentada no relatório é de aproximadamente 98%, garantindo a robustez e confiabilidade do código.

---

## Documentação da API com Swagger/OpenAPI

A configuração do **OpenAPI** está em `OpenApiConfig.java`. A documentação pode ser acessada nos seguintes endpoints:

| Endpoint               | Descrição                      |
|------------------------|--------------------------------|
| `/v3/api-docs`         | Documento OpenAPI em formato JSON. |
| `/swagger-ui/index.html` | Interface interativa do Swagger UI para explorar e testar as rotas da API. |

**Como Acessar:**

1. **Documento JSON:** Navegue até https://auth-service-yyj4.onrender.com/v3/api-docs para obter o documento OpenAPI em JSON.
2. **Swagger UI:** Acesse https://auth-service-yyj4.onrender.com/swagger-ui/index.html para interagir com a documentação de forma visual e testar as rotas diretamente pela interface.

---

## Autenticação e Segurança JWT

A segurança da API é implementada utilizando **Spring Security** com autenticação baseada em **JWT**. A configuração está detalhada nos seguintes arquivos:

| Arquivo                     | Descrição                                                                                                         |
|-----------------------------|-------------------------------------------------------------------------------------------------------------------|
| SecurityConfig.java         | Define as regras de segurança, especificando quais endpoints são públicos e quais exigem autenticação via JWT.    |
| JwtAuthenticationFilter.java | Filtro que intercepta requisições, extrai o token JWT do cabeçalho `Authorization`, valida e autentica o contexto. |
| JwtSecretProvider.java      | Fornece o segredo utilizado para assinar e validar os tokens JWT.                                                 |
| TokenService.java           | Gera tokens JWT com claims específicos e define a expiração dos tokens.                                         |

**Configuração de Segurança:**

- **Endpoints Públicos:** As rotas `/auth/**`, `/swagger-ui/**`, `/v3/api-docs/**` e `/h2-console/**` são configuradas como públicas, permitindo acesso sem autenticação.
- **Endpoints Protegidos:** Todas as outras rotas exigem um token JWT válido no cabeçalho `Authorization: Bearer <token>`.
- **Filtro JWT:** O `JwtAuthenticationFilter` verifica a presença e validade do token JWT, estabelecendo a autenticação no contexto de segurança ou retornando um erro 401 em caso de falha.

**Fluxo de Autenticação:**

1. **Registro (`POST /auth/register`):**
  - Envia-se `nomeUbs`, `cnes`, `address` e `password`.
  - O serviço verifica a existência do `cnes` e, se não existir, registra a UBS com a senha hasheada.

2. **Login (`POST /auth/login`):**
  - Envia-se `cnes` e `password`.
  - O serviço valida as credenciais e, se corretas, gera um token JWT para acesso às rotas protegidas.

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
| **Geração de Relatório Jacoco** | Após os testes, gera o relatório de cobertura com `mvn jacoco:report`.                              |

**Arquivo de Configuração (`.github/workflows/ci.yml`):**

```yaml
name: CI Pipeline Auth-service

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

      # Geração de Relatório Jacoco
      - name: Generate Test Coverage Report
        run: mvn jacoco:report
```

Este pipeline assegura que, a cada alteração na branch principal, o código seja automaticamente compilado, testado e validado, mantendo a integridade e qualidade do projeto.

---

## Integração com o Banco de Dados no Supabase

A aplicação está configurada para utilizar o **Supabase** como banco de dados PostgreSQL em produção. As configurações estão definidas no arquivo `application.yml`.

**Configurações de Banco de Dados (`application.yml`):**

```yaml
spring:
  datasource:
    url: jdbc:postgresql://aws-0-sa-east-1.pooler.supabase.com:6543/postgres
    driver-class-name: org.postgresql.Driver
    username: postgres.kcznmlsdphaxfxotjits
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

**Scripts de Inicialização:**

Caso seja necessário, scripts de inicialização ou migração podem ser adicionados na pasta `src/main/resources` ou gerenciados via ferramentas como **Flyway** ou **Liquibase**. No presente projeto, o Liquibase está desabilitado (`enabled: false`), e a criação do esquema é gerenciada pelo Hibernate.

**Considerações:**

- **Segurança:** As credenciais do banco de dados devem ser armazenadas de forma segura, preferencialmente utilizando variáveis de ambiente ou serviços de gerenciamento de segredos.
- **Conexão:** Assegure-se de que o banco de dados no Supabase esteja configurado para aceitar conexões externas provenientes do serviço de deploy (Render).

---

## Relatório de Cobertura de Testes

A cobertura de testes é essencial para garantir a qualidade e confiabilidade do código. O projeto utiliza **Jacoco** para gerar relatórios detalhados de cobertura.

**Geração do Relatório:**

Após a execução dos testes, o relatório de cobertura pode ser gerado com os seguintes comandos:

```bash
mvn clean test
mvn jacoco:report
```

**Acesso ao Relatório:**

O relatório gerado estará disponível em `target/site/jacoco/index.html`. Abra este arquivo em um navegador para visualizar detalhes sobre a cobertura de testes, incluindo:

| Elemento                 | Cobertura          |
|--------------------------|--------------------|
| **Missed Instructions**  | 13 of 546           |
| **Missed Branches**      | 4 of 38             |
| **Missed Lines**         | 3 of 158            |
| **Missed Methods**       | 0 of 50             |
| **Missed Classes**       | 1 of 12             |

**Interpretação dos Dados:**

- **Instruições:** Número de linhas de código executadas vs. não executadas durante os testes.
- **Ramos:** Cobertura de estruturas condicionais (e.g., `if`, `switch`).
- **Linhas:** Total de linhas de código cobertas.
- **Métodos:** Quantidade de métodos testados adequadamente.
- **Classes:** Cobertura por classe.

**Resumo:**

Com uma cobertura total de aproximadamente 98%, o projeto assegura que a maior parte do código está sendo validamente testada, reduzindo riscos de bugs e facilitando futuras manutenções.

---

## Documentação da API com Swagger/OpenAPI

A documentação interativa da API está disponível através do **Swagger UI** e do documento **OpenAPI**. Isso facilita a compreensão e testes das rotas disponibilizadas.

| Endpoint                     | Descrição                                                                 |
|------------------------------|---------------------------------------------------------------------------|
| `/v3/api-docs`               | Documento OpenAPI em formato JSON, descrevendo todas as rotas e modelos.  |
| `/swagger-ui/index.html`     | Interface interativa do Swagger UI para explorar e testar as rotas da API. |

**Como Utilizar:**

1. **Acessar o Swagger UI:**
  - Navegue até https://auth-service-yyj4.onrender.com/swagger-ui/index.html em seu navegador.
  - Explore as rotas disponíveis, visualize os modelos de dados e execute testes diretamente pela interface.

2. **Acessar o Documento OpenAPI:**
  - Acesse https://auth-service-yyj4.onrender.com/v3/api-docs para obter o documento JSON que descreve a API.
  - Este documento pode ser utilizado para integração com outras ferramentas ou serviços que suportam OpenAPI.

**Benefícios:**

- **Exploração Facilmente:** Permite que desenvolvedores entendam rapidamente as funcionalidades disponíveis.
- **Testes Interativos:** Possibilita a execução de chamadas de API diretamente pelo navegador sem a necessidade de ferramentas externas.
- **Integração:** Facilita a integração com outras aplicações e serviços que consomem a API.

---

## Autenticação e Segurança JWT

A segurança da API é garantida através de autenticação baseada em **JWT** (JSON Web Tokens). O processo envolve a geração, validação e gerenciamento de tokens para proteger rotas sensíveis.

### Componentes Principais:

| Componente               | Descrição                                                                                              |
|--------------------------|--------------------------------------------------------------------------------------------------------|
| `SecurityConfig.java`    | Configura as regras de segurança da aplicação, definindo quais rotas são públicas e quais são protegidas. |
| `JwtAuthenticationFilter.java` | Filtro que intercepta requisições, extrai o token JWT do cabeçalho `Authorization`, valida e autentica o contexto. |
| `JwtSecretProvider.java` | Fornece o segredo utilizado para assinar e validar os tokens JWT.                                     |
| `TokenService.java`      | Responsável por gerar tokens JWT com informações específicas e definir a expiração dos tokens.         |

### Fluxo de Autenticação:

1. **Registro de UBS:**
  - **Endpoint:** `POST /auth/register`
  - **Dados Enviados:** `nomeUbs`, `cnes`, `address`, `password`.
  - **Processo:**
    - Verifica se o `cnes` já está registrado.
    - Se não, hasheia a senha e registra a UBS no banco de dados.
  - **Resposta:** Mensagem de sucesso ou erro se a UBS já existir.

2. **Login:**
  - **Endpoint:** `POST /auth/login`
  - **Dados Enviados:** `cnes`, `password`.
  - **Processo:**
    - Valida as credenciais fornecidas.
    - Se corretas, gera um token JWT contendo o `cnes` como subject.
  - **Resposta:** Token JWT para autenticação nas próximas requisições.

3. **Acesso a Rotas Protegidas:**
  - **Headers Requeridos:** `Authorization: Bearer <token>`
  - **Processo:**
    - O `JwtAuthenticationFilter` intercepta a requisição.
    - Extrai e valida o token JWT.
    - Se válido, autentica o usuário no contexto de segurança.
    - Se inválido ou ausente, retorna um erro 401 Unauthorized.

### Configuração de Segurança (`SecurityConfig.java`):

| Configuração             | Descrição                                                                                     |
|--------------------------|-----------------------------------------------------------------------------------------------|
| **CSRF Desativado**      | A proteção CSRF está desativada para facilitar o uso da API.                                  |
| **Endpoints Públicos**   | Rotas como `/auth/**`, `/swagger-ui/**`, `/v3/api-docs/**`, `/h2-console/**` são públicas.    |
| **Endpoints Protegidos** | Todas as outras rotas exigem autenticação via JWT.                                           |
| **Filtro JWT**           | O `JwtAuthenticationFilter` é adicionado antes do `UsernamePasswordAuthenticationFilter`.      |
| **Tratamento de Exceções** | Define um `AuthenticationEntryPoint` personalizado que retorna 401 Unauthorized em caso de falha. |
| **Encoder de Senha**     | Utiliza `BCryptPasswordEncoder` para hashear senhas de forma segura.                          |

### Gerenciamento de Tokens JWT:

- **Geração de Tokens:** Realizada pelo `TokenService.java`, que cria tokens com claims específicos e define a expiração conforme configurado.
- **Validação de Tokens:** O `JwtAuthenticationFilter.java` verifica a validade do token, incluindo assinatura e expiração, antes de autenticar o contexto.
- **Segredo JWT:** O `JwtSecretProvider.java` gerencia o segredo utilizado para assinar e validar os tokens. Este segredo deve ser mantido seguro e nunca exposto.

---

## CI/CD com GitHub Actions

O pipeline de **CI/CD** está configurado utilizando **GitHub Actions**, garantindo integração contínua e entrega contínua da aplicação. O arquivo de configuração está localizado em `.github/workflows/ci.yml`.

### Passos do Pipeline:

| Passo                        | Descrição                                                                                              |
|------------------------------|--------------------------------------------------------------------------------------------------------|
| **Checkout do Código**       | Utiliza a ação `actions/checkout@v3` para obter o código-fonte do repositório.                         |
| **Configurar Java**          | Configura o JDK 17 utilizando `actions/setup-java@v3` com a distribuição `temurin`.                   |
| **Cache do Maven**           | Utiliza `actions/cache@v3` para armazenar o cache das dependências Maven, melhorando a performance.    |
| **Build com Maven**          | Executa `mvn clean install` para compilar o projeto e instalar as dependências.                        |
| **Executar Testes**          | Executa `mvn test` para rodar os testes unitários.                                                     |
| **Geração de Relatório Jacoco** | Após os testes, gera o relatório de cobertura com `mvn jacoco:report`.                              |

### Arquivo de Configuração (`.github/workflows/ci.yml`):

```yaml
name: CI Pipeline Auth-service

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

      # Geração de Relatório Jacoco
      - name: Generate Test Coverage Report
        run: mvn jacoco:report
```

**Benefícios do Pipeline:**

- **Automatização:** Compilação e testes são executados automaticamente em cada alteração na branch principal, garantindo que apenas código validado seja integrado.
- **Performance:** O cache de dependências Maven reduz o tempo de build em execuções subsequentes.
- **Qualidade:** A geração de relatórios de cobertura assegura que a aplicação mantenha altos padrões de teste.

---

## Integração com o Banco de Dados no Supabase

A integração com o **Supabase** permite que a aplicação utilize um banco de dados PostgreSQL gerenciado em nuvem, proporcionando escalabilidade e confiabilidade.

### Configurações de Integração:

| Configuração                   | Valor                                                    | Descrição                                                                                      |
|--------------------------------|----------------------------------------------------------|------------------------------------------------------------------------------------------------|
| `spring.datasource.url`        | `jdbc:postgresql://aws-0-sa-east-1.pooler.supabase.com:6543/postgres` | URL de conexão com o banco de dados PostgreSQL no Supabase.                                    |
| `spring.datasource.driver-class-name` | `org.postgresql.Driver`                                    | Driver JDBC para PostgreSQL.                                                                    |
| `spring.datasource.username`   | `postgres.kcznmlsdphaxfxotjits`                           | Nome de usuário para autenticação no banco de dados.                                           |
| `spring.datasource.password`   | `Madmap@@021`                                            | Senha para autenticação no banco de dados.                                                     |
| `spring.jpa.hibernate.ddl-auto` | `update`                                                 | Estratégia de criação do esquema do banco de dados. `update` permite que o Hibernate atualize o esquema conforme as entidades. |
| `spring.jpa.properties.hibernate.dialect` | `org.hibernate.dialect.PostgreSQLDialect`                | Dialeto do Hibernate para PostgreSQL.                                                            |
| `spring.jpa.show-sql`          | `true`                                                   | Habilita a exibição de queries SQL no console para depuração.                                   |
| `spring.liquibase.enabled`     | `false`                                                  | Desabilita o uso do Liquibase para migrações de banco de dados.                                 |

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
| **Missed Instructions**  | 13 of 546           |
| **Missed Branches**      | 4 of 38             |
| **Missed Lines**         | 3 of 158            |
| **Missed Methods**       | 0 of 50             |
| **Missed Classes**       | 1 of 12             |

### Interpretação dos Dados

- **Instruições:** Número de linhas de código executadas vs. não executadas durante os testes.
- **Ramos:** Cobertura de estruturas condicionais (e.g., `if`, `switch`).
- **Linhas:** Total de linhas de código cobertas.
- **Métodos:** Quantidade de métodos testados adequadamente.
- **Classes:** Cobertura por classe.

**Resumo:**

Com uma cobertura total de aproximadamente 98%, o projeto assegura que a maior parte do código está sendo validamente testada, reduzindo riscos de bugs e facilitando futuras manutenções.

---

## Detalhes Adicionais

### Fluxo de Autenticação

1. **Registro (`POST /auth/register`):**
  - **Dados Enviados:** `nomeUbs`, `cnes`, `address`, `password`.
  - **Processo:**
    - O `AuthService` verifica se o `cnes` já está registrado no `UserRepository`.
    - Se não existir, a senha é hasheada usando `BCryptPasswordEncoder`.
    - A UBS é então salva no banco de dados.
  - **Resposta:** Mensagem de sucesso ou erro caso o `cnes` já exista.

2. **Login (`POST /auth/login`):**
  - **Dados Enviados:** `cnes`, `password`.
  - **Processo:**
    - O `AuthService` busca a UBS pelo `cnes`.
    - Valida a senha fornecida comparando com o hash armazenado.
    - Se válido, gera um token JWT através do `TokenService`.
  - **Resposta:** Token JWT para autenticação nas próximas requisições.

3. **Acesso a Rotas Protegidas:**
  - **Cabeçalho Requerido:** `Authorization: Bearer <token>`.
  - **Processo:**
    - O `JwtAuthenticationFilter` intercepta a requisição.
    - Extrai e valida o token JWT.
    - Se válido, autentica o usuário no contexto de segurança.
    - Se inválido ou ausente, retorna um erro 401 Unauthorized.

### Propriedades de Configuração

| Propriedade               | Ambiente  | Descrição                                                                                   |
|---------------------------|-----------|---------------------------------------------------------------------------------------------|
| `spring.datasource.url`   | Produção  | URL de conexão com o banco de dados PostgreSQL no Supabase.                                 |
| `spring.datasource.url`   | Testes    | `jdbc:h2:mem:testdb` para uso de banco de dados H2 in-memory durante testes.                 |
| `jwt.secret`              | Todos     | Segredo utilizado para assinar e validar tokens JWT. Deve ser mantido seguro e não exposto.  |
| `jwt.expiration`          | Todos     | Tempo de expiração dos tokens JWT em segundos.                                              |

**Arquivos de Configuração:**

| Arquivo                      | Ambiente  | Descrição                                                                                   |
|------------------------------|-----------|---------------------------------------------------------------------------------------------|
| `application.yml`            | Produção  | Configurações gerais do projeto, incluindo conexão com Supabase e propriedades do JWT.     |
| `application-test.properties`| Testes    | Configurações específicas para testes, como uso do H2 in-memory e desabilitação de segurança.|

### Exemplos de Rotas Disponíveis

| Rota                     | Método | Autenticação | Descrição                                                        |
|--------------------------|--------|--------------|------------------------------------------------------------------|
| `/auth/register`         | POST   | Não          | Registra uma nova UBS, requer `nomeUbs`, `cnes`, `address`, `password`. |
| `/auth/login`            | POST   | Não          | Autentica e retorna um token JWT, requer `cnes` e `password`.    |
| `/swagger-ui/index.html` | GET    | Não          | Documentação interativa da API                                   |
| `/v3/api-docs`           | GET    | Não          | Documento OpenAPI em JSON                                         |
| `/h2-console`            | GET    | Não          | Console H2 (apenas para desenvolvimento e testes)                 |
| `/private/data`          | GET    | Sim          | Exige token JWT, retorna dados protegidos ou 401 se não autenticado. |

**Exemplos de Requisições:**

1. **Registro de UBS:**

   ```bash
   curl -X POST https://auth-service-yyj4.onrender.com/auth/register/auth/register \
   -H 'Content-Type: application/json' \
   -d '{"nomeUbs":"UBS Exemplo","cnes":"123456","address":"Rua ABC","password":"minhaSenha"}'
   ```

2. **Login:**

   ```bash
   curl -X POST https://auth-service-yyj4.onrender.com/auth/register/auth/login \
   -d "cnes=123456" \
   -d "password=minhaSenha"
   ```

   **Resposta (Exemplo):**

   ```
   "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9..."
   ```

3. **Acesso a Rota Protegida:**

   ```bash
   curl -X GET https://auth-service-yyj4.onrender.com/auth/register/private/data \
   -H "Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9..."
   ```

---

## Conclusão

A **MedMap API** oferece uma solução robusta para gerenciamento de UBSs, incorporando práticas de desenvolvimento modernas como autenticação segura via JWT, integração contínua com GitHub Actions, e documentação interativa com Swagger. A alta cobertura de testes assegura a qualidade e confiabilidade da aplicação, tornando-a pronta para produção com integração ao banco de dados Supabase.

Para mais detalhes sobre o código, navegue pela [Árvore de Diretórios](#árvore-de-diretórios) e consulte os arquivos específicos conforme necessário.