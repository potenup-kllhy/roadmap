package com.kllhy.roadmap.travel.domain.exception;

import com.kllhy.roadmap.common.exception.IErrorCode;

public enum TravelErrorCode implements IErrorCode {

    TRAVEL_NOT_FOUND(404, "NOT_FOUND", "not found travel"),
    ;

    private final int httpStatus;
    private final String code;
    private final String message;

    TravelErrorCode(int httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    @Override
    public int getHttpStatus() {
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
