package com.renegade.videoondemand.exception;

public class TokenDoesNotExistException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Token does not exist.";
    }
}
