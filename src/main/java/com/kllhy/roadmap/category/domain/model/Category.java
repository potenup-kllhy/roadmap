package com.kllhy.roadmap.category.domain.model;

import com.kllhy.roadmap.category.domain.enums.Type;
import com.kllhy.roadmap.common.model.IdEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends IdEntity {

    @Enumerated(EnumType.STRING)
    private Type type;

    private String name;
}
