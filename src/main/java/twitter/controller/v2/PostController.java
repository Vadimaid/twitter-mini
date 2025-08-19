package twitter.controller.v2;

import twitter.dto.v2.request.AddPostRequestDto;
import twitter.dto.v2.response.AddPostResponseDto;

public interface PostController {
    AddPostResponseDto addPost(AddPostRequestDto request, String username);

}
