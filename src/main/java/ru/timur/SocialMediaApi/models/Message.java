package ru.timur.SocialMediaApi.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "sender_id")
    private int sender;
    @Column(name = "recipient_id")
    private int recipient;
    @Column(name = "text")
    private String text;
}