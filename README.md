# SignaSafe: API de Assinatura Digital

![Java](https://img.shields.io/badge/Java-21-blue?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?style=for-the-badge&logo=spring)
![Docker](https://img.shields.io/badge/Docker-24.0-blue?style=for-the-badge&logo=docker)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-informational?style=for-the-badge&logo=postgresql)
[![Java CI with Maven](https://github.com/martinswilliam/Signasafe/actions/workflows/ci-pipeline.yml/badge.svg)](https://github.com/martinswilliam/Signasafe/actions)

## 📝 Descrição

**SignaSafe** é uma API RESTful desenvolvida como um projeto de estudo para demonstrar a implementação de um sistema de assinatura digital seguro e robusto. O projeto aborda conceitos de criptografia assimétrica, segurança de API com JWT, testes unitários, containerização com Docker e integração contínua com GitHub Actions.

## ✨ Funcionalidades

- **Autenticação de Usuários:** Cadastro e Login com retorno de token JWT.
- **Gerenciamento de Chaves:** Geração de pares de chaves (pública/privada) para cada usuário no momento do cadastro.
- **Upload de Documentos:** Envio de documentos para a plataforma.
- **Processo de Assinatura:** Um usuário pode assinar um documento usando sua chave privada.
- **Verificação de Assinatura:** Funcionalidade para verificar a validade de uma assinatura (a ser exposta em futuros endpoints).

## 🛠️ Tecnologias Utilizadas

- **Java 21** & **Spring Boot 3**: Spring Web, Spring Security, Spring Data JPA
- **PostgreSQL**: Banco de dados relacional.
- **Docker & Docker Compose**: Para containerização da aplicação e do banco de dados.
- **JUnit 5 & Mockito**: Para testes unitários.
- **Bouncy Castle**: Biblioteca de criptografia para geração de chaves e operações de assinatura.
- **JWT (JSON Web Tokens)**: Para controle de acesso stateless.
- **Maven**: Gerenciador de dependências.
- **GitHub Actions**: Para automação de CI/CD (build e teste).

## 📁 Estrutura do Projeto

```
.
├── .github/workflows/      # Workflows de CI/CD com GitHub Actions
├── src/
│   ├── main/
│   │   ├── java/           # Código fonte da aplicação
│   │   └── resources/      # Arquivos de configuração (application.properties)
│   └── test/
│       ├── java/           # Código fonte dos testes
│       └── resources/      # Arquivos de configuração para o ambiente de teste
├── .gitignore
├── docker-compose.yml      # Orquestração dos contêineres Docker
├── Dockerfile              # "Receita" para construir a imagem Docker da aplicação
└── pom.xml                 # Definições do projeto e dependências Maven
```

## 🚀 Como Executar o Projeto

### Pré-requisitos

- [JDK 21](https://www.oracle.com/java/technologies/downloads/#jdk21-windows)
- [Docker Desktop](https://www.docker.com/products/docker-desktop/)

### 🐳 Rodando com Docker (Método Recomendado)

Este é o método mais simples e garante que o ambiente funcione de forma idêntica em qualquer máquina.

1.  **Clone o repositório:**

    ```bash
    git clone [https://github.com/martinswilliam/Signasafe.git](https://github.com/martinswilliam/Signasafe.git)
    cd Signasafe
    ```

2.  **Suba o ambiente com Docker Compose:**
    Este único comando irá construir a imagem da sua aplicação e iniciar os contêineres da API e do banco de dados.
    ```bash
    docker-compose up --build
    ```
    A API estará disponível em `http://localhost:8080`.

### 🔧 Rodando pela IDE (Para Desenvolvimento e Debug)

Use este método quando quiser usar os recursos de debug da sua IDE (como breakpoints).

1.  **Clone o repositório** e abra-o na sua IDE (IntelliJ, VS Code, etc).

2.  **Inicie apenas o banco de dados** com Docker:

    ```bash
    docker-compose up -d db
    ```

3.  **Crie o arquivo de configuração local:**
    Na pasta `src/main/resources/`, crie o arquivo `application-local.properties` com o seguinte conteúdo:

    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/signasafe
    spring.datasource.username=admin
    spring.datasource.password=admin
    api.security.token.secret=meu-secret-super-secreto-para-o-projeto-signasafe
    ```

4.  **Execute a aplicação Spring Boot** pela sua IDE, garantindo que o perfil `local` esteja ativo.

## 🔀 Endpoints da API

A URL base é `http://localhost:8080`.

| Método | Endpoint            | Descrição                                    | Autenticação? | Corpo da Requisição (Exemplo)                    |
| ------ | ------------------- | -------------------------------------------- | ------------- | ------------------------------------------------ |
| `POST` | `/auth/register`    | Registra um novo usuário.                    | Não           | `{"email": "user@email.com", "password": "123"}` |
| `POST` | `/auth/login`       | Autentica um usuário e retorna um token JWT. | Não           | `{"email": "user@email.com", "password": "123"}` |
| `POST` | `/documents/upload` | Faz o upload de um documento (form-data).    | **Sim (JWT)** | Chave: `file`, Valor: (o arquivo a ser enviado)  |
| `POST` | `/signatures/sign`  | Assina um documento existente.               | **Sim (JWT)** | `{"documentId": "0e1c2b3a-..."}`                 |

## 🔒 Questões de Segurança

- **Gerenciamento de Segredos:** Dados sensíveis são gerenciados via variáveis de ambiente no Docker Compose, evitando que sejam expostos no código-fonte. O arquivo `.gitignore` previne o envio de arquivos de configuração locais.
- **Autenticação:** Todas as rotas, exceto `/auth/**`, são protegidas e exigem um token JWT válido no cabeçalho `Authorization: Bearer <token>`.
- **Gerenciamento de Chaves:** Este projeto, para fins didáticos, gera e armazena o par de chaves no servidor. Em um sistema de produção real, a chave privada **jamais** deveria ser transmitida ou armazenada no servidor.

## 🧪 Testes

O projeto possui uma suíte de testes unitários configurada com um banco de dados em memória (H2), garantindo que os testes sejam independentes do ambiente. Para rodar os testes localmente:

```bash
./mvnw test
```

## 🔄 CI/CD

Um workflow de Integração Contínua está configurado usando **GitHub Actions**. A cada `push` ou `pull request` para a branch `main`, o workflow executa `mvn clean install` para compilar o código e rodar todos os testes automatizados, garantindo a integridade do projeto.
