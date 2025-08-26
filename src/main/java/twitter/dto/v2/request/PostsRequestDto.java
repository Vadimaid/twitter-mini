package twitter.dto.v2.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PostsRequestDto {

    private String author;
    private String topic;
    private String text;
    private String tags;

}
