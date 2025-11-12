package com.kllhy.roadmap.travel.domain.exception;

import com.kllhy.roadmap.common.exception.DomainHttpStatus;
import com.kllhy.roadmap.common.exception.IErrorCode;

public enum TravelErrorCode implements IErrorCode {
    TRAVEL_NOT_FOUND(DomainHttpStatus.NOT_FOUND, "TRAVEL_001", "not found travel"),
    TRAVEL_TOPICS_INVALID(DomainHttpStatus.CONFLICT, "TRAVEL_002", "must be topicId"),
    TRAVEL_TOPICS_DUPLICATED(DomainHttpStatus.CONFLICT, "TRAVEL_003", "duplicated topicId"),
    TRAVEL_SUB_TOPICS_DUPLICATED(DomainHttpStatus.CONFLICT, "TRAVEL_004", "duplicated subTopicId"),
    TRAVEL_TOPICS_NOT_FOUND(DomainHttpStatus.NOT_FOUND, "TRAVEL_005", "not found topicId"),
    TRAVEL_SUB_TOPICS_NOT_FOUND(DomainHttpStatus.NOT_FOUND, "TRAVEL_006", "not found subTopicId"),
    TRAVEL_USER_NOT_ACTIVE(DomainHttpStatus.NOT_FOUND, "TRAVEL_007", "user not active"),
    TRAVEL_ROADMAP_INVALID(DomainHttpStatus.NOT_FOUND, "TRAVEL_008", "roadmap invalid"),
    TRAVEL_TOPIC_INVALID(DomainHttpStatus.NOT_FOUND, "TRAVEL_009", "topic invalid"),
    ;

    private final DomainHttpStatus httpStatus;
    private final String code;
    private final String message;

    TravelErrorCode(DomainHttpStatus httpStatus, String code, String message) {
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
