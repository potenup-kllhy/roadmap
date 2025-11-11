package com.kllhy.roadmap.category.domain.enums;

import com.kllhy.roadmap.category.domain.exception.CategoryErrorCode;
import com.kllhy.roadmap.common.exception.DomainException;

public enum Type {
    ROLE,
    SKILL;

    public static Type from(String type) {
        for (Type t : Type.values()) {
            if (t.name().equalsIgnoreCase(type)) {
                return t;
            }
        }
        throw new DomainException(CategoryErrorCode.CATEGORY_TYPE_INVALID);
    }
}
