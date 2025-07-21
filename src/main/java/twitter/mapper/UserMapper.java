package twitter.mapper;

import twitter.entity.user.User;

public interface UserMapper {

    User mapFileStringToUser(String userAsString);

    User mapUploadFileStringToUser(String userAsString);


}
