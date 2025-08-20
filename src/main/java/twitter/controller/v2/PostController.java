package twitter.controller.v2;

import twitter.dto.v1.PostResponseDto;
import twitter.exception.UnknownUserTypeException;

import java.util.List;

public interface PostController {

    public List<PostResponseDto> postsByUserType(int userType) throws UnknownUserTypeException;
}
