package com.gft.recruitment.cv.domain.exception;

import java.util.UUID;

public class CvNotFoundException extends RuntimeException {

    public CvNotFoundException(UUID id) {
        super("CV with id '" + id + "' not found");
    }
}
