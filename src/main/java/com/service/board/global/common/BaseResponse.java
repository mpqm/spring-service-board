package com.service.board.global.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseResponse<T> {

    private Integer code;
    private Boolean success;
    private String message;
    private T result;

    public BaseResponse(BaseMsg baseMsg) {
        this.code = baseMsg.getCode();
        this.success = baseMsg.getSuccess();
        this.message = baseMsg.getMessage();
        this.result = null;
    }

    public BaseResponse(BaseMsg baseMsg, T result) {
        this.code = baseMsg.getCode();
        this.success = baseMsg.getSuccess();
        this.message = baseMsg.getMessage();
        this.result = result;
    }

}
