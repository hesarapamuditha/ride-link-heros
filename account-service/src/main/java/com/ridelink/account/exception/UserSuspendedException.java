package com.ridelink.account.exception;

public class UserSuspendedException extends RuntimeException {
    public UserSuspendedException(String message) {
        super(message);
    }
}
