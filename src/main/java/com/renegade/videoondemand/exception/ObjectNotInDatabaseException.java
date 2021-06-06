package com.renegade.videoondemand.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ObjectNotInDatabaseException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Object not in database.";
    }
}
