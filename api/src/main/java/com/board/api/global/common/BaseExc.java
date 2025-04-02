package com.board.api.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BaseExc extends Exception {

    private final Integer code;
    private final Boolean success;
    private final String message;
    private final String details;

    public BaseExc(BaseMsg baseMsg) {
        this.success = baseMsg.getSuccess();
        this.code = baseMsg.getCode();
        this.message = baseMsg.getMessage();
        this.details = null;
    }

    public BaseExc(BaseMsg baseMsg, String details) {
        this.success = baseMsg.getSuccess();
        this.code = baseMsg.getCode();
        this.message = baseMsg.getMessage();
        this.details = details;
    }

}
