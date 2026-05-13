package com.gft.recruitment.test.domain.exception;

public class TestAlreadySubmittedException extends RuntimeException {

    public TestAlreadySubmittedException(String message) {
        super(message);
    }
}
