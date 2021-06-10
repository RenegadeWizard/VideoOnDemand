package com.renegade.videoondemand.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ObjectNotInDatabaseException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Object not in database.";
    }
}
