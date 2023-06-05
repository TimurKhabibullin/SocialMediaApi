package ru.timur.SocialMediaApi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.timur.SocialMediaApi.dto.MessageDTO;
import ru.timur.SocialMediaApi.models.*;
import ru.timur.SocialMediaApi.security.PersonDetails;
import ru.timur.SocialMediaApi.service.FriendRequestService;
import ru.timur.SocialMediaApi.service.FriendshipService;
import ru.timur.SocialMediaApi.service.MessageService;
import ru.timur.SocialMediaApi.service.PersonDetailsService;

import java.util.*;

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendController {

    private final FriendRequestService friendRequestService;
    private final FriendshipService friendshipService;
    private final MessageService messageService;
    private final ModelMapper modelMapper;

    private final PersonDetailsService personDetailsService;


    @Operation(summary = "Отправка заявки в друзья")
    @PostMapping("/requests")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> sendFriendRequest(@RequestParam(value = "senderId", required = false) Integer senderId,
                                            @RequestParam(value = "recipientId", required = false) Integer recipientId) {
        if (senderId == null || recipientId == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You must specify the senderId and recipientId");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        Person person = personDetails.getPerson();

        // Отправка заявки в друзья

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSender(senderId);
        friendRequest.setRecipient(recipientId);
        if (Objects.equals(person.getId(), senderId))
            return ResponseEntity.status(HttpStatus.OK).body(friendRequestService.sendFriendRequest(friendRequest));
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You cannot send requests on behalf of other users, please provide a valid id");
    }

    @Operation(summary = "Принятие заявки в друзья")
    @PostMapping("/requests/{requestId}/accept")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> acceptFriendRequest(@PathVariable(value = "requestId", required = false) Integer requestId) {
        // Получение заявки по идентификатору
        if (requestId == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You must specify requestId");
        }
        FriendRequest request = friendRequestService.getFriendRequestById(requestId);
        if (request == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Application with this id was not found");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        // принятие заявки
        Person person = personDetails.getPerson();
        if (request.getRecipient() == person.getId()) {
            friendRequestService.acceptFriendRequest(request.getId());
            return ResponseEntity.status(HttpStatus.OK).body("Application from user: " +
                    personDetailsService.findByPersonId(request.getSender()).getUsername()
                    + " successfully accepted");
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You cannot accept only your own applications");
        }
    }

    @Operation(summary = "Отклонение заявки в друзья")
    @PostMapping("/requests/{requestId}/reject")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> rejectFriendRequest(@PathVariable(value = "requestId", required = false) Integer requestId) {
        if (requestId == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You must specify requestId");
        }
        // Получение заявки по идентификатору
        FriendRequest request = friendRequestService.getFriendRequestById(requestId);
        if (request == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Application with this id was not found");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        Person person = personDetails.getPerson();
        if (request.getRecipient() == person.getId()) {
            // Отклонение заявки
            friendRequestService.rejectFriendRequest(request.getId());
            return ResponseEntity.status(HttpStatus.OK).body("Application successfully rejected");
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You can only reject your own applications");
        }
    }

    @Operation(summary = "Удаление заявки в друзья")
    @DeleteMapping("/requests/{requestId}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> removeRequest(@PathVariable(value = "requestId", required = false) Integer requestId){
        if (requestId == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You must specify requestId");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        FriendRequest friendRequest = friendRequestService.getFriendRequestById(requestId);
        Person person = personDetails.getPerson();
        if (friendRequest == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Application with this id was not found");
        }
        if (person.getId() == friendRequest.getSender()){
            friendRequestService.remove(friendRequest);
            return ResponseEntity.status(HttpStatus.OK).body("Friend request successfully remove");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You can only remove your own applications");
        }


    }

    @Operation(summary = "Удаление дружбы")
    @DeleteMapping("/friendships/{friendshipId}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> removeFriendship(@PathVariable(value = "friendshipId", required = false) Integer friendshipId) {
        if (friendshipId == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You must specify requestId");
        }
        // Получение дружбы по идентификатору
        Friendship friendship = friendshipService.getFriendshipById(friendshipId);
        if (friendship == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Friendship with this id was not found");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        Person person = personDetails.getPerson();

        // Удаление дружбы
        if (friendship.getUser1() == person.getId() || friendship.getUser2() == person.getId()) {
            FriendRequest friendRequest = new FriendRequest();
            friendRequest.setRecipient(person.getId());
            if (person.getId() == friendship.getUser1()){
                friendRequest.setSender(friendship.getUser2());
            } else {
                friendRequest.setSender(friendship.getUser1());
            }
            friendRequest.setStatus(RequestStatus.PENDING.toString());
            friendRequestService.sendFriendRequest(friendRequest);
            friendshipService.removeFriendship(friendship);
            return ResponseEntity.status(HttpStatus.OK).body("Friendship successfully remove");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You can only remove your own friendship");
        }
    }

    @Operation(summary = "Отправка сообщения")
    @PostMapping("/messages")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> sendMessage(@RequestBody @Valid MessageDTO messageDTO, BindingResult bindingResult ){

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        Message message = converteToMessage(messageDTO);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        Person person = personDetails.getPerson();

        if (friendshipService.isFriends(message.getSender(),message.getRecipient()) && person.getId() == message.getSender()) {
            // Отправка сообщения
            return ResponseEntity.status(HttpStatus.OK).body(messageService.sendMessage(message));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Only friends can send messages");
        }
    }


    @Operation(summary = "Получение сообщений")
    @GetMapping("/messages")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> getMessages(@RequestParam(value = "user1Id",required = false) Integer user1Id,
                                     @RequestParam(value = "user2Id",required = false) Integer user2Id) {
        if (user1Id == null || user2Id == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You must specify user1Id and user2Id");
        // Получение пользователей по их идентификаторам
        Person user1 = personDetailsService.findByPersonId(user1Id);
        Person user2 = personDetailsService.findByPersonId(user2Id);

        if (user1.getId() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with this id:"+ user1Id + " was not found");
        if (user2.getId() == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with this id:"+ user2Id + " was not found");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        Person person = personDetails.getPerson();

        // Получение сообщений между пользователями
        if (Objects.equals(person.getId(), user1Id) || Objects.equals(person.getId(), user2Id)) {
            if (friendshipService.isFriends(user1Id, user2Id)) {
                // Отправка сообщения
                return ResponseEntity.status(HttpStatus.OK).body(messageService.getMessages(user1, user2));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Messages are only available to friends");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You can only receive your messages");
        }
    }

    private Message converteToMessage(MessageDTO messageDTO) {
            return this.modelMapper.map(messageDTO, Message.class);
    }
}