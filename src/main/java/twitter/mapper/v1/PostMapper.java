package twitter.mapper.v1;

import twitter.dto.v1.PostResponseDto;
import twitter.entity.post.Post;
import twitter.exception.UserNotFoundException;

public interface PostMapper {

    PostResponseDto mapToDto(Post post) throws UserNotFoundException;

    Post mapFileStringToPost(String postAsString);

    Post mapUploadFileStringToPost(String postAsString);


}
