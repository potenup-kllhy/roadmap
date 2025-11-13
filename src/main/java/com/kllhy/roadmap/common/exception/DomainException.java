package com.kllhy.roadmap.common.exception;

import lombok.Getter;

public class DomainException extends RuntimeException {

    @Getter private final IErrorCode errorCode;

    public DomainException(IErrorCode errorCode) {
        super(errorCode != null ? errorCode.getMessage() : null);
        this.errorCode = errorCode;
    }
}
