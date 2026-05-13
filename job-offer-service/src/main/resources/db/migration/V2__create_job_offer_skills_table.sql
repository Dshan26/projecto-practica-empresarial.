CREATE TABLE job_offer_skills (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    job_offer_id UUID NOT NULL REFERENCES job_offers(id) ON DELETE CASCADE,
    skill_name   VARCHAR(255) NOT NULL
);
