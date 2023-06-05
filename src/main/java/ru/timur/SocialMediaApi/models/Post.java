package ru.timur.SocialMediaApi.models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "header")
    private String header;
    @Column(name = "text")
    private String text;
    @Column(name = "link_for_image")
    private String linkForImage;
    @Column(name = "date_of_create")
    private long dateOfCreate;
    @Column(name = "person_id")
    private int person;

    public void setAll(int id, String header, String text, String linkForImage, long dateOfCreate, int person){
        this.id = id;
        this.header = header;
        this.text = text;
        this.linkForImage = linkForImage;
        this.dateOfCreate = dateOfCreate;
        this.person = person;
    }

    public Post() {
    }

    public Post(int id, String header, String text, String linkForImage, long dateOfCreate, int person) {
        this.id = id;
        this.header = header;
        this.text = text;
        this.linkForImage = linkForImage;
        this.dateOfCreate = dateOfCreate;
        this.person = person;
    }
}
