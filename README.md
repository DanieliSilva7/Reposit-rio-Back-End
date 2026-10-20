# Up Finances API (Trabalho II: persistência com PostgreSQL)

API REST de organização financeira pessoal, em Java + Spring Boot. Continuação do Trabalho I: os dados agora são salvos no **PostgreSQL** (Spring Data JPA) em vez de uma lista em memória.

## Tecnologias

- Java 17, Spring Boot 3.3.2
- Spring Web, Spring Data JPA, PostgreSQL Driver

## Modelo (entidade JPA `Transacao`)

| Campo | Tipo | Observação |
|---|---|---|
| id | Long | gerado pelo banco (`@GeneratedValue`); não vai no JSON de cadastro |
| descricao | String | |
| valor | BigDecimal | |
| tipo | String | `RECEITA` ou `DESPESA` |
| categoria | String | ex.: Alimentação, Transporte, Salário |
| data | LocalDate | formato `YYYY-MM-DD` |

## Como rodar

1. Crie o banco no PostgreSQL:
   ```sql
   CREATE DATABASE upfinances;
   ```
2. Ajuste usuário e senha em `src/main/resources/application.properties`.
3. Suba a aplicação:
   ```bash
   mvn spring-boot:run
   ```
   A tabela `transacao` é criada automaticamente (`spring.jpa.hibernate.ddl-auto=update`).

A API sobe em `http://localhost:8080`.

## Endpoints

Base URL: `http://localhost:8080/api/transacoes`

| Método | Rota | Descrição |
|---|---|---|
| GET | `/api/transacoes` | Lista todas (ou filtradas) |
| GET | `/api/transacoes/{id}` | Busca pelo id |
| POST | `/api/transacoes` | Cadastra (id gerado pelo banco) |
| PUT | `/api/transacoes/{id}` | Atualiza |
| DELETE | `/api/transacoes/{id}` | Exclui |

### Filtros combináveis (todos opcionais)

| Parâmetro | Exemplo |
|---|---|
| `tipo` | `?tipo=DESPESA` |
| `categoria` | `?categoria=Alimentação` |
| `descricao` (busca parcial) | `?descricao=mercado` |
| `valorMin` | `?valorMin=50` |
| `valorMax` | `?valorMax=500` |

Exemplo combinando três filtros:
```
GET /api/transacoes?tipo=DESPESA&categoria=Alimentação&valorMax=200
```

## Exemplo de JSON para POST (sem id)

```json
{
  "descricao": "Compra no mercado",
  "valor": 185.50,
  "tipo": "DESPESA",
  "categoria": "Alimentação",
  "data": "2026-09-20"
}
```

## Demonstração de persistência

1. `POST /api/transacoes` no Postman e anotar o `id` retornado.
2. Parar e subir a aplicação novamente.
3. `GET /api/transacoes/{id}`: o registro continua disponível.
4. No pgAdmin/psql: `SELECT * FROM transacao;` mostra o mesmo registro.
