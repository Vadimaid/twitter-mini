package twitter.controller.v2.impl;

import twitter.configuration.Component;
import twitter.configuration.Injection;
import twitter.controller.v2.PostsController;
import twitter.dto.v2.request.PostsRequestDto;
import twitter.dto.v2.response.PostsResponseDto;
import twitter.entity.post.Post;
import twitter.entity.user.User;
import twitter.exception.TwitterCommonException;
import twitter.exception.UserNotFoundException;
import twitter.mapper.v2.impl.PostsMapper;
import twitter.service.PostService;
import twitter.service.UserService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Component
public class PostsControllerImpl implements PostsController {


    private final PostsMapper postMapper;
    private final PostService postService;
    private final UserService userService;

    @Injection
    public PostsControllerImpl(PostsMapper postMapper, PostService postService, UserService userService) {
        this.postMapper = postMapper;
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
    public PostsResponseDto addPost(PostsRequestDto request, String username) {
        if (Objects.isNull(request.getTopic()) || request.getTopic().isEmpty()) {
            throw new TwitterCommonException("Тема публикации не может быть пустой");

        }
        if (Objects.isNull(request.getText()) || request.getText().isEmpty()) {
            throw new TwitterCommonException("Текст публикации не может быть пустой");

        }

        try {
            User user = userService.getUserByLogin(username);

            Post post = new Post();
            post.setTopic(request.getTopic());
            post.setText(request.getText());
            post.setAuthor(user);
            post.setCreationDate(LocalDateTime.now());

            String[] tagArray = request.getTags() != null ? request.getTags().split(",") : new String[0];
            post.setTags(tagArray);

            Post createdPost = postService.createPost(post);

            try {
                return postMapper.mapForDto(createdPost);
            } catch (Exception e) {
                throw new TwitterCommonException(e.getMessage());
            }

        } catch (UserNotFoundException ex) {
            throw new TwitterCommonException(ex.getMessage());
        }
    }

    @Override
    public List<PostsResponseDto> postByTag(String tag) {
        if (Objects.isNull(tag) || tag.trim().isEmpty()) {
            throw new TwitterCommonException("Тег публикации не может быть пустым");
        }

        List<Post> posts = postService.getAllPostsByTag(tag);
        List<PostsResponseDto> result = new ArrayList<>();

        for (Post post : posts) {
            try {
                result.add(postMapper.mapForDto(post));
            } catch (Exception e) {
                throw new TwitterCommonException("Ошибка при преобразовании поста: " + e.getMessage());
            }
        }

        return result;
    }

    @Override
    public List<PostsResponseDto> postByUsername(String username) {
        if (Objects.isNull(username) || username.isBlank()) {
            throw new TwitterCommonException("Некорректные данные пользователя");
        }

        try {
            User user = this.userService.getUserByLogin(username);

            List<PostsResponseDto> responseDtoS = new ArrayList<>();
            List<Post> posts = this.postService.getAllPostsByUser(user);
            for (Post post : posts) {
                PostsResponseDto responseDto = this.postMapper.mapForDto(post);

                responseDtoS.add(responseDto);
            }

            return responseDtoS;

        } catch (UserNotFoundException ex) {
            throw new TwitterCommonException(ex.getMessage());
        }
    }

    @Override
    public List<PostsResponseDto> postByUserType(String username) {
        return List.of();
    }
}
