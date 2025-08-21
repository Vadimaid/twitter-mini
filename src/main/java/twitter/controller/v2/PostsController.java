package twitter.controller.v2;

import twitter.dto.v2.response.PostsResponseDto;
import twitter.entity.post.Post;

import java.util.List;

public interface PostsController {

    PostsResponseDto addPost(String username);
    List<PostsResponseDto> myPosts(String username);
    List<PostsResponseDto> postByTag(String username, String tag);
    List<PostsResponseDto> postByUsername(String username);
    List<PostsResponseDto> postByUserType(String username);
}
