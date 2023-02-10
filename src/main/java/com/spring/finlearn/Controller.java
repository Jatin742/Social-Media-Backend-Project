package com.spring.finlearn;

import com.spring.finlearn.Users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class Controller {
    @Autowired
    Services services;
    @PostMapping("/create")
    public ResponseEntity<Object> addUser(@RequestBody User user){
        return services.addUser(user);
    }

    @PostMapping("/add/{userA}/{userB}")
    public ResponseEntity<Object> sendFriendRequest(@PathVariable("userA") String usernameA,@PathVariable("userB") String usernameB){
        return services.sendFriendRequest(usernameA,usernameB);
    }
    @GetMapping("/friendrequests/{userA}")
    public ResponseEntity<Object> getFriendRequests(@PathVariable("userA") String username){
        return services.getFriendRequests(username);
    }
    @GetMapping("/friends/{userA}")
    public ResponseEntity<Object> getFriendList(@PathVariable("userA") String username){
        return services.getFriendList(username);
    }
    @GetMapping("/suggestions/{userA}")
    public ResponseEntity<Object> getSuggestions(@PathVariable("userA") String username){
        return services.getSuggestions(username);
    }
}
