CREATE TABLE test_submissions (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    test_id         UUID NOT NULL REFERENCES technical_tests(id),
    candidate_id    UUID NOT NULL,
    job_offer_id    UUID NOT NULL,
    submitted_at    TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(candidate_id, job_offer_id)
);
