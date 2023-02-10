package com.spring.finlearn.FriendRequests;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import java.util.Optional;

@Repository
public interface FriendRequetsRepository extends JpaRepository<FriendRequests,Long> {
    Optional<FriendRequests> findBySenderAndReceiver(String sender, String receiver);

    List<FriendRequests> findByReceiver(String receiver);
}
