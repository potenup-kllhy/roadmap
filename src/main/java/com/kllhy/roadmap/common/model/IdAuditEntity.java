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

    @LastModifiedDate protected Timestamp modifiedAt;

    protected IdAuditEntity(Timestamp createdAt, Timestamp modifiedAt) {
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    protected IdAuditEntity() {
        super();
    }
}
