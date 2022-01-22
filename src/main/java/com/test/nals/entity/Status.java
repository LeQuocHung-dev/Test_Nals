package com.test.nals.entity;

public enum Status {
    PLANNING("Planning"),
    DOING("Doing"),
    COMPLETE("Complete");
    
    private String message;
    
    Status(String message) {
     this.message = message;    
    }
    
    public String getMessage() {
        return this.message;
    }
}
