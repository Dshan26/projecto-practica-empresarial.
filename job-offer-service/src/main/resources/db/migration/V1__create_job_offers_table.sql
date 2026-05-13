CREATE TABLE job_offers (
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre_cargo            VARCHAR(255) NOT NULL,
    descripcion             TEXT,
    experiencia_minima_anios INT NOT NULL DEFAULT 0,
    seniority_esperado      VARCHAR(20) NOT NULL CHECK (seniority_esperado IN ('JUNIOR', 'SEMI_SENIOR', 'SENIOR')),
    recruiter_id            UUID NOT NULL,
    created_at              TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMP NOT NULL DEFAULT NOW()
);
