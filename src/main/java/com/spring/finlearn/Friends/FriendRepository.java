package com.spring.finlearn.Friends;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friends,Long> {
    Optional<Friends> findByUserAndFriend(String sender, String receiver);

    List<Friends> findByUser(String username);
}
