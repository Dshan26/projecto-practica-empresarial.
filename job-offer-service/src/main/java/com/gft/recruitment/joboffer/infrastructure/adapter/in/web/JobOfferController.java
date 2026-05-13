package com.gft.recruitment.joboffer.infrastructure.adapter.in.web;

import com.gft.recruitment.joboffer.application.dto.CreateJobOfferCommand;
import com.gft.recruitment.joboffer.application.dto.JobOfferResponse;
import com.gft.recruitment.joboffer.application.dto.UpdateJobOfferCommand;
import com.gft.recruitment.joboffer.domain.port.in.CreateJobOfferUseCase;
import com.gft.recruitment.joboffer.domain.port.in.DeleteJobOfferUseCase;
import com.gft.recruitment.joboffer.domain.port.in.QueryJobOfferUseCase;
import com.gft.recruitment.joboffer.domain.port.in.UpdateJobOfferUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/job-offers")
public class JobOfferController {

    private final CreateJobOfferUseCase createJobOfferUseCase;
    private final QueryJobOfferUseCase queryJobOfferUseCase;
    private final UpdateJobOfferUseCase updateJobOfferUseCase;
    private final DeleteJobOfferUseCase deleteJobOfferUseCase;

    public JobOfferController(CreateJobOfferUseCase createJobOfferUseCase,
                              QueryJobOfferUseCase queryJobOfferUseCase,
                              UpdateJobOfferUseCase updateJobOfferUseCase,
                              DeleteJobOfferUseCase deleteJobOfferUseCase) {
        this.createJobOfferUseCase = createJobOfferUseCase;
        this.queryJobOfferUseCase = queryJobOfferUseCase;
        this.updateJobOfferUseCase = updateJobOfferUseCase;
        this.deleteJobOfferUseCase = deleteJobOfferUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<JobOfferResponse> create(@RequestBody CreateJobOfferCommand command,
                                         @RequestHeader("X-User-Id") UUID recruiterId) {
        return createJobOfferUseCase.create(command, recruiterId);
    }

    @GetMapping
    public Flux<JobOfferResponse> findAll() {
        return queryJobOfferUseCase.findAll();
    }

    @GetMapping("/{id}")
    public Mono<JobOfferResponse> findById(@PathVariable UUID id) {
        return queryJobOfferUseCase.findById(id);
    }

    @PutMapping("/{id}")
    public Mono<JobOfferResponse> update(@PathVariable UUID id,
                                         @RequestBody UpdateJobOfferCommand command) {
        return updateJobOfferUseCase.update(id, command);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable UUID id) {
        return deleteJobOfferUseCase.delete(id);
    }
}
