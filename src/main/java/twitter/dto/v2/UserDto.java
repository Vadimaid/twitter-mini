package twitter.dto.v2;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import twitter.entity.user.UserType;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private Integer id;
    private String login;
    private UserType userType;
}
