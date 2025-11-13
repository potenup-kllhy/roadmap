package com.kllhy.roadmap.star.roadmap.domain.exception;

import com.kllhy.roadmap.common.exception.DomainHttpStatus;
import com.kllhy.roadmap.common.exception.IErrorCode;

public enum StarRoadMapErrorCode implements IErrorCode {
    STAR_ROAD_MAP_INVALID_VALUE(
            DomainHttpStatus.BAD_REQUEST, "SRM-001", "Star RoadMap value is invalid. (range: 0 ~ 5)"),
    STAR_ROAD_MAP_NOT_FOUND(DomainHttpStatus.NOT_FOUND, "SRM-002", "Star RoadMap not found."),
    STAR_ROAD_MAP_NOT_AUTHORIZED(DomainHttpStatus.FORBIDDEN, "SRM-003", "Not authorized to access this Star RoadMap."),
    STAR_ROAD_MAP_ALREADY_EXISTS(DomainHttpStatus.CONFLICT, "SRM-004", "Star RoadMap already exists."),
    STAR_ROAD_MAP_USER_NOT_ACTIVE(DomainHttpStatus.NOT_FOUND, "SRM-005", "User account is not active."),
    STAR_ROAD_MAP_ROADMAP_INVALID(DomainHttpStatus.NOT_FOUND, "SRM-006",
            "RoadMap is invalid for starring (must be draft and not deleted).");

    private final DomainHttpStatus httpStatus;
    private final String code;
    private final String message;

    StarRoadMapErrorCode(
            final DomainHttpStatus httpStatus, final String code, final String message) {
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
