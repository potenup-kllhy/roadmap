package com.kllhy.roadmap.category.domain.enums;

import com.kllhy.roadmap.category.domain.exception.CategoryErrorCode;
import com.kllhy.roadmap.common.exception.DomainException;

public enum CategoryType {
    ROLE,
    SKILL;

    public static CategoryType from(String type) {
        for (CategoryType t : CategoryType.values()) {
            if (t.name().equalsIgnoreCase(type)) {
                return t;
            }
        }
        throw new DomainException(CategoryErrorCode.CATEGORY_TYPE_INVALID);
    }
}
