package com.test.nals.util;

public enum ErrorCode {
    TIME_INVALID("The Time is not valid"),
    WORK_EXISTED("This work has already existed"),
    WORK_NOT_EXISTED("The work does not exist with this id");

    private String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
