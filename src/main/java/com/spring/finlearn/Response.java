package com.spring.finlearn;

public class Response {
    private String status;
    Response(String status){
        this.status=status;
    }
    public String getStatus() {
        return status;
    }

}
