package twitter.controller.v2;

import twitter.dto.v2.response.InfoResponseDto;

public interface InfoController {

    InfoResponseDto info(String username);

}
