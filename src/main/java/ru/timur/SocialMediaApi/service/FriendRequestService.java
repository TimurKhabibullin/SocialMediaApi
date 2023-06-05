package ru.timur.SocialMediaApi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.timur.SocialMediaApi.models.FriendRequest;
import ru.timur.SocialMediaApi.models.Friendship;
import ru.timur.SocialMediaApi.models.Person;
import ru.timur.SocialMediaApi.models.RequestStatus;
import ru.timur.SocialMediaApi.repositoris.FriendRequestRepository;
import ru.timur.SocialMediaApi.repositoris.FriendshipRepository;

import java.util.List;

@Service
public class FriendRequestService {
    private final FriendRequestRepository friendRequestRepository;
    private final FriendshipRepository friendshipRepository;

    @Autowired
    public FriendRequestService(FriendRequestRepository friendRequestRepository, FriendshipRepository friendshipRepository) {
        this.friendRequestRepository = friendRequestRepository;
        this.friendshipRepository = friendshipRepository;
    }

    public FriendRequest sendFriendRequest(FriendRequest friendRequest) {
        friendRequest.setStatus(RequestStatus.PENDING.toString());
        return friendRequestRepository.save(friendRequest);
    }

    public void acceptFriendRequest(int friendRequestId) {
        FriendRequest friendRequest = friendRequestRepository.findById(friendRequestId);

        // Создание дружбы между пользователями
        Friendship friendship = new Friendship(friendRequest.getSender(), friendRequest.getRecipient());
        friendship.setUser1(friendRequest.getSender());
        friendship.setUser2(friendRequest.getRecipient());
        friendshipRepository.save(friendship);

        friendRequestRepository.delete(friendRequest);
    }

    public void rejectFriendRequest(int friendRequestId) {
        FriendRequest friendRequest = friendRequestRepository.findById(friendRequestId);
        friendRequest.setStatus(RequestStatus.REJECTED.toString());
    }

    public FriendRequest getFriendRequestById(int requestId) {
        return friendRequestRepository.findById(requestId);
    }

    public List<FriendRequest> findBySender(int senderId){
        return friendRequestRepository.findBySender(senderId);
    }

    public void remove(FriendRequest friendRequest){
        friendRequestRepository.delete(friendRequest);
    }
}