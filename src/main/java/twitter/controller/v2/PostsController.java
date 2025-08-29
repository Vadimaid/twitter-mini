package twitter.controller.v2;


import twitter.dto.v2.request.PostsRequestDto;
import twitter.dto.v2.response.PostsResponseDto;
import twitter.dto.v2.response.InfoResponseDto;


import java.util.List;

public interface PostsController {

    PostsResponseDto addPost(PostsRequestDto request, String username);
    List<PostsResponseDto> myPosts(String username);
    List<PostsResponseDto> postByTag(String username, String tag);
    List<PostsResponseDto> postByUsername(String username);
    List<PostsResponseDto> postByUserType(String username);
    List<InfoResponseDto> getPostUsersLikes(int postId);
}
