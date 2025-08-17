package twitter.mapper;

import twitter.dto.v1.PostResponseDto;
import twitter.dto.v2.response.AddPostResponseDto;
import twitter.entity.post.Post;
import twitter.exception.UserNotFoundException;

public interface PostMapper {

    PostResponseDto mapToDto(Post post) throws UserNotFoundException;

    Post mapFileStringToPost(String postAsString);

    Post mapUploadFileStringToPost(String postAsString);


}
