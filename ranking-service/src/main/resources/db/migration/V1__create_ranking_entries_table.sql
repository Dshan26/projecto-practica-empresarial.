CREATE TABLE ranking_entries (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    candidate_id    UUID NOT NULL,
    job_offer_id    UUID NOT NULL,
    cv_score        INT NOT NULL CHECK (cv_score >= 0 AND cv_score <= 100),
    tech_score      INT NOT NULL CHECK (tech_score >= 0 AND tech_score <= 100),
    final_score     DECIMAL(5,2) NOT NULL CHECK (final_score >= 0 AND final_score <= 100),
    ranking_position INT,
    calculated_at   TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(candidate_id, job_offer_id)
);

-- Composite index for ranking queries ordered by final_score DESC, tiebreak by cv_score DESC
CREATE INDEX idx_ranking_job_offer_score ON ranking_entries(job_offer_id, final_score DESC, cv_score DESC);
