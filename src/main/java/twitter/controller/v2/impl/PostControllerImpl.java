package twitter.controller.v2.impl;

import twitter.controller.v2.PostController;
import twitter.dto.v1.PostResponseDto;
import twitter.entity.post.Post;
import twitter.exception.UnknownUserTypeException;
import twitter.service.PostService;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PostControllerImpl implements PostController {

    private final PostService postService;

    public PostControllerImpl(PostService postService) {
        this.postService = postService;
    }

    @Override
    public List<PostResponseDto> postsByUserType(int userType) throws UnknownUserTypeException {
        if (userType != 0 && userType != 1) {
            throw new UnknownUserTypeException("Введен неверный тип пользователя: " + userType);
        }
        List<Post> posts = postService.getAllPostsByUserType(userType);
        List<PostResponseDto> pDtos = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (Post post : posts) {
            PostResponseDto postResponseDto = new PostResponseDto();
            postResponseDto.setAuthor(post.getAuthor().whatIsYourName());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            postResponseDto.setCreatedAt(post.getCreationDate().format(formatter));
            postResponseDto.setTopic(post.getTopic());
            postResponseDto.setText(post.getText());
            postResponseDto.setTags(String.join(", ", post.getTags()));
            postResponseDto.setLikesCount(post.getUsersWhoLiked().size());
            pDtos.add(postResponseDto);
        }
        return pDtos;
    }
}
