CREATE TABLE evaluation_results (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    candidate_id    UUID NOT NULL,
    job_offer_id    UUID NOT NULL,
    test_id         UUID NOT NULL,
    tech_score      INT NOT NULL CHECK (tech_score >= 0 AND tech_score <= 100),
    total_questions INT NOT NULL,
    correct_answers INT NOT NULL,
    evaluated_at    TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(candidate_id, job_offer_id)
);
