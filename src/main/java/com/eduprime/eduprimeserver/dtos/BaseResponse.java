package com.eduprime.eduprimeserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> {

    private int code;

    private String message;

    private T result;

    public static final <T> BaseResponse of(T result, int code, String message) {
        BaseResponse<T> response = new BaseResponse<>();
        response.setCode(code);
        response.setMessage(message);
        response.setResult(result);
        return response;
    }

    public static final <T> BaseResponse error(int code, String message) {
        BaseResponse<T> response = new BaseResponse<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }

    public static final <T> BaseResponse message(int code, String message) {
        BaseResponse<T> response = new BaseResponse<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }
}
