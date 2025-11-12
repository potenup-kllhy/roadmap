package com.kllhy.roadmap.common.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@JsonPropertyOrder({"code", "message", "data"})
public class ApiResponse<T> {
    private final String code;
    private final String message;
    private final T data;

    private ApiResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseEntity<ApiResponse<T>> of(IResponseCode code, T data) {
        return ResponseEntity.status(code.getHttpStatus()).body(new ApiResponse<>(code.getCode(), code.getMessage(), data));
    }
}
