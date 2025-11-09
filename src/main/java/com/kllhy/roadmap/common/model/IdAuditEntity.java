package com.kllhy.roadmap.common.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.sql.Timestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class IdAuditEntity extends IdEntity {

    @CreatedDate
    @Column(updatable = false)
    protected Timestamp createdAt;

    @LastModifiedDate protected Timestamp updatedAt;

    protected IdAuditEntity(Timestamp createdAt, Timestamp updatedAt) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    protected IdAuditEntity() {
        super();
    }
}
