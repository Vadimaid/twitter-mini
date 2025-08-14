package twitter.controller.v2;

import twitter.dto.v2.request.PostRequestDto;
import twitter.dto.v2.response.PostResponseDto;

public interface PostController {
    PostResponseDto addNewPost(PostRequestDto postRequestDto);

}
