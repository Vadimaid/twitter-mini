package twitter.controller.v2;

import twitter.dto.v2.response.PostResponseDto;

import java.util.List;

public interface PostController {

    List<PostResponseDto> postsByLogin(String username);
}
