package ru.timur.SocialMediaApi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import ru.timur.SocialMediaApi.models.Friendship;
import ru.timur.SocialMediaApi.repositoris.FriendshipRepository;

import java.util.List;

@Service
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;

    @Autowired
    public FriendshipService(FriendshipRepository friendshipRepository) {
        this.friendshipRepository = friendshipRepository;
    }

    public Friendship getFriendshipById(int friendshipId) {
        return friendshipRepository.findById(friendshipId);
    }

    public void removeFriendship(Friendship friendship) {
        friendshipRepository.delete(friendship);
    }

    public List<Friendship> findByUser1OrUser2(int user1Id, int user2Id){
        return friendshipRepository.findByUser1OrUser2(user1Id,user2Id);
    }

    public Friendship findByUser1AndUser2(int user1Id, int user2Id){
        return friendshipRepository.findByUser1AndUser2(user1Id,user2Id);
    }

    public boolean isFriends(int senderId, int recipientId) {
        return friendshipRepository.findByUser1AndUser2(senderId, recipientId) != null ||
                friendshipRepository.findByUser1AndUser2(recipientId, senderId) != null;
    }
}