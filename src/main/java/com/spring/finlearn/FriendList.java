package com.spring.finlearn;

import java.util.List;

public class FriendList {
    private List<String> friends;

    FriendList(List<String> friends){
        this.friends=friends;
    }

    public List<String> getFriends() {
        return friends;
    }
}
