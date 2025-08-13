package twitter.dto.v2.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RegisterRequestDto {

    private Integer userType;
    private String login;
    private String password;

    private String firstName;
    private String lastName;
    private String dateOfBirth;

    private String title;
    private String occupation;
    private String dateOfFoundation;

}
