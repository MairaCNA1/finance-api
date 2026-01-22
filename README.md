# Finance API 💰

API REST desenvolvida em Java com Spring Boot para gerenciamento financeiro,
permitindo o cadastro de usuários, controle de transações, consulta de saldo bancário
e integração com serviços externos de câmbio.

Este projeto foi desenvolvido como **Desafio Final da BECA Java Jr (NTT DATA)**,
aplicando conceitos de arquitetura em camadas, boas práticas de desenvolvimento,
tratamento de erros, documentação e testes automatizados.

---

## 🚀 Funcionalidades

- Cadastro, listagem, busca e exclusão de usuários
- Importação de usuários via arquivo Excel
- Registro de transações financeiras
- Análise de despesas por categoria e por dia
- Consulta de saldo bancário via MockAPI
- Consulta de taxa de câmbio via BrasilAPI
- Endpoint de health check da aplicação

---

## 🏗️ Arquitetura

O projeto segue uma **arquitetura em camadas**, separando responsabilidades
e facilitando manutenção e testes:

Controller → Service → Repository


### Camadas:
- **Controller**: expõe os endpoints REST e retorna respostas padronizadas
- **Service**: contém a lógica de negócio da aplicação
- **Repository**: acesso ao banco de dados com Spring Data JPA
- **DTOs**: controle de entrada e saída de dados
- **Exception Handler**: tratamento global de erros

---

## 📦 Padronização de Respostas

Todas as respostas da API seguem um padrão único utilizando o objeto `ApiResponse`:

```json
{
  "status": 200,
  "message": "Descrição da operação",
  "data": {}
}

Campos:
- status: código HTTP

- message: mensagem descritiva

- data: payload da resposta

```
---


## ❗ Tratamento de Erros

A aplicação utiliza um GlobalExceptionHandler para capturar exceções e retornar
respostas padronizadas, garantindo:

- Uso correto dos códigos HTTP (400, 404, 500, etc.)

- Mensagens claras para o cliente

- Centralização do tratamento de erros

---

## 🌐 Integrações Externas

BrasilAPI

- Consulta de taxa de câmbio

MockAPI

- Simulação de saldo bancário de usuários

As URLs externas são configuradas via application.yml,
seguindo boas práticas de configuração.

---

## 📑 Documentação da API

A API é documentada automaticamente com Swagger (Springdoc OpenAPI).

Após subir a aplicação, acesse:

http://localhost:8080/swagger

---

## 🧪 Testes Automatizados

O projeto possui testes automatizados utilizando:

- JUnit 5

- Mockito

- MockMvc

Foram testadas:

- Camada de Service

- Camada de Controller

- Tratamento global de exceções

Para rodar os testes:

./mvnw test

---

## 🛠️ Tecnologias Utilizadas

- Java 17

- Spring Boot 3

- Spring Web

- Spring Data JPA

- PostgreSQL

- Docker / Docker Compose

- OpenAPI / Swagger (Springdoc)

- JUnit 5

- Mockito

- Apache POI (upload Excel)

- MockAPI

- BrasilAPI

---

## ▶️ Como Executar o Projeto
Pré-requisitos

Java 17+

Maven

PostgreSQL

Execução:

- ./mvnw clean

- ./mvnw spring-boot:run


A aplicação estará disponível em:
http://localhost:8080


## 👩‍💻 Autora

Projeto desenvolvido por Maíra
Desafio BECA Java Jr — NTT DATA





