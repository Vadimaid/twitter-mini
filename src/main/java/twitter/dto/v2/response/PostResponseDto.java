package twitter.dto.v2.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PostResponseDto {
    private Integer id;
    private String creationDate;
    private InfoResponseDto author;
    private String topic;
    private String text;
    private List<InfoResponseDto> usersWhoLiked;
    private String[] tags;
}
