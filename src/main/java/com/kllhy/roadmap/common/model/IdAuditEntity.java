package com.kllhy.roadmap.common.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class IdAuditEntity extends IdEntity {
    @CreatedDate
    @Column(updatable = false)
    protected LocalDateTime createdAt;


    @LastModifiedDate
    protected LocalDateTime updatedAt;

    protected IdAuditEntity(LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    protected IdAuditEntity() {
        super();
    }
}
