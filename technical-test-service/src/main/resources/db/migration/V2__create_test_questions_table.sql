CREATE TABLE test_questions (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    test_id             UUID NOT NULL REFERENCES technical_tests(id) ON DELETE CASCADE,
    enunciado           TEXT NOT NULL,
    tipo                VARCHAR(20) NOT NULL CHECK (tipo IN ('MULTIPLE_CHOICE', 'OPEN_ENDED')),
    opciones            JSONB,
    respuesta_correcta  TEXT NOT NULL,
    orden               INT NOT NULL
);
