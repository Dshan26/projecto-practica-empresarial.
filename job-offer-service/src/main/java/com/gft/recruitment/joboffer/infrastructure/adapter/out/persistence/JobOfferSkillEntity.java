package com.gft.recruitment.joboffer.infrastructure.adapter.out.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("job_offer_skills")
public class JobOfferSkillEntity implements Persistable<UUID> {

    @Id
    private UUID id;

    @Column("job_offer_id")
    private UUID jobOfferId;

    @Column("skill_name")
    private String skillName;

    @Transient
    private boolean isNew = true;

    public JobOfferSkillEntity() {
    }

    public JobOfferSkillEntity(UUID id, UUID jobOfferId, String skillName) {
        this.id = id;
        this.jobOfferId = jobOfferId;
        this.skillName = skillName;
    }

    @Override
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getJobOfferId() { return jobOfferId; }
    public void setJobOfferId(UUID jobOfferId) { this.jobOfferId = jobOfferId; }

    public String getSkillName() { return skillName; }
    public void setSkillName(String skillName) { this.skillName = skillName; }

    @Override
    @Transient
    public boolean isNew() { return isNew; }
    public void setNew(boolean isNew) { this.isNew = isNew; }
}
