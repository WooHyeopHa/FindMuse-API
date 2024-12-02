package com.whh.findmuseapi.common.exception;

import com.whh.findmuseapi.common.constant.ResponseCode;
import org.springframework.http.HttpStatus;

public class CRefreshTokenException extends FindmuseException {
    @Override
    public HttpStatus getStatus() {
        return ResponseCode.REFRESH_TOKEN_EXPIRED.getStatus();
    }
    
    public CRefreshTokenException(String message) {
//        super("Token : " + token +  "을 파싱하는데 실패하였습니다.");
        super(message);
    }
    public CRefreshTokenException(ResponseCode code, String message) {
        super(code.getMessage() + message);
    }
    
    public CRefreshTokenException(ResponseCode code) {
        super(code.toString());
    }
}
