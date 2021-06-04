package com.renegade.videoondemand.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Authentication failed")
public class FailedAuthenticationException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Failed authentication!";
    }
}
