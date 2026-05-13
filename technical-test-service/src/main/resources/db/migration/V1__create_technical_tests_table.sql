CREATE TABLE technical_tests (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    job_offer_id    UUID NOT NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW()
);
