package com.service.board.global.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseRes<T> {

    private Integer code;
    private Boolean success;
    private String message;
    private T result;

    public BaseRes(BaseMsg baseMsg) {
        this.code = baseMsg.getCode();
        this.success = baseMsg.getSuccess();
        this.message = baseMsg.getMessage();
        this.result = null;
    }

    public BaseRes(BaseMsg baseMsg, T result) {
        this.code = baseMsg.getCode();
        this.success = baseMsg.getSuccess();
        this.message = baseMsg.getMessage();
        this.result = result;
    }

}
