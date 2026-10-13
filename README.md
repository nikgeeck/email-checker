# Email Checker (вариант 17)

Проверка валидности Email: формат адреса и DNS MX-записи. Список проверенных контрагентов хранится в PostgreSQL.

## Архитектура (4 контейнера)

| Сервис | Технологии | Порт на хосте |
|--------|------------|---------------|
| `frontend` | nginx + HTML/CSS/JS | http://localhost:8080 |
| `backend` | Java 21 + Spring Boot | http://localhost:8081 |
| `web-service` | Python FastAPI (format + MX) | http://localhost:8082 |
| `db` | PostgreSQL 16 | внутренний `5432` |

### Кнопки на фронтенде

1. **Показать контрагентов** — выборка из БД через backend  
2. **Проверить email** — backend вызывает web-service, сохраняет результат в БД  

## Запуск

```bash
docker compose up --build -d
```

Откройте http://localhost:8080

Остановка:

```bash
docker compose down
```

С удалением данных БД (нужно после изменений `db/init.sql`, например unique-индекса):

```bash
docker compose down -v && docker compose up --build -d
```

## API (кратко)

- `GET /api/counterparties` — список контрагентов  
- `POST /api/counterparties/check` — `{ "name": "...", "email": "..." }`  
  - повтор того же email → `409` `{ "message": "..." }`  
  - невалидный email → `400`  
- `POST http://localhost:8082/validate` — прямая проверка web-service  
