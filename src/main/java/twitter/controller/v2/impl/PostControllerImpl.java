package twitter.controller.v2.impl;

import twitter.configuration.Component;
import twitter.configuration.Injection;
import twitter.controller.v2.PostController;
import twitter.dto.v2.request.PostRequestDto;
import twitter.dto.v2.response.PostResponseDto;
import twitter.entity.post.Post;
import twitter.entity.user.User;
import twitter.exception.TwitterCommonException;
import twitter.exception.UnknownUserTypeException;
import twitter.exception.UserNotFoundException;
import twitter.mapper.ServletPostMapper;
import twitter.service.PostService;
import twitter.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    public PostResponseDto addNewPost(PostRequestDto postRequestDto) {
        Post post = new Post();
        post.setTopic(postRequestDto.getTopic());
        post.setText(postRequestDto.getText());
        String[] tagArray = postRequestDto.getTags().split(",");
        post.setTags(tagArray);
        post.setCreationDate(LocalDateTime.now());
        try {
            User author = userService.getUserByLogin(postRequestDto.getAuthor());
            post.setAuthor(author);
        } catch (UserNotFoundException e) {
            throw new TwitterCommonException("Автор публикации не найден в системе");
        }
        post = postService.createPost(post);
        return ServletPostMapper.mapEntityToDto(post);
    }

    @Override
    public List<PostResponseDto> getMyPosts(Integer id) {
        try {
            return postService.getAllPostsByUser(userService.getUserById(id)).stream()
                    .map(ServletPostMapper::mapEntityToDto
                    ).collect(Collectors.toList());
        } catch (UserNotFoundException e) {
            throw new TwitterCommonException(e.getMessage());
        }
    }

    @Override
    public List<PostResponseDto> getAll() {
        return postService.getAllPosts().stream()
                .map(ServletPostMapper::mapEntityToDto
                ).collect(Collectors.toList());
    }

    @Override
    public List<PostResponseDto> getAllPostsByTag(String tag) {
                return  postService.getAllPostsByTag(tag).stream()
                .map(ServletPostMapper::mapEntityToDto
                ).collect(Collectors.toList());

    }

    @Override
    public List<PostResponseDto> getAllPostsByLogin(String login) {
        try {
            User user = userService.getUserByLogin(login);
            return postService.getAllPostsByUser(user).stream()
                    .map(ServletPostMapper::mapEntityToDto)
                    .collect(Collectors.toList());
        } catch (UserNotFoundException e) {
            throw new TwitterCommonException(e.getMessage());
        }

    }

    @Override
    public List<PostResponseDto> getAllPostsByUserType(Integer userType) {
        try {
            return postService.getAllPostsByUserType(userType).stream()
                    .map(ServletPostMapper::mapEntityToDto)
                    .collect(Collectors.toList());
        } catch (UnknownUserTypeException e) {
            throw new TwitterCommonException(e.getMessage());
        }
    }
}
