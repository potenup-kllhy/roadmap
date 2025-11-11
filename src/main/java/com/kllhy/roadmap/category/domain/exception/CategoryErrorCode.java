package com.kllhy.roadmap.category.domain.exception;

import com.kllhy.roadmap.common.exception.DomainHttpStatus;
import com.kllhy.roadmap.common.exception.IErrorCode;

public enum CategoryErrorCode implements IErrorCode {
    CATEGORY_NOT_FOUND(DomainHttpStatus.NOT_FOUND, "CATEGORY_001", "Category not found"),
    CATEGORY_TYPE_INVALID(DomainHttpStatus.BAD_REQUEST, "CATEGORY_002", "Invalid category type");

    private final DomainHttpStatus httpStatus;
    private final String code;
    private final String message;

    CategoryErrorCode(DomainHttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    @Override
    public DomainHttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
