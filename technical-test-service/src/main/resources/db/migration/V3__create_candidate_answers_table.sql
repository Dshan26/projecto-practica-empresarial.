CREATE TABLE candidate_answers (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    test_id         UUID NOT NULL REFERENCES technical_tests(id),
    candidate_id    UUID NOT NULL,
    job_offer_id    UUID NOT NULL,
    question_id     UUID NOT NULL REFERENCES test_questions(id),
    respuesta       TEXT NOT NULL,
    submitted_at    TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE(test_id, candidate_id, question_id)
);
