package twitter.mapper;

import twitter.dto.v2.response.PostResponseDto;
import twitter.entity.post.Post;

public class ServletPostMapper {
    public static PostResponseDto mapEntityToDto(Post post) {
        PostResponseDto postResponseDto = new PostResponseDto();
                    postResponseDto.setId(post.getId());
                    postResponseDto.setCreationDate(post.getCreationDate().toString());
                    postResponseDto.setTopic(post.getTopic());
                    postResponseDto.setText(post.getText());
                    postResponseDto.setTags(post.getTags());
                    postResponseDto.setAuthor(ServletUserMapper.mapEntityToDtoResponse(post.getAuthor()));
                    return postResponseDto;
    }
}
