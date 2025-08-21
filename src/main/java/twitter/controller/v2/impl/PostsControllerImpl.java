package twitter.controller.v2.impl;

import twitter.configuration.Component;
import twitter.configuration.Injection;
import twitter.controller.v2.PostsController;
import twitter.dto.v1.PostResponseDto;
import twitter.dto.v2.response.PostsResponseDto;
import twitter.entity.post.Post;
import twitter.entity.user.User;
import twitter.exception.TwitterCommonException;
import twitter.exception.UserNotFoundException;
import twitter.mapper.v1.PostMapper;
import twitter.mapper.v2.impl.PostsMapper;
import twitter.service.PostService;
import twitter.service.UserService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Component
public class PostsControllerImpl implements PostsController {


    private final PostsMapper postMapper;
    private final PostResponseDto postResponseDto;
    private final PostService postService;
    private final UserService userService;

    @Injection
    public PostsControllerImpl(PostsMapper postMapper, PostResponseDto postResponseDto, PostService postService, UserService userService) {
        this.postMapper = postMapper;
        this.postResponseDto = postResponseDto;
        this.postService = postService;
        this.userService = userService;
    }

    @Override
    public List<PostsResponseDto> myPosts(String username) {
        if (Objects.isNull(username) || username.isBlank()) {
            throw new TwitterCommonException("Некорректные данные пользователя");
        }

        try {

            User user = userService.getUserByLogin(username);
            List<Post> posts = postService.getAllPostsByUser(user);
            List<PostsResponseDto> postResponseDtos = posts.stream()
                    .map(post -> {
                        try {
                            return postMapper.mapForDto(post);
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

    @Override
    public PostsResponseDto addPost(String username) {
        return null;
    }

    @Override
    public List<PostsResponseDto> postByTag(String username, String tag) {
        return List.of();
    }

    @Override
    public List<PostsResponseDto> postByUsername(String username) {
        return List.of();
    }

    @Override
    public List<PostsResponseDto> postByUserType(String username) {
        return List.of();
    }
}
