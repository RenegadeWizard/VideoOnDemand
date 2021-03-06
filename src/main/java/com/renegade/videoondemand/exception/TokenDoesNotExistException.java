package com.renegade.videoondemand.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Token does not exist")
public class TokenDoesNotExistException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Token does not exist.";
    }
}
