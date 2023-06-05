package ru.timur.SocialMediaApi.repositoris;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.timur.SocialMediaApi.models.FriendRequest;
import ru.timur.SocialMediaApi.models.Person;

import java.util.List;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Integer> {
    FriendRequest findById(int id);
    List<FriendRequest> findBySender(int senderId);
}
