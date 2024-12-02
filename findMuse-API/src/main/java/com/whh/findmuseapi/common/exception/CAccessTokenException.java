package com.whh.findmuseapi.common.exception;

import com.whh.findmuseapi.common.constant.ResponseCode;
import org.springframework.http.HttpStatus;

public class CAccessTokenException extends FindmuseException {

    @Override
    public HttpStatus getStatus() {
        return ResponseCode.ACCESS_TOKEN_EXPIRED.getStatus();
    }

    public CAccessTokenException(String message) {
//        super("Token : " + token +  "을 파싱하는데 실패하였습니다.");
        super(message);
    }
    public CAccessTokenException(ResponseCode code, String message) {
        super(code.getMessage() + message);
    }

    public CAccessTokenException(ResponseCode code) {
        super(code.toString());
    }
}
