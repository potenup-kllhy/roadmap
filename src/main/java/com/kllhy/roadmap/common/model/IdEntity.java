package com.kllhy.roadmap.common.model;

import jakarta.persistence.*;
import java.util.Objects;
import lombok.Getter;

@MappedSuperclass
public abstract class IdEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    protected Long id;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof IdEntity that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
