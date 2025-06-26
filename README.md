# SignaSafe: API de Assinatura Digital

![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.1-brightgreen)
![Docker](https://img.shields.io/badge/Docker-blue)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-informational)
![CI/CD](https://github.com/SEU-USUARIO/SEU-REPOSITORIO/actions/workflows/ci.yml/badge.svg)

## 📝 Descrição

**SignaSafe** é uma API RESTful desenvolvida como um projeto de estudo para demonstrar a implementação de um sistema de assinatura digital seguro e robusto. O projeto aborda conceitos de criptografia assimétrica, segurança de API com JWT, testes unitários, containerização com Docker e integração contínua com GitHub Actions.

## ✨ Funcionalidades

- **Autenticação de Usuários:** Cadastro e Login com retorno de token JWT.
- **Gerenciamento de Chaves:** Geração de pares de chaves (pública/privada) para cada usuário no momento do cadastro.
- **Upload de Documentos:** Envio de documentos para a plataforma.
- **Processo de Assinatura:** Um usuário pode assinar um documento usando sua chave privada.
- **Verificação de Assinatura:** Qualquer pessoa pode verificar a validade de uma assinatura usando a chave pública do signatário.

## 🛠️ Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3**: Spring Web, Spring Security, Spring Data JPA
- **PostgreSQL**: Banco de dados relacional.
- **Docker & Docker Compose**: Para containerização da aplicação e do banco de dados, garantindo um ambiente de desenvolvimento consistente.
- **JUnit 5 & Mockito**: Para testes unitários e de integração.
- **Bouncy Castle**: Biblioteca de criptografia para geração de chaves e operações de assinatura.
- **JWT (JSON Web Tokens)**: Para controle de acesso e autenticação stateless.
- **Maven**: Gerenciador de dependências.
- **GitHub Actions**: Para automação de CI/CD (build e teste).

## 🚀 Como Executar o Projeto Localmente

### Pré-requisitos

- [JDK 21](https://www.oracle.com/java/technologies/downloads/#jdk21-windows)
- [Docker](https://www.docker.com/products/docker-desktop/)
- [Maven](https://maven.apache.org/download.cgi)
- Um cliente de API como [Postman](https://www.postman.com/) ou [Insomnia](https://insomnia.rest/).

### Passos

1.  **Clone o repositório:**

    ```bash
    git clone [https://github.com/SEU-USUARIO/SEU-REPOSITORIO.git](https://github.com/SEU-USUARIO/SEU-REPOSITORIO.git)
    cd SEU-REPOSITORIO
    ```

2.  **Crie o arquivo de configuração local:**
    Na pasta `src/main/resources/`, crie uma cópia do arquivo `application.properties` e renomeie-a para `application-local.properties`. Este arquivo não é rastreado pelo Git e conterá suas variáveis locais.

3.  **Suba o ambiente com Docker Compose:**
    Este comando irá iniciar um contêiner PostgreSQL com as configurações definidas em `docker-compose.yml`.

    ```bash
    docker-compose up -d
    ```

    O `-d` executa os contêineres em modo "detached" (em segundo plano).

4.  **Execute a aplicação Spring Boot:**
    Use sua IDE para executar a classe principal `SignasafeApplication` ou via Maven:

    ```bash
    mvn spring-boot:run -Dspring-boot.run.profiles=local
    ```

5.  A API estará disponível em `http://localhost:8080`.

## 🔒 Questões de Segurança

- **Gerenciamento de Segredos:** Dados sensíveis como senhas de banco de dados são gerenciados através de perfis (`application-local.properties`) e não são enviados para o repositório Git, conforme definido no arquivo `.gitignore`. Em produção, esses valores devem ser injetados como variáveis de ambiente.
- **Autenticação:** Todas as rotas, exceto `/auth/register` e `/auth/login`, são protegidas e exigem um token JWT válido no cabeçalho `Authorization`.
- **Gerenciamento de Chaves:** Este projeto, para fins didáticos, gera o par de chaves no servidor. Em um sistema de produção real, a chave privada **jamais** deveria ser transmitida ou armazenada no servidor. A geração e o uso da chave privada deveriam ocorrer inteiramente no lado do cliente (client-side).

## 🧪 Testes

O projeto possui uma suíte de testes unitários para garantir a qualidade e o funcionamento correto das lógicas de negócio e serviços. Para rodar os testes:

```bash
mvn test
```

## 🔄 CI/CD

Um workflow de Integração Contínua está configurado usando **GitHub Actions** (`.github/workflows/ci.yml`). A cada `push` ou `pull request` para a branch `main`, o workflow é acionado para:

1.  Fazer o checkout do código.
2.  Configurar o JDK 21.
3.  Executar `mvn clean install`, que compila o código e roda todos os testes automatizados.
