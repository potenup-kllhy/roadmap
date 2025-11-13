package com.kllhy.roadmap.common.response;

import org.springframework.http.HttpStatus;

public interface IResponseCode {
    HttpStatus getHttpStatus();

    String getCode();

    String getMessage();
}
