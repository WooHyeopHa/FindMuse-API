package com.whh.findmuseapi.common.exception;

import com.whh.findmuseapi.common.constant.ResponseCode;
import org.springframework.http.HttpStatus;

public class CInternalServerError extends FindmuseException{

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public CInternalServerError(String message) {
        super(message);
    }

    public CInternalServerError(ResponseCode code) {
        super(code.getMessage());
    }

    public CInternalServerError(ResponseCode code, String message) {
        super(code.getMessage() + message);
    }
}
