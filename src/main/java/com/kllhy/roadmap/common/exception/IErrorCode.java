package com.kllhy.roadmap.common.exception;

public interface IErrorCode {
    int getHttpStatus();

    String getCode();

    String getMessage();
}
