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
import ru.timur.SocialMediaApi.dto.PostDTO;
import ru.timur.SocialMediaApi.dto.PostUpdateDTO;
import ru.timur.SocialMediaApi.models.Person;
import ru.timur.SocialMediaApi.models.Post;
import ru.timur.SocialMediaApi.security.PersonDetails;
import ru.timur.SocialMediaApi.service.PostService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostsController {

    private final PostService postService;
    private final ModelMapper modelMapper;

    @Operation(summary = "Создание поста")
    @PostMapping("/create")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> create(@RequestBody @Valid PostDTO postDTO,
                                    BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        Person person = personDetails.getPerson();
        Post post = converteToPost(postDTO);
        post.setPerson(person.getId());

        return ResponseEntity.status(HttpStatus.OK).body(postService.create(post));
    }

    @Operation(summary = "Вывод всех постов")
    @GetMapping("/showAll")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> showAll(){
        return ResponseEntity.status(HttpStatus.OK).body(postService.showAll());
    }

    @Operation(summary = "Изменение поста")
    @PostMapping("/update/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody @Valid PostUpdateDTO postUpdateDTO,
                       BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        if (postUpdateDTO.getLinkForImage() == null && postUpdateDTO.getHeader() == null && postUpdateDTO.getText() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You didn't fill in one field");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        Person person = personDetails.getPerson();

        Post postToBeUpdated = postService.findById(id);
        if (postUpdateDTO.getHeader() != null)
            postToBeUpdated.setHeader(postUpdateDTO.getHeader());
        if (postUpdateDTO.getText() != null)
            postToBeUpdated.setText(postUpdateDTO.getText());
        if (postUpdateDTO.getLinkForImage() != null)
            postToBeUpdated.setLinkForImage(postUpdateDTO.getLinkForImage());
        if(person.getId() == postToBeUpdated.getPerson()){
            return ResponseEntity.status(HttpStatus.OK).body(postService.update(postToBeUpdated));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You cannot edit this post because you are not the owner");
        }
    }

    @Operation(summary = "Удаление поста")
    @DeleteMapping("/delete/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> delete(@PathVariable int id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        Person person = personDetails.getPerson();
        Post postToBeDeleted = postService.findById(id);
        if (person.getId() == postToBeDeleted.getPerson()){
            postService.delete(postToBeDeleted);
            return ResponseEntity.status(HttpStatus.OK).body("OK");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You cannot delete this post because you are not the owner");
    }

    private Post converteToPost(PostDTO postDTO) {
        return this.modelMapper.map(postDTO, Post.class);
    }

}
