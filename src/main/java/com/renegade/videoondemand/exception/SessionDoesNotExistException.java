package com.renegade.videoondemand.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Session does not exist")
public class SessionDoesNotExistException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Session does not exist.";
    }
}
