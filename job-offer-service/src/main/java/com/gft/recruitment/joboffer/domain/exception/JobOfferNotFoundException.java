package com.gft.recruitment.joboffer.domain.exception;

import java.util.UUID;

public class JobOfferNotFoundException extends RuntimeException {

    public JobOfferNotFoundException(UUID id) {
        super("Job offer with id '" + id + "' not found");
    }
}
