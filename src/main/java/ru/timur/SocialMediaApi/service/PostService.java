package ru.timur.SocialMediaApi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.timur.SocialMediaApi.models.Post;
import ru.timur.SocialMediaApi.repositoris.PostsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    private final PostsRepository postsRepository;

    @Autowired
    public PostService(PostsRepository postsRepository) {
        this.postsRepository = postsRepository;
    }

    public Post create(Post post) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        long timestamp = currentDateTime.toEpochSecond(java.time.ZoneOffset.UTC);
        post.setDateOfCreate(timestamp);
        return postsRepository.save(post);
    }

    public List<Post> showAll() {
        return postsRepository.findAll();
    }

    public Post findById(int id) {
        return postsRepository.findById(id);
    }

    public Post update(Post post) {
        return postsRepository.save(post);
    }

    public void delete(Post postToBeDeleted) {
        postsRepository.delete(postToBeDeleted);
    }

    public List<Post> findAllByPersonId(int personId){
        return postsRepository.findAllByPerson(personId);
    }
}
