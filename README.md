# API COMMERCE

[![NPM](https://img.shields.io/npm/l/react)](https://github.com/IsraelLima25/sec-piloto-api/blob/master/LICENSE)

# Sobre o projeto

Api restful que simula o fluxo de venda dos produtos 

http://localhost:8080/swagger-ui/index.html

```bash
# Credenciais armazenadas em memória
email: desafio@softplan.com
senha: 123456

# Usar o token retornado na requisição para fazer requisições protegidas.
```
# Tecnologias utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- Spring Security
- Swagger
- JPA / Hibernate
- H2 Database
- Maven

## Implantação em produção
- Amazon EC2

# Como executar o projeto no Host
Pré-requisitos: Maven

```bash
# Clonar repositório
git clone git@github.com:IsraelLima25/api-commerce.git

# Executar na pasta do projeto o comando
mvn spring-boot:run
```

# Como executar o projeto em ambiente Docker

Pré-requisitos: Docker

```bash
# Executar
docker run -d -p 8080:8080 ilimafilho/apicommerce:1.0.0

Obs: A imagem está armazenada no docker hub registry
```

# Autor

Israel Santos Lima Filho

https://www.linkedin.com/in/israelsantoslima