package twitter.controller.v2.impl;

import twitter.configuration.Component;
import twitter.configuration.Injection;
import twitter.controller.v2.PostController;
import twitter.dto.v2.request.PostRequestDto;
import twitter.dto.v2.response.PostResponseDto;
import twitter.entity.post.Post;
import twitter.exception.TwitterCommonException;
import twitter.exception.UnknownUserTypeException;
import twitter.mapper.ServletPostMapper;
import twitter.service.PostService;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostControllerImpl implements PostController {
    private final PostService postService;


    @Injection
    public PostControllerImpl(PostService postService) {
        this.postService = postService;
    }

    @Override
    public PostResponseDto addNewPost(PostRequestDto postRequestDto) {

        Post post = postService.createPost(postRequestDto);
        return ServletPostMapper.mapEntityToDto(post);
    }

    @Override
    public List<PostResponseDto> getMyPosts(Integer userId) {
        return postService.getAllPostsByUser(userId).stream()
                .map(ServletPostMapper::mapEntityToDto
                ).collect(Collectors.toList());

    }

    @Override
    public List<PostResponseDto> getAll() {
        return postService.getAllPosts().stream()
                .map(ServletPostMapper::mapEntityToDto
                ).collect(Collectors.toList());
    }

    @Override
    public List<PostResponseDto> getAllPostsByTag(String tag) {
        return postService.getAllPostsByTag(tag).stream()
                .map(ServletPostMapper::mapEntityToDto
                ).collect(Collectors.toList());

    }

    @Override
    public List<PostResponseDto> getAllPostsByLogin(String login) {
        return postService.getAllPostsByUser(login).stream()
                .map(ServletPostMapper::mapEntityToDto)
                .collect(Collectors.toList());
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

    @Override
    public PostResponseDto addLike(Integer postId, String username) {
        Post post = postService.addLike(postId, username);
        return ServletPostMapper.mapEntityToDto(post);
    }
}
