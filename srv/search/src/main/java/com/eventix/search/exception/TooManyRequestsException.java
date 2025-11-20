package com.eventix.search.exception;

public class TooManyRequestsException extends RuntimeException {
    public TooManyRequestsException(String msg) { super(msg); }
}
