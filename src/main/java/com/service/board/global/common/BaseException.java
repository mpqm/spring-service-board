package com.service.board.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BaseException extends Exception {
    private final Integer code;
    private final Boolean success;
    private final String message;
    private final String details;

    public BaseException(BaseResMsg baseResMsg) {
        this.success = baseResMsg.getSuccess();
        this.code = baseResMsg.getCode();
        this.message = baseResMsg.getMessage();
        this.details = null;
    }

    public BaseException(BaseResMsg baseResMsg, String details) {
        this.success = baseResMsg.getSuccess();
        this.code = baseResMsg.getCode();
        this.message = baseResMsg.getMessage();
        this.details = details;
    }
}
