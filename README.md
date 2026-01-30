# Finance API

API REST desenvolvida em **Spring Boot** para gerenciamento financeiro pessoal, com autenticação JWT, controle de acesso por papéis (USER / ADMIN), integração com **Kafka**, banco de dados **PostgreSQL** e execução via **Docker**.

Este projeto foi desenvolvido com foco em **boas práticas**, **segurança**, **arquitetura limpa** e **facilidade de demonstração em avaliação técnica**.

---

## 🧠 Visão Geral

A Finance API permite:

- Cadastro e autenticação de usuários
- Controle de acesso baseado em roles (USER e ADMIN)
- Criação e consulta de transações financeiras
- Transferências entre usuários
- Geração de relatórios (Excel)
- Upload em massa de usuários via CSV (ADMIN)
- Publicação e consumo de eventos com Kafka
- Integração com API externa de câmbio

---

## 🏗️ Arquitetura

- **Backend:** Java 17 + Spring Boot
- **Banco de Dados:** PostgreSQL
- **Mensageria:** Apache Kafka
- **Autenticação:** JWT (Stateless)
- **Documentação:** Swagger / OpenAPI
- **Containerização:** Docker + Docker Compose

Arquitetura em camadas:
- Controller
- Service
- Repository
- Config
- Security
- Kafka (Producer / Consumer)

---

## 🔐 Segurança

- Autenticação via **JWT**
- Autorização por roles:
  - `USER`: operações financeiras próprias
  - `ADMIN`: gestão de usuários e uploads
- Filtros customizados com `OncePerRequestFilter`
- Segurança por método com `@PreAuthorize`

### Regras principais:
- `POST /users` → público (cadastro)
- `POST /auth/login` → público
- `POST /users/upload` → apenas ADMIN
- Demais endpoints → autenticados

---

## 🚀 Como executar o projeto

### Pré-requisitos

- Docker
- Docker Compose
- Git

### Passo a passo

```bash
git clone <repositorio>
cd finance-api
docker-compose up --build
```

A API ficará disponível em:
```
http://localhost:8080
```

Swagger:
```
http://localhost:8080/swagger-ui.html
```

---

## 🧪 Fluxo de Testes (para apresentação)

### 1️⃣ Fluxo USER

1. Criar usuário  
   `POST /users`

2. Login  
   `POST /auth/login`

3. Criar transação  
   `POST /transactions`

4. Consultar saldo  
   `GET /balance/{userId}`

5. Gerar relatório  
   `GET /transactions/report/{userId}`

---

### 2️⃣ Fluxo ADMIN

1. Criar usuário ADMIN diretamente no banco (PostgreSQL)
2. Login como ADMIN
3. Upload em massa de usuários  
   `POST /users/upload`

Arquivo CSV exemplo:
```
doc/users_100_utf8.csv
```

---

## 📂 Upload de Usuários (CSV)

Formato esperado:

```csv
name,email,password
Maria,maria@email.com,123456
João,joao@email.com,123456
```

- Primeira linha é ignorada (header)
- Usuários duplicados são contabilizados como falha
- Retorno com total, sucesso e falhas

---

## 📊 Kafka

### Evento publicado
- `transaction.created`

### Quando ocorre?
- Sempre que uma transação é criada

### Finalidade
- Demonstração de arquitetura orientada a eventos
- Desacoplamento da regra de negócio



---

## 🧾 Status HTTP importantes

- `200` → sucesso
- `201` → criado com sucesso
- `204` → operação realizada sem retorno (ex: DELETE)
- `400` → erro de validação/regra de negócio
- `401` → não autenticado
- `403` → sem permissão
- `500` → erro interno tratado globalmente

---

## 📌 Observações para avaliadores

- Projeto executa 100% em Docker
- Banco é recriado ao subir os containers
- CSV incluso para facilitar carga inicial
- Código organizado, comentado e modular
- Foco em clareza de fluxo e segurança

---

## 👩‍💻 Autora

Projeto desenvolvido por **Maíra Cristina Nascimento Assis**  
Área: Desenvolvimento Backend / Java

---

