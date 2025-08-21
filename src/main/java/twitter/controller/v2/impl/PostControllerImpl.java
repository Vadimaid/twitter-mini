package twitter.controller.v2.impl;

import twitter.configuration.Component;
import twitter.configuration.Injection;
import twitter.controller.v2.PostController;
import twitter.dto.v2.request.AddPostRequestDto;
import twitter.dto.v2.response.AddPostResponseDto;
import twitter.entity.post.Post;
import twitter.entity.user.User;
import twitter.exception.TwitterCommonException;
import twitter.exception.UserNotFoundException;
import twitter.service.PostService;
import twitter.service.UserService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;

@Component
public class PostControllerImpl implements PostController {
    private final PostService postService;
    private final UserService userService;



    @Injection
    public PostControllerImpl(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @Override
    public AddPostResponseDto addPost(AddPostRequestDto request, String username) {
        if (Objects.isNull(request.getTopic()) || request.getTopic().isEmpty()) {
            throw new TwitterCommonException("Тема публикации не может быть пустой");

        }
        if (Objects.isNull(request.getText()) || request.getText().isEmpty()) {
            throw new TwitterCommonException("Текст публикации не может быть пустой");

        }
        String tags = request.getTags();


        try {
            User user = this.userService.getUserByLogin(username);
            AddPostResponseDto responseDto = new AddPostResponseDto();
            responseDto.setId(user.getId());

            Post post = new Post();


            post.setTopic(request.getTopic());
            post.setText(request.getText());
            post.setAuthor(user);
            post.setCreationDate(LocalDateTime.now());

            String[] tagArray = tags != null ? tags.split(",") : new String[0];
            post.setTags(tagArray);

            Post createdPost = postService.createPost(post);

            responseDto.setId(createdPost.getId());
            responseDto.setAuthor(createdPost.getAuthor().getLogin());
            responseDto.setTopic(createdPost.getTopic());
            responseDto.setText(createdPost.getText());
            responseDto.setTags(Arrays.toString(createdPost.getTags()));
            responseDto.setCreated_at(createdPost.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            return responseDto;
        } catch (UserNotFoundException ex) {
            throw new TwitterCommonException(ex.getMessage());
        }
    }
}



