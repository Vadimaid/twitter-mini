package twitter.dto.v2.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InfoResponseDto  {

    private Integer id;
    private String login;

    private String firstName;
    private String lastName;
    private String dateOfBirth;

    private String title;
    private String occupation;
    private String dateOfFoundation;

}
