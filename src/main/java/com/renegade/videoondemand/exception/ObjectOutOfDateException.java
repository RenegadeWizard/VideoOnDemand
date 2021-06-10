package com.renegade.videoondemand.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.PRECONDITION_FAILED)
public class ObjectOutOfDateException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Object out of date.";
    }
}
