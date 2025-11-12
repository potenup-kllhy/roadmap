package com.kllhy.roadmap.star.roadmap.domain.exception;

import com.kllhy.roadmap.common.exception.DomainHttpStatus;
import com.kllhy.roadmap.common.exception.IErrorCode;

public enum StarRoadMapErrorCode implements IErrorCode {
    STAR_ROAD_MAP_INVALID_VALUE(DomainHttpStatus.BAD_REQUEST, "SRM-001", "Star RoadMap value is invalid. (range: 0 ~ 5)");

    private final DomainHttpStatus httpStatus;
    private final String code;
    private final String message;

    StarRoadMapErrorCode(final DomainHttpStatus httpStatus, final String code, final String message) {
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
