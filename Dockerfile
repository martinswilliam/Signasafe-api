# Estágio 1: Build (Construção) - Usa uma imagem com JDK completo para compilar o projeto.
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia primeiro o pom.xml para aproveitar o cache de camadas do Docker.
# Se o pom.xml não mudar, o Docker reutiliza as dependências já baixadas.
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o resto do código-fonte
COPY src ./src

# Executa o build do Maven, que compila e roda os testes.
# O -DskipTests pode ser removido se você quiser que os testes rodem durante o build da imagem.
RUN mvn clean package -DskipTests


# Estágio 2: Run (Execução) - Usa uma imagem muito menor, apenas com o Java Runtime (JRE).
FROM eclipse-temurin:21-jre-alpine

# Define o diretório de trabalho
WORKDIR /app

# Copia o arquivo .jar gerado no estágio de build para o nosso contêiner final.
COPY --from=builder /app/target/*.jar app.jar

# Expõe a porta que a aplicação Spring Boot usa
EXPOSE 8080

# Comando para iniciar a aplicação quando o contêiner for executado
ENTRYPOINT ["java","-jar","app.jar"]