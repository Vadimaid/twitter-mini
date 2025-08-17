package twitter.controller.v2;

import twitter.dto.v2.request.LoginRequestDto;
import twitter.dto.v2.response.LoginResponseDto;

public interface AuthenticationController {

    LoginResponseDto login(LoginRequestDto request);

}
