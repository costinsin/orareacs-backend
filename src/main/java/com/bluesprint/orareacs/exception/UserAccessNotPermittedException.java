package com.bluesprint.orareacs.exception;

public class UserAccessNotPermittedException extends RuntimeException {
    public UserAccessNotPermittedException(String message) {
        super(message);
    }
}
