package com.spring.finlearn;

import java.util.List;

public class RequestList {
    private List<String> friend_request;

    RequestList(List<String> friend_request){
        this.friend_request=friend_request;
    }

    public List<String> getFriend_request() {
        return friend_request;
    }
}
