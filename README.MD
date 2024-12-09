# Documentação Central dos Microserviços MedMap

## Índice

1. [Introdução](#introdução)
2. [Links para Documentação Swagger](#links-para-documentação-swagger)
3. [Visão Geral dos Microserviços](#visão-geral-dos-microserviços)
    - [Microserviço de Registro e Autenticação de UBS](#microserviço-de-registro-e-autenticação-de-ubs)
    - [Microserviço de Gerenciamento de Medicamento](#microserviço-de-gerenciamento-de-medicamento)
    - [Microserviço de Consulta de Usuários](#microserviço-de-consulta-de-usuários)
4. [Tecnologias e Ferramentas](#tecnologias-e-ferramentas)
5. [Comunicação Entre Microserviços](#comunicação-entre-microserviços)
6. [Autenticação e Segurança](#autenticação-e-segurança)
7. [Rotas dos Microserviços](#rotas-dos-microserviços)
    - [Microserviço de Registro e Autenticação de UBS](#microserviço-de-registro-e-autenticação-de-ubs-rotas)
    - [Microserviço de Gerenciamento de Medicamento](#microserviço-de-gerenciamento-de-medicamento-rotas)
    - [Microserviço de Consulta de Usuários](#microserviço-de-consulta-de-usuários-rotas)
8. [Testes e Cobertura de Código](#testes-e-cobertura-de-código)
    - [Resumo da Cobertura de Testes](#resumo-da-cobertura-de-testes)
9. [Docker e Implantação](#docker-e-implantação)
    - [Papéis dos Dockerfiles](#papéis-dos-dockerfiles)
    - [Pipeline de CI/CD](#pipeline-de-cicd)
    - [Deploy no Render e Supabase](#deploy-no-render-e-supabase)
10. [Conclusão](#conclusão)

---

## Introdução

Esta documentação centraliza informações sobre os três microserviços que compõem a aplicação **MedMap**. Cada microserviço foi desenvolvido utilizando **Java** com **Spring Boot**, implementando funcionalidades específicas, garantindo alta qualidade por meio de testes unitários, e integrando-se de forma segura com o banco de dados gerenciado pelo **Supabase**. A seguir, detalhamos cada microserviço, suas interações, rotas, segurança, e processos de implantação.

## Links para Documentação Swagger

Acesse a documentação interativa de cada microserviço através dos links abaixo:

- **Medicamento Microserviço**: [Swagger UI - Medicamento](https://medicamento-microservico.onrender.com/swagger-ui/index.html#/medicamento)
- **Auth Service**: [Swagger UI - Auth Service](https://auth-service-yyj4.onrender.com/swagger-ui/index.html#/)
- **Usuário Microserviço**: [Swagger UI - Usuário](https://usuario-microservico.onrender.com/swagger-ui/index.html#/)

## Visão Geral dos Microserviços

### Microserviço de Registro e Autenticação de UBS

Responsável pelo registro e autenticação das **UBS (Unidades Básicas de Saúde)** no sistema MedMap. Este microserviço permite que as UBS se registrem no sistema e obtenham tokens de autenticação para acessar funcionalidades protegidas.

### Microserviço de Gerenciamento de Medicamento

Gerencia o **CRUD (Create, Read, Update, Delete)** das entidades **Medicamento**. Este microserviço permite a criação, listagem, atualização e deleção de medicamentos no sistema.

### Microserviço de Consulta de Usuários

Facilita a consulta de informações sobre **Usuários** e **Medicamentos**. Este microserviço se comunica com o microserviço de Medicamento para recuperar dados relevantes.

## Tecnologias e Ferramentas

| **Tecnologia/Ferramenta** | **Descrição**                                                   | **Microserviço**                    |
|---------------------------|-----------------------------------------------------------------|-------------------------------------|
| **Java**                  | Linguagem de programação principal.                             | Todos                               |
| **Spring Boot**           | Framework para desenvolvimento dos microserviços.               | Todos                               |
| **JUnit & Mockito**       | Frameworks para testes unitários.                              | Todos                               |
| **Swagger/OpenAPI**       | Documentação interativa das APIs.                               | Todos                               |
| **Spring Security & JWT** | Implementação de autenticação e segurança.                      | Auth Service, Medicamento           |
| **Supabase**              | Gerenciamento de banco de dados PostgreSQL em nuvem.            | Todos                               |
| **Docker**                | Containerização dos microserviços.                              | Todos                               |
| **GitHub Actions**        | Implementação da pipeline de CI/CD.                             | Todos                               |

## Comunicação Entre Microserviços

Os microserviços se comunicam principalmente através de **APIs RESTful**. O **Microserviço de Consulta de Usuários** utiliza o **Feign Client** para interagir com o **Microserviço de Gerenciamento de Medicamento**, permitindo chamadas HTTP simplificadas e eficientes.

## Autenticação e Segurança

A segurança dos microserviços é garantida por meio de **Spring Security** e **JSON Web Tokens (JWT)**. O fluxo de autenticação funciona da seguinte maneira:

1. **Registro e Login**:
    - As UBS registram-se no **Auth Service** fornecendo suas credenciais.
    - Após o registro, as UBS podem realizar login para obter um token JWT.

2. **Validação de Token**:
    - Para acessar rotas protegidas, os microserviços verificam a presença e a validade do token JWT no cabeçalho `Authorization`.
    - Tokens inválidos ou expirados resultam em respostas de erro 401 Unauthorized.

3. **Controle de Acesso**:
    - Rotas sensíveis exigem autenticação via JWT.
    - Rotas públicas são acessíveis sem necessidade de autenticação.

## Rotas dos Microserviços

### Microserviço de Registro e Autenticação de UBS Rotas

| **Endpoint**       | **Método HTTP** | **Autenticado** | **Descrição**                                        |
|--------------------|-----------------|------------------|------------------------------------------------------|
| `/auth/register`   | `POST`          | Não              | Registra uma nova UBS no sistema.                    |
| `/auth/login`      | `POST`          | Não              | Autentica uma UBS e retorna um token JWT.            |

### Microserviço de Gerenciamento de Medicamento Rotas

| **Endpoint**           | **Método HTTP** | **Autenticado** | **Descrição**                                |
|------------------------|-----------------|------------------|----------------------------------------------|
| `/medicamento`         | `POST`          | Sim              | Cria um novo medicamento.                    |
| `/medicamento`         | `GET`           | Não              | Lista todos os medicamentos.                 |
| `/medicamento/{id}`    | `GET`           | Não              | Retorna um medicamento específico pelo ID.   |
| `/medicamento/{id}`    | `PUT`           | Sim              | Atualiza um medicamento existente pelo ID.   |
| `/medicamento/{id}`    | `DELETE`        | Sim              | Exclui um medicamento pelo ID.               |

### Microserviço de Consulta de Usuários Rotas

| **Endpoint**                | **Método HTTP** | **Autenticado** | **Descrição**                                  |
|-----------------------------|-----------------|------------------|------------------------------------------------|
| `/usuarios/medicamentos`    | `GET`           | Não              | Retorna a lista de todos os medicamentos.      |
| `/usuarios/medicamentos/{id}` | `GET`         | Não              | Retorna um medicamento específico pelo ID.     |

## Testes e Cobertura de Código

Todos os microserviços são cobertos por **testes unitários** utilizando **JUnit** e **Mockito**, garantindo uma cobertura mínima de **90%** para assegurar a qualidade e a confiabilidade do código.

### Resumo da Cobertura de Testes

| **Microserviço**                         | **Cobertura Total** | **Cobertura de Instruções** | **Cobertura de Branches** | **Missed Instructions** | **Missed Branches** | **Missed Lines** | **Classes Cobertas** |
|------------------------------------------|---------------------|------------------------------|----------------------------|-------------------------|---------------------|------------------|-----------------------|
| **Registro e Autenticação de UBS**       | 100%                | 98%                          | 89%                        | 5 de 546                | 4 de 38             | 7                | 7                     |
| **Gerenciamento de Medicamento**         | 100%                | 100%                         | 86%                        | 0 de 32                 | 0 de 0              | 0                | 7                     |
| **Consulta de Usuários (Medicamentos)**   | 100%                | 100%                         | n/a                        | 0 de 32                 | 0 de 0              | 0                | 3                     |
| **Total Geral**                          | 100%                | 98%                          | 86%                        | 5 de 546                | 4 de 38             | 7                | 7                     |

> **Nota**: A cobertura de testes foi gerada utilizando o **JaCoCo** e está disponível nos relatórios detalhados de cada microserviço.

## Docker e Implantação

### Papéis dos Dockerfiles

Cada microserviço possui um **Dockerfile** que define o processo de containerização, garantindo que as aplicações possam ser facilmente implantadas em qualquer ambiente suportado pelo Docker.

- **Fases Comuns**:
    1. **Build**: Utiliza uma imagem base com **Maven** para compilar o projeto e gerar o JAR.
    2. **Final**: Utiliza uma imagem base com **Java** para executar o JAR gerado.

- **Resumo dos Dockerfiles**:
    - **Build Phase**: Compila o código-fonte e gera o arquivo JAR.
    - **Runtime Phase**: Executa o JAR em um ambiente otimizado para produção.

### Pipeline de CI/CD

Os microserviços utilizam **GitHub Actions** para implementar pipelines de **CI/CD**, assegurando que cada commit ou pull request seja automaticamente testado e validado antes de ser integrado ao ambiente de produção.

- **Etapas da Pipeline**:
    1. **Checkout do Código**: Obtém o código-fonte do repositório.
    2. **Configuração do Ambiente**: Configura o JDK e as dependências do projeto.
    3. **Build e Testes**: Compila o projeto e executa os testes unitários.
    4. **Geração de Relatórios**: Cria relatórios de cobertura de testes.

### Deploy no Render e Supabase

Os microserviços são implantados na plataforma **Render**, que fornece infraestrutura escalável e confiável para hospedar aplicações containerizadas. O banco de dados **Supabase** gerencia os dados em um ambiente PostgreSQL seguro e escalável.

- **Configurações de Deploy**:
    - **Render**: Hospeda os containers Docker dos microserviços, facilitando o gerenciamento e a escalabilidade.
    - **Supabase**: Fornece a API PostgreSQL utilizada pelos microserviços para persistência de dados.

## Conclusão

Os três microserviços da aplicação **MedMap** foram desenvolvidos seguindo as melhores práticas de engenharia de software, garantindo segurança, performance e escalabilidade. A integração entre os microserviços é facilitada por APIs bem definidas e mecanismos de autenticação robustos, assegurando uma comunicação eficiente e segura. Com a implementação de testes unitários abrangentes e pipelines de CI/CD automatizadas, o sistema mantém alta qualidade e confiabilidade, pronto para atender às necessidades de gerenciamento de UBS e medicamentos de forma eficaz.

---