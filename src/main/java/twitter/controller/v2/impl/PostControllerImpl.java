package twitter.controller.v2.impl;

import twitter.configuration.Component;
import twitter.configuration.Injection;
import twitter.controller.v2.PostController;
import twitter.dto.v2.response.PostResponseDto;
import twitter.entity.post.Post;
import twitter.entity.user.User;
import twitter.exception.TwitterCommonException;
import twitter.exception.UserNotFoundException;
import twitter.service.PostService;
import twitter.service.UserService;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@Component
public class PostControllerImpl implements PostController {

    private final UserService userService;
    private final PostService postService;


    @Injection
    public PostControllerImpl(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @Override
    public List<PostResponseDto> postsByLogin(String username) {
        if (Objects.isNull(username) || username.isBlank()) {
            throw new TwitterCommonException("Некорректные данные пользователя");
        }
        try {
            User user = this.userService.getUserByLogin(username);

            List<PostResponseDto> responseDtoS = new ArrayList<>();
            List<Post> posts = this.postService.getAllPostsByUser(user);
            for (Post post : posts) {
                PostResponseDto responseDto = new PostResponseDto();
                responseDto.setAuthor(post.getAuthor().getLogin());
                responseDto.setCreatedAt(post.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                responseDto.setTopic(post.getTopic());
                responseDto.setText(post.getText());
                responseDto.setTags(Arrays.toString(post.getTags()));

                responseDtoS.add(responseDto);
            }

            return responseDtoS;

        } catch (UserNotFoundException ex) {
            throw new TwitterCommonException(ex.getMessage());
        }
    }
}
