# 💰 Finance API — Projeto Final BECA Java JR

API REST desenvolvida em **Java + Spring Boot** para gerenciamento de transações financeiras, com **autenticação JWT**, **mensageria com Kafka**, **consumo de APIs externas**, **geração de relatórios** e **containerização com Docker**.

Este projeto foi desenvolvido como **Desafio Final da BECA Java JR (NTT DATA) 2025–2026**, seguindo boas práticas de **arquitetura em camadas**, **segurança**, **testes automatizados** e **documentação**.

---

## 📌 Funcionalidades

### 🔐 Autenticação e Usuários
- Cadastro de usuários
- Login com autenticação JWT
- Controle de acesso por roles (`USER`, `ADMIN`)
- Usuários acessam apenas seus próprios dados
- Importação de usuários via arquivo Excel

### 💳 Transações Financeiras
- Criação de transações de entrada e saída
- Validação de saldo (não permite gastar mais do que possui)
- Transferência entre usuários
- Listagem de transações por usuário

### 📊 Análises Financeiras
- Resumo de gastos por categoria
- Resumo de gastos por dia
- Resumo de gastos por mês
- Cálculo de saldo consolidado

### 🌎 Conversão de Moeda (API Pública)
- Consumo da **BrasilAPI**
- Conversão do valor de uma transação para outra moeda
- Exibição de:
  - valor original
  - moeda de origem
  - moeda destino
  - taxa de câmbio
  - valor convertido
  - data da cotação

### 🏦 Saldo Bancário (API Mock)
- Consumo de **API Mock externa**
- Exibição do saldo bancário do usuário
- Simulação de integração com sistema legado

### 📄 Relatórios
- Geração de relatório financeiro
- Download em **PDF** ou **Excel**
- Resumo das transações do usuário

### 📬 Mensageria com Kafka
- Publicação de eventos ao criar transações
- Consumer escutando eventos de transações criadas
- Arquitetura desacoplada (*fire-and-forget*)

### 📘 Documentação
- Swagger UI disponível
- Endpoints documentados automaticamente

---

## 🧱 Arquitetura do Projeto

```
controller  →  service  →  repository  →  database
                    ↓
                 kafka
                    ↓
              APIs externas
```

---

## 🛠️ Tecnologias Utilizadas
- Java 17
- Spring Boot 3
- Spring Security (JWT)
- Spring Data JPA
- PostgreSQL
- Apache Kafka
- Docker & Docker Compose
- Swagger (Springdoc OpenAPI)
- BrasilAPI
- MockAPI

---

## ▶️ Como Rodar o Projeto com Docker

```bash
docker-compose up --build
```

Acessos:
- API: http://localhost:8080
- Swagger: http://localhost:8080/swagger

---

## 👩‍💻 Autora
**Maíra Cristina Nascimento Assis**
