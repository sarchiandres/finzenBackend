package com.FinZen.payload.response;

import lombok.Data;

@Data
public class MessageResponse {
    private String message;
    private Object data;
    

    public MessageResponse(String message) {
        this.message = message;
    }

    public MessageResponse(String message, Object data) {
        this.message = message;
        this.data = data;
    }
}
