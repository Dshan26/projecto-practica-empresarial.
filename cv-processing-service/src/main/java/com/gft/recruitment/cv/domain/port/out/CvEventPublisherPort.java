package com.gft.recruitment.cv.domain.port.out;

import com.gft.recruitment.events.CvUploadedEvent;
import reactor.core.publisher.Mono;

public interface CvEventPublisherPort {
    Mono<Void> publishCvUploaded(CvUploadedEvent event);
}
