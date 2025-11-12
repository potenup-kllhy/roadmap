package com.kllhy.roadmap.roadmap.domain.exception;

import com.kllhy.roadmap.common.exception.DomainHttpStatus;
import com.kllhy.roadmap.common.exception.IErrorCode;

public enum RoadMapIErrorCode implements IErrorCode {
    ROAD_MAP_SUCCESS(DomainHttpStatus.SUCCESS, "ROAD_MAP_001", "200 success"),
    ROAD_MAP_BAD_REQUEST(DomainHttpStatus.BAD_REQUEST, "ROAD_MAP_002", "400 bad request"),
    ROAD_MAP_INTERNAL_SERVER_ERROR(
            DomainHttpStatus.INTERNAL_SERVER_ERROR, "ROAD_MAP_003", "500 internal server error"),
    ROAD_MAP_NOT_FOUND(DomainHttpStatus.NOT_FOUND, "ROAD_MAP_004", "404 not found"),
    ;

    private final DomainHttpStatus httpStatus;
    private final String code;
    private final String message;

    RoadMapIErrorCode(DomainHttpStatus httpStatus, String code, String message) {
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
