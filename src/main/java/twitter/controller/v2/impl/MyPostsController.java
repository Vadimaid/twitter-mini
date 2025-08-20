package twitter.controller.v2.impl;

import twitter.dto.v1.PostResponseDto;
import twitter.entity.post.Post;
import twitter.entity.user.User;
import twitter.exception.TwitterCommonException;
import twitter.exception.UserNotFoundException;
import twitter.mapper.v1.PostMapper;
import twitter.service.PostService;
import twitter.service.UserService;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MyPostsController {

    private final PostMapper postMapper;
    private final PostResponseDto postResponseDto;
    private final PostService postService;
    private final UserService userService;


    public MyPostsController(PostMapper postMapper, PostResponseDto postResponseDto, PostService postService, UserService userService) {
        this.postMapper = postMapper;
        this.postResponseDto = postResponseDto;
        this.postService = postService;
        this.userService = userService;
    }

    public List<PostResponseDto> MyPosts(String username) {
        if (Objects.isNull(username) || username.isBlank()) {
            throw new TwitterCommonException("Некорректные данные пользователя");
        }

        try {

            User user = userService.getUserByLogin(username);
            List<Post> posts = postService.getAllPostsByUser(user);
            List<PostResponseDto> postResponseDtos = posts.stream()
                    .map(post -> {
                        try {
                            return postMapper.mapToDto(post);
                        } catch (Exception e) {
                            throw new TwitterCommonException(e.getMessage());
                        }
                    })
                    .collect(Collectors.toList());


            return postResponseDtos;

        }catch (UserNotFoundException e){
            throw new TwitterCommonException(e.getMessage());
        }


    }
}
