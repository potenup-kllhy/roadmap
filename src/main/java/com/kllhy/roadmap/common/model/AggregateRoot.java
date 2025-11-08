package com.kllhy.roadmap.common.model;

import com.kllhy.roadmap.common.event.DomainEvent;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import lombok.Getter;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@MappedSuperclass
public abstract class AggregateRoot extends IdAuditEntity {

    @Getter
    @Version
    protected Long version;

    @Transient
    protected final List<DomainEvent> domainEvents = new ArrayList<>();

    protected AggregateRoot() {
        super();
        this.version = 0L;
    }

    protected AggregateRoot(Long version) {
        this.version = version != null ? version : 0L;
    }

    protected void addDomainEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }

    @DomainEvents
    protected Collection<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    @AfterDomainEventPublication
    protected void clearDomainEvents() {
        this.domainEvents.clear();
    }

}
