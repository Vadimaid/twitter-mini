package twitter.dto.v2.response;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PostResponseDto {

    private String author;
    private String createdAt;
    private String topic;
    private String text;
    private String tags;
    private Integer likesCount;
}
