# API COMMERCE

[![NPM](https://img.shields.io/npm/l/react)](https://github.com/IsraelLima25/sec-piloto-api/blob/master/LICENSE)

# Sobre o projeto

Api Restful que simula o fluxo de venda de produtos. Esta API foi documentada com o swagger, segue link abaixo para acessar os documentos quando a API estiver em execução.

http://localhost:8080/swagger-ui/index.html

Nesta API os recursos de autenticação do Spring Security foram adicionados. Portanto para acessar a maioria dos recursos exceto a listagem de produtos, é necessário se autenticar. Para se autenticar basta acessar a URI /api/login e capturar um token JWT. Como o objetivo deste artefato é a didática, estamos fornecendo um usuário previamente cadastro em memória para capturar o token e executar os testes, segue as credenciais abaixo:

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
# Executar o comando
docker run -p 8080:8080 ilimafilho/apicommerce:1.0.0
```

Observação: No cenário de execução com docker ficar atento para a definição das portas host/container.

# Autor

Israel Santos Lima Filho

https://www.linkedin.com/in/israelsantoslima