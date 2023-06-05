package ru.timur.SocialMediaApi.repositoris;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.timur.SocialMediaApi.models.Message;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findBySenderAndRecipient(int senderId, int recipientId);
}
