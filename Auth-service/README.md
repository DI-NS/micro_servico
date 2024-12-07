# MedMap API

## Descrição
A **MedMap API** é uma aplicação Spring Boot que implementa autenticação e autorização via JWT (JSON Web Tokens). O objetivo é gerenciar UBSs (Unidades Básicas de Saúde) com endpoints para cadastro, autenticação e geração de tokens de acesso. A API segue princípios **SOLID** e **Clean Code** para facilitar manutenção e extensibilidade.

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
- [Autenticação e Segurança JWT](#autenticação-e-segurança-jwt)
- [CI/CD com GitHub Actions](#cicd-com-github-actions)
- [Boas Práticas, SOLID e Clean Code](#boas-práticas-solid-e-clean-code)
- [Detalhes Adicionais e Considerações](#detalhes-adicionais-e-considerações)
  - [Fluxo de Autenticação](#fluxo-de-autenticação)
  - [Propriedades de Configuração](#propriedades-de-configuração)
  - [Integração com Outros Microserviços](#integração-com-outros-microserviços)
  - [CI/CD](#cicd)
  - [Métricas e Monitoramento](#métricas-e-monitoramento)
  - [Boas Práticas](#boas-práticas-1)
  - [Exemplos de Rotas Disponíveis](#exemplos-de-rotas-disponíveis)

---

## Visão Geral do Projeto

O microserviço **MedMap** é responsável por autenticação e registro de UBS (Unidades Básicas de Saúde), fornecendo rotas para registrar novas UBS, realizar login, proteger rotas sensíveis via JWT e documentar a API via Swagger. A aplicação está preparada para uso com PostgreSQL (ou Supabase) em produção e H2 em testes, além de possuir pipeline CI/CD e testes unitários com alta cobertura.

**Principais Tecnologias:**

| Tecnologia       | Uso                                |
|------------------|-------------------------------------|
| Java 17          | Linguagem principal                 |
| Spring Boot      | Framework principal                 |
| Spring Security  | Segurança e Autenticação JWT       |
| JUnit, Mockito   | Testes unitários                   |
| Swagger/OpenAPI  | Documentação da API                |
| Maven            | Gerenciamento de dependências       |
| GitHub Actions   | CI/CD Pipeline                      |

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
│   ├── test/
│   │   ├── java/
│   │   │   ├── MedMap/
│   │   │   │   ├── MedMapApplicationMainTest.java
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
│   │   ├── resources/
│   │   │   └── application-test.properties
├── .github/
│   └── workflows/
│       └── ci.yml
└── pom.xml
```

---

## Descrição dos Arquivos

### Configurações (config/)

| Arquivo              | Descrição                                                                    |
|----------------------|--------------------------------------------------------------------------------|
| OpenApiConfig.java   | Configura o Swagger/OpenAPI, definindo o título, descrição e versão da API.    |
| SecurityConfig.java  | Configura o Spring Security, definindo endpoints públicos e protegidos com JWT. |

### Controlador (controller/)

| Arquivo             | Descrição                                                                 |
|---------------------|---------------------------------------------------------------------------|
| AuthController.java | Controlador REST responsável pelas rotas de autenticação /auth/register e /auth/login. |

### Exceções (exception/)

| Arquivo                       | Descrição                                                                             |
|--------------------------------|-----------------------------------------------------------------------------------------|
| GlobalExceptionHandler.java    | Tratador global de exceções. Captura exceções personalizadas e retorna códigos HTTP adequados. |
| InvalidCredentialsException.java | Exceção lançada quando as credenciais fornecidas no login são inválidas.               |
| UserAlreadyExistsException.java | Exceção lançada quando se tenta registrar uma UBS já existente (mesmo CNES).             |

### Modelo (model/)

| Arquivo    | Descrição                                                      |
|------------|----------------------------------------------------------------|
| User.java  | Entidade que representa a UBS, com campos nomeUbs, cnes, address, hashedPassword. |

### Repositório (repository/)

| Arquivo            | Descrição                                                        |
|--------------------|------------------------------------------------------------------|
| UserRepository.java | Interface do Spring Data JPA para interagir com a entidade User. |

### Serviços (service/)

| Arquivo                    | Descrição                                                                                 |
|----------------------------|---------------------------------------------------------------------------------------------|
| AuthService.java           | Lógica de autenticação: registra usuários, valida credenciais, gera tokens via TokenService. |
| JwtAuthenticationFilter.java | Filtro que intercepta requisições protegidas, extrai e valida JWT, autenticando o contexto. |
| JwtSecretProvider.java     | Gera ou fornece o segredo usado para assinar os JWT.                                          |
| TokenService.java          | Responsável por criar o JWT com claims (cnes, nomeUbs) e expiração.                        |

### Classe Principal (MedMapApplication.java)

| Arquivo                | Descrição                                                             |
|------------------------|---------------------------------------------------------------------|
| MedMapApplication.java | Classe principal do projeto. Inicializa o contexto Spring Boot.     |

### Recursos de Configuração (resources/)

- **application.yml**: Configurações gerais do projeto, como porta do servidor, configs do JWT e do banco (em produção).
- **application-test.properties**: Configurações específicas para testes, por exemplo, uso de H2 in-memory, desabilitando PostgreSQL.

### Testes (test/)

Cada teste valida o comportamento de seus respectivos componentes:

| Arquivo                          | Descrição                                                       |
|----------------------------------|-----------------------------------------------------------------|
| MedMapApplicationMainTest.java   | Testa o contexto da aplicação principal.                        |
| ConfigTest.java                  | Testa se o contexto carrega as configurações corretamente.       |
| OpenApiConfigTest.java           | Verifica se o Swagger/OpenAPI é configurado adequadamente.      |
| AuthControllerTest.java          | Testes para as rotas de autenticação (registro/login).          |
| GlobalExceptionHandlerTest.java  | Verifica o tratamento global de exceções.                        |
| JwtAuthenticationFilterTest.java | Testa o filtro JWT (rotas públicas e protegidas).               |
| UserTest.java                    | Testes da entidade User.                                         |
| UserRepositoryTest.java          | Testa operações do repositório UserRepository.                 |
| AuthServiceTest.java             | Testes da lógica de autenticação em AuthService.               |
| JwtSecretProviderTest.java       | Testa o provedor de segredos JWT.                                |
| TokenServiceTest.java            | Testa a geração de tokens JWT em TokenService.                 |

---

## Testes e Cobertura de Código

Os testes foram executados usando JUnit e Mockito. O Jacoco é utilizado para gerar relatórios de cobertura. A cobertura ultrapassa 90%, conforme exigido. Acesse o relatório em `target/site/jacoco/index.html` após rodar:

```bash
mvn clean test
mvn jacoco:report
```

A cobertura final é de aproximadamente 98%, superando o requisito.

## Documentação da API com Swagger/OpenAPI

A configuração do OpenAPI está em `OpenApiConfig.java`. A documentação pode ser acessada nos endpoints:

- `/v3/api-docs` para o documento JSON
- `/swagger-ui/index.html` para a UI interativa Swagger.

Dessa forma, é fácil explorar e testar as rotas da API.

## Autenticação e Segurança JWT

- O arquivo `SecurityConfig.java` determina quais rotas são públicas ou protegidas.
- O `JwtAuthenticationFilter.java` valida o token JWT presente no cabeçalho `Authorization: Bearer <token>` antes de permitir acesso às rotas protegidas.
- O `AuthService.java` gera tokens JWT usando o `TokenService.java` após validar o usuário.
- O `JwtSecretProvider.java` gerencia o segredo usado na assinatura dos tokens JWT.

As rotas `/auth/**` são públicas, permitindo registro e login. Outras rotas, como `/private/data`, requerem um token JWT válido para acesso.

## CI/CD com GitHub Actions

No diretório `.github/workflows/` encontra-se o arquivo `ci.yml` que descreve o pipeline:

- Build da aplicação
- Execução dos testes
- Cache de dependências Maven

Isso garante que a aplicação seja automaticamente testada e validada em cada commit ou pull request.

## Boas Práticas, SOLID e Clean Code

O código segue princípios SOLID, separando responsabilidades:

- **Single Responsibility**: Cada classe tem um propósito claro (Controlador só controla, Serviço só trata lógica, etc.).
- **Open/Closed, Liskov, Interface Segregation, Dependency Inversion**: A injeção de dependência, o uso de interfaces em repositórios e a separação de camadas tornam o código extensível e fácil de manter.
- **Clean Code**: Nomes claros, comentários explicativos, testes bem estruturados e documentação adicionada.

---

## Detalhes Adicionais e Considerações

### Fluxo de Autenticação

1. **Registro (`POST /auth/register`)**:
  - O cliente envia `nomeUbs`, `cnes`, `address`, `password`.
  - O `AuthService` verifica se o `cnes` já existe no `UserRepository`. Caso exista, lança `UserAlreadyExistsException`.
  - Caso contrário, a senha é codificada (`BCryptPasswordEncoder`) e o usuário é salvo.
  - Retorna uma mensagem de sucesso.

   **Exemplo de Requisição:**
   ```bash
   curl -X POST http://localhost:8081/auth/register \
   -H 'Content-Type: application/json' \
   -d '{"nomeUbs":"UBS Exemplo","cnes":"123456","address":"Rua ABC","password":"minhaSenha"}'
   ```

2. **Login (`POST /auth/login`)**:
  - O cliente envia `cnes` e `password` como parâmetros.
  - O `AuthService` busca o usuário por `cnes`.
  - Verifica se a senha corresponde ao hash armazenado. Se não, lança `InvalidCredentialsException`.
  - Em caso de sucesso, gera um token JWT com `TokenService` que inclui `cnes` no subject e `nomeUbs` como claim.

   **Exemplo de Requisição:**
   ```bash
   curl -X POST http://localhost:8081/auth/login \
   -d "cnes=123456" \
   -d "password=minhaSenha"
   ```

   **Resposta (Exemplo):**
   ```
   "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9..."
   ```

   Esse token deve ser usado no cabeçalho `Authorization: Bearer <token>` para acessar rotas protegidas.

### Propriedades de Configuração

- `application.yml`:  incluir configurações de banco para produção apontando para Supabase e JWT expiration.
- `application-test.properties`: Configura o uso do H2 durante os testes, garantindo isolamento do ambiente de testes.

### Integração com Outros Microserviços

O MedMap pode ser integrado com microserviços de UBS e Medicamentos:

- O microserviço MedMap fornece autenticação JWT. Outros microserviços podem validar o token fornecido pelo cliente (via chave compartilhada ou endpoint de validação se implementado).
- Ao centralizar a autenticação no MedMap, os outros microserviços podem confiar nos tokens gerados e focar apenas na lógica de seus respectivos domínios.

### CI/CD

O arquivo `.github/workflows/ci.yml` dispara a pipeline no push ou pull request para a branch `main`.

**Passos:**

- Checkout do código
- Setup do JDK 17
- Cache do Maven
- Build com `mvn clean install`

No futuro, podem ser adicionados passos de deploy automático para um ambiente de teste ou produção.

### Boas Práticas

- Código organizado por camadas: controller, service, repository e model.
- Tratamento de exceções com classes dedicadas.
- Testes unitários para cada camada chave.
- Uso de `@ActiveProfiles("test")` para ambientes de teste isolados.
- Uso de JWT seguro, evitando exposição do segredo. Em produção, esse segredo deve ser armazenado em variáveis de ambiente seguras.

### Exemplos de Rotas Disponíveis

| Rota                    | Método | Autentic. | Descrição                                             |
|-------------------------|---------|-----------|---------------------------------------------------------|
| `/auth/register`        | POST    | Não       | Registra uma nova UBS, requer `nomeUbs`, `cnes`, `address`, `password` |
| `/auth/login`           | POST    | Não       | Autentica e retorna um token JWT, requer `cnes` e `password` |
| `/swagger-ui/index.html`| GET     | Não       | Documentação interativa da API                          |
| `/v3/api-docs`          | GET     | Não       | Documentação OpenAPI em JSON                            |
| `/h2-console`           | GET     | Não       | Console H2 (apenas para dev/test)                         |
| `/private/data`         | GET     | Sim       | Exige token JWT, retorna 401 se não autenticado ou 404 se não existe. |

Acima temos apenas um exemplo de rota protegida (`/private/data`).
