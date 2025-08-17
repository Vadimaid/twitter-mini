package twitter.controller.v2;

import twitter.dto.v2.request.RegisterRequestDto;
import twitter.dto.v2.response.InfoResponseDto;

public interface RegistrationController {

    InfoResponseDto register(RegisterRequestDto request);

}
