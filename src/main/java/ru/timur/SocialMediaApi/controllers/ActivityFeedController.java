package ru.timur.SocialMediaApi.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.timur.SocialMediaApi.models.FriendRequest;
import ru.timur.SocialMediaApi.models.Friendship;
import ru.timur.SocialMediaApi.models.Person;
import ru.timur.SocialMediaApi.models.Post;
import ru.timur.SocialMediaApi.security.PersonDetails;
import ru.timur.SocialMediaApi.service.FriendRequestService;
import ru.timur.SocialMediaApi.service.FriendshipService;
import ru.timur.SocialMediaApi.service.PostService;
import java.util.*;

@RestController
@RequestMapping("/activity")
@Tag(name = "ActivityFeed", description = "Auth management APIs")
@RequiredArgsConstructor
public class ActivityFeedController {

    private final PostService postService;
    private final FriendshipService friendshipService;
    private final FriendRequestService friendRequestService;

    @GetMapping()
    @Operation(summary = "Отображение последних постов")
    @SecurityRequirement(name = "bearerAuth")
    public List<Post> showActivity(@RequestParam(value = "offset", required = false) Integer offset,
                                   @RequestParam(value = "limit", required = false) Integer limit,
                                   HttpServletRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            System.out.println(headerName + ": " + headerValue);
        }

        // Получить значение конкретного заголовка
        String contentType = request.getHeader("Content-Type");
        System.out.println("Content-Type: " + contentType);

        Person person = personDetails.getPerson();
        List<Integer> personsPostsId = new ArrayList<>();
        List<Friendship> friendships = friendshipService.findByUser1OrUser2(person.getId(),person.getId());
        List<FriendRequest> friendRequests = friendRequestService.findBySender(person.getId());

        for(Friendship friendship : friendships){
            if (friendship.getUser1() == person.getId()) {
                personsPostsId.add(friendship.getUser2());
            } else {
                personsPostsId.add(friendship.getUser1());
            }
        }
        for (FriendRequest friendRequest : friendRequests){
            personsPostsId.add(friendRequest.getRecipient());
        }

        List<Post> posts = new ArrayList<>();
        for (Integer integer : personsPostsId) {
            posts.addAll(postService.findAllByPersonId(integer));
        }
        posts.sort(Comparator.comparingLong(Post::getDateOfCreate));
        Collections.reverse(posts);
        if (offset != null && limit != null){
            return getPostsByPage(posts,offset,limit);
        }
        return posts;
    }

    public List<Post> getPostsByPage(List<Post> posts, int pageNumber, int pageSize) {
        int startIndex = pageNumber * pageSize;
        int endIndex = Math.min(startIndex + pageSize, posts.size());

        // Проверка на выход за пределы списка
        if (startIndex >= posts.size()) {
            return new ArrayList<>();
        }

        // Получение подсписка с учетом пагинации

        return posts.subList(startIndex, endIndex);
    }
}
