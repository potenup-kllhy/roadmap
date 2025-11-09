package com.kllhy.roadmap.common.exception;

public interface IErrorCode {
    DomainHttpStatus getHttpStatus();

    String getCode();

    String getMessage();
}
