package com.eduprime.eduprimeserver.dtos.response;

import lombok.Data;

@Data
public class MessageResponse {

    private int code;

    private String message;

    public static final MessageResponse of(int code, String message) {
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setCode(code);
        messageResponse.setMessage(message);
        return messageResponse;
    }
}
