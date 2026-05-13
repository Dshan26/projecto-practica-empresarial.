CREATE TABLE cv_metadata (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    candidate_id    UUID NOT NULL,
    job_offer_id    UUID NOT NULL,
    file_name       VARCHAR(255) NOT NULL,
    file_size       BIGINT NOT NULL,
    file_path       VARCHAR(512) NOT NULL,
    extracted_text  TEXT,
    status          VARCHAR(20) NOT NULL DEFAULT 'UPLOADED'
                    CHECK (status IN ('UPLOADED', 'PROCESSING', 'PROCESSED', 'ERROR')),
    error_message   TEXT,
    uploaded_at     TIMESTAMP NOT NULL DEFAULT NOW()
);
