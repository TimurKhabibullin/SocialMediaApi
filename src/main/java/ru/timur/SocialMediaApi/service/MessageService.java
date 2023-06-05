package ru.timur.SocialMediaApi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.timur.SocialMediaApi.models.Message;
import ru.timur.SocialMediaApi.models.Person;
import ru.timur.SocialMediaApi.repositoris.MessageRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message sendMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> getMessages(Person user1, Person user2) {
        List<Message> messages = new ArrayList<>();

        messages.addAll(messageRepository.findBySenderAndRecipient(user1.getId(),user2.getId()));
        messages.addAll(messageRepository.findBySenderAndRecipient(user2.getId(), user1.getId()));

        return messages;
    }
}
