package twitter.controller.v2;

import twitter.dto.v2.request.PostRequestDto;
import twitter.dto.v2.response.PostResponseDto;

import java.util.List;

public interface PostController {
    PostResponseDto addNewPost(PostRequestDto postRequestDto);

    List<PostResponseDto> getMyPosts(Integer id);

    List<PostResponseDto> getAll();

    List<PostResponseDto> getAllPostsByTag(String tag);

    List<PostResponseDto> getAllPostsByLogin(String login);
}
