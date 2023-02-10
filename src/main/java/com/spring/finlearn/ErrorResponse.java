package com.spring.finlearn;

public class ErrorResponse {
    private final String status;
    private final String reason;

    public ErrorResponse(String status, String reason) {
        this.status = status;
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }
}