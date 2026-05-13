package com.gft.recruitment.events;

/**
 * Constants for Kafka topic names used across microservices.
 */
public final class KafkaTopics {

    private KafkaTopics() {
        // Utility class
    }

    public static final String CV_UPLOADED = "CV_UPLOADED";
    public static final String TEST_COMPLETED = "TEST_COMPLETED";
    public static final String SCORES_READY = "SCORES_READY";
}
