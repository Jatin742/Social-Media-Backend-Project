package com.spring.finlearn;

import com.spring.finlearn.FriendRequests.FriendRequests;
import com.spring.finlearn.FriendRequests.FriendRequetsRepository;
import com.spring.finlearn.Friends.FriendRepository;
import com.spring.finlearn.Friends.Friends;
import com.spring.finlearn.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.spring.finlearn.Users.User;

import java.util.*;

@Service
public class Services {
    @Autowired
    UserRepository userRepository;

    @Autowired
    FriendRequetsRepository friendRequetsRepository;

    @Autowired
    FriendRepository friendRepository;
     public ResponseEntity<Object> addUser(User user){
         if (userRepository.existsByUsername(user.getUsername())) {
             return new ResponseEntity<>(new ErrorResponse("failure","Username Already Exists"),HttpStatus.BAD_REQUEST);
         }

         User newUser = userRepository.save(user);
         return new ResponseEntity<>(newUser, HttpStatus.CREATED);
     }
    public ResponseEntity<Object> sendFriendRequest(String senderId,String receiverId){
        Optional<User> sender=userRepository.findByUsername(senderId);
        Optional<User> receiver=userRepository.findByUsername(receiverId);
        //Checking if Users exists or not
        if(!sender.isPresent() && !receiver.isPresent()){
            return new ResponseEntity<>(new ErrorResponse("failure", "Sender and Receiver not found"), HttpStatus.NOT_FOUND);
        }
        else if(!sender.isPresent()){
            return new ResponseEntity<>(new ErrorResponse("failure", "Sender not found"), HttpStatus.NOT_FOUND);
        }
        else if(!receiver.isPresent()){
            return new ResponseEntity<>(new ErrorResponse("failure", "Receiver not found"), HttpStatus.NOT_FOUND);
        }
        Optional<Friends> existingFriendship = friendRepository.findByUserAndFriend(senderId, receiverId);
        if (existingFriendship.isPresent()) {
            return new ResponseEntity<>(new ErrorResponse("failure", "Users are already friends"), HttpStatus.BAD_REQUEST);
        }
        Optional<FriendRequests> existingRequest = friendRequetsRepository.findBySenderAndReceiver(senderId, receiverId);
        if (existingRequest.isPresent()) {
            return new ResponseEntity<>(new ErrorResponse("pending", "Friend request already sent"), HttpStatus.BAD_REQUEST);
        }
        existingRequest = friendRequetsRepository.findBySenderAndReceiver(receiverId, senderId);
        if (existingRequest.isPresent()) {
            Friends friends1=new Friends();
            friends1.setFriend(receiverId);
            friends1.setUser(senderId);
            friendRepository.save(friends1);
            Friends friends2=new Friends();
            friends2.setFriend(senderId);
            friends2.setUser(receiverId);
            friendRepository.save(friends2);
            friendRequetsRepository.delete(existingRequest.get());
            return new ResponseEntity<>(new Response("accepted"), HttpStatus.OK);
        }

        FriendRequests friendRequest = new FriendRequests();
        friendRequest.setSender(senderId);
        friendRequest.setReceiver(receiverId);
        friendRequetsRepository.save(friendRequest);
        return new ResponseEntity<>(new Response("success"), HttpStatus.OK);
    }

    public ResponseEntity<Object> getFriendRequests(String username) {
        Optional<User> user=userRepository.findByUsername(username);
        if(!user.isPresent()){
            return new ResponseEntity<>(new ErrorResponse("failure", "User not found"), HttpStatus.NOT_FOUND);
        }
        List<FriendRequests> requests=friendRequetsRepository.findByReceiver(username);
        if(requests.size()==0){
            return new ResponseEntity<>(new ErrorResponse("failure", "User does not have friend requests"), HttpStatus.OK);
        }
        List<String> ans=new ArrayList<>();
        for(FriendRequests friendRequests:requests){
            ans.add(friendRequests.getSender());
        }
        return new ResponseEntity<>(new RequestList(ans),HttpStatus.OK);
    }

    public ResponseEntity<Object> getFriendList(String username) {
        Optional<User> user=userRepository.findByUsername(username);
        if(!user.isPresent()){
            return new ResponseEntity<>(new ErrorResponse("failure", "User not found"), HttpStatus.NOT_FOUND);
        }
        List<Friends> friends=friendRepository.findByUser(username);
        if(friends.size()==0){
            return new ResponseEntity<>(new ErrorResponse("failure", "User does not have friends"), HttpStatus.OK);
        }
        List<String> ans=new ArrayList<>();
        for(Friends friend:friends){
            ans.add(friend.getFriend());
        }
        return new ResponseEntity<>(new FriendList(ans),HttpStatus.OK);
    }

    public ResponseEntity<Object> getSuggestions(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            return new ResponseEntity<>(new ErrorResponse("failure", "User not found"), HttpStatus.NOT_FOUND);
        }
        List<Friends> friends = friendRepository.findByUser(username);
        Set<String> f1=new HashSet<>();
        List<String> suggestedFriends=new ArrayList<>();
        for (Friends friend : friends) {
            f1.add(friend.getFriend());
        }
        Set<String> friendsOfFriends = new HashSet<>(f1);
        for(String friend:f1){
            List<Friends> firstDegreeFriends = friendRepository.findByUser(friend);
            for (Friends firstDegreeFriend : firstDegreeFriends) {
                String f=firstDegreeFriend.getFriend();
                if (!friendsOfFriends.contains(f) && !f.equals(username)) {
                    suggestedFriends.add(f);
                    friendsOfFriends.add(f);
                }
            }
        }
        Set<String> friendOfFriendOfFriend=new HashSet<>(friendsOfFriends);
        for(String friend:friendsOfFriends){
            List<Friends> secondDegreeFriends=friendRepository.findByUser(friend);
            for(Friends secondDegreeFriend:secondDegreeFriends) {
                String f = secondDegreeFriend.getFriend();
                    if (!friendOfFriendOfFriend.contains(f) && !f.equals(username)) {
                        suggestedFriends.add(f);
                        friendOfFriendOfFriend.add(f);
                    }
                }
        }
        if(suggestedFriends.size()==0){
            return new ResponseEntity<>(new ErrorResponse("failure", "User does not have friend suggestions"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new FriendSuggestionList(suggestedFriends),HttpStatus.OK);
    }
}

