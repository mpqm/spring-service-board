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

    public BaseRes(BaseResMsg baseResMsg) {
        this.code = baseResMsg.getCode();
        this.success = baseResMsg.getSuccess();
        this.message = baseResMsg.getMessage();
        this.result = null;
    }

    public BaseRes(BaseResMsg baseResMsg, T result) {
        this.code = baseResMsg.getCode();
        this.success = baseResMsg.getSuccess();
        this.message = baseResMsg.getMessage();
        this.result = result;
    }

}
