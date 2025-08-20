package twitter.controller.v2;

import twitter.dto.v2.response.InfoResponseDto;

import java.util.List;

public interface InfoController {

    InfoResponseDto info(String username);

    List<InfoResponseDto> infoAll();


}
