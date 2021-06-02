package com.renegade.videoondemand.exception;

import lombok.RequiredArgsConstructor;

import java.text.MessageFormat;

@RequiredArgsConstructor
public class ObjectNotInDatabaseException extends RuntimeException {
    private final String id;
    private final String entity;

    @Override
    public String getMessage() {
        return MessageFormat.format("Object with id: {0} not in {1}.", id, entity);
    }
}
