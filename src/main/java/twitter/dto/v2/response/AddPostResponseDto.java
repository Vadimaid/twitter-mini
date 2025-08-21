package twitter.dto.v2.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AddPostResponseDto {
    private Integer id;
    private String author;
    private String topic;
    private String text;
    private String tags;
    private String created_at;
    private Integer likes;

}
