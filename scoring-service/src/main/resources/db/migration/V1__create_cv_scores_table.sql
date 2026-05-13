CREATE TABLE cv_scores (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    candidate_id    UUID NOT NULL,
    job_offer_id    UUID NOT NULL,
    cv_score        INT NOT NULL CHECK (cv_score >= 0 AND cv_score <= 100),
    score_details   JSONB,
    analysis_error  BOOLEAN NOT NULL DEFAULT FALSE,
    calculated_at   TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(candidate_id, job_offer_id)
);
