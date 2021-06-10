package com.renegade.videoondemand.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.PRECONDITION_REQUIRED)
public class EtagNotProvidedException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Etag not provided in the header.";
    }
}
