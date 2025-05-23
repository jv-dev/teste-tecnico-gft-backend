# 💼 Teste Técnico GFT - Backend

Este projeto consiste em uma API RESTful desenvolvida em Java com Spring Boot. A aplicação foi projetada para gerenciar usuários e seus respectivos endereços, com autenticação baseada em JWT, controle de acesso por perfis de usuário (USER e ADMIN) e integração com o serviço de CEP ViaCEP. Trata-se de um backend robusto e seguro, com foco em boas práticas, modularização e cobertura de testes.

---

## 🚀 Tecnologias utilizadas

- **Java 17**
- **Spring Boot 3.4.5**
- **Spring Security com JWT**
- **PostgreSQL**
- **Flyway (migrations de banco)**
- **JPA/Hibernate**
- **JUnit + MockMvc (testes)**

---

## ⚙️ Funcionalidades principais

- Autenticação com token JWT
- Cadastro e login de usuários
- Perfis de acesso: `USER` e `ADMIN`
- CRUD de endereços por usuário
- Admins podem visualizar/gerenciar qualquer usuário/endereço
- Integração com ViaCEP para autocompletar dados do endereço
- Tratamento global de exceções
- Paginação e ordenação por data de criação
- Swagger para documentação da API
- Migrations versionadas com Flyway

---

## ▶️ Como executar o projeto

### Pré-requisitos:

- JDK 17+
- PostgreSQL (banco local ou container)
- Maven 3.8+

### Etapas:

1. Clone o projeto:

```bash
git clone https://github.com/seu-usuario/teste-tecnico-gft-backend.git
cd teste-tecnico-gft-backend
```

2. Configure o banco de dados em `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/seu_banco
spring.datasource.username=postgres
spring.datasource.password=123456
```

3. Rode a aplicação:

```bash
./mvnw spring-boot:run
```

> As migrations serão executadas automaticamente.

4. Acesse a documentação:

- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 🔐 Segurança

- Registro: `POST /auth/register`
- Login: `POST /auth/login` (retorna o token JWT)
- Endpoints autenticados exigem o token no header `Authorization: Bearer <token>`
- Acesso a dados é controlado por permissões (usuário comum vs. admin)

---

## 🧪 Testes

Executar testes com:

```bash
./mvnw test
```

Testes cobrem:
- Autenticação
- Validação de regras de negócio
- Proteção de rotas
- Regras de permissão
- Integração com a base de dados (via H2)

---

## 📂 Estrutura de pacotes

- `controller/` – Camada de entrada (REST API)
- `service/` – Regras de negócio
- `repository/` – Interface com o banco
- `dto/` – Objetos de transferência de dados
- `security/` – Configuração de JWT e permissões
- `exception/` – Tratamento global de erros
- `model/` – Entidades JPA

---

## 📌 Observações

- Os campos `created_at` são gerados automaticamente pelo banco e usados para ordenação e controle.
- Toda a aplicação foi construída com foco em escalabilidade e boas práticas de desenvolvimento.

---

## Autor

Desenvolvido por **João Victor Cardoso de Souza**
