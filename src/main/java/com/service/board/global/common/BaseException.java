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

    public BaseException(BaseMsg baseMsg) {
        this.success = baseMsg.getSuccess();
        this.code = baseMsg.getCode();
        this.message = baseMsg.getMessage();
        this.details = null;
    }

    public BaseException(BaseMsg baseMsg, String details) {
        this.success = baseMsg.getSuccess();
        this.code = baseMsg.getCode();
        this.message = baseMsg.getMessage();
        this.details = details;
    }
}
