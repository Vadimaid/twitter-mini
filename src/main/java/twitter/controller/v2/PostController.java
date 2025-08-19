package twitter.controller.v2;

import twitter.dto.v1.PostResponseDto;

import java.util.List;

public interface PostController {

    List<PostResponseDto> postsByLogin(String username);
}
