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


/*

{
     "userType": 0
     "login": "userlogin123"
     "password": "qwerty123"
     "firstName": "alexander"
     "lastName": "solo"
     "dateOfBirth": "1990-11-26"
     "title":
     "occupation":
     "dateOfFoundation":
}

{
     "userType": 1
     "login": "userlogin123"
     "password": "qwerty123"
     "firstName":
     "lastName":
     "dateOfBirth":
     "title": "nooborak"
     "occupation": "gaming"
     "dateOfFoundation": "2018-12-09"
}
 */