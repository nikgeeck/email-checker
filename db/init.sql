CREATE TABLE IF NOT EXISTS counterparties (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    email           VARCHAR(320) NOT NULL,
    format_valid    BOOLEAN NOT NULL,
    mx_valid        BOOLEAN NOT NULL,
    checked_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE UNIQUE INDEX IF NOT EXISTS uq_counterparties_email_lower
    ON counterparties (LOWER(email));

INSERT INTO counterparties (name, email, format_valid, mx_valid, checked_at) VALUES
    ('ООО Ромашка', 'info@gmail.com', TRUE, TRUE, NOW() - INTERVAL '2 days'),
    ('ИП Иванов', 'ivanov@example.invalid', TRUE, FALSE, NOW() - INTERVAL '1 day'),
    ('Тест Невалидный', 'bad@invalid.example', FALSE, FALSE, NOW() - INTERVAL '3 hours');
