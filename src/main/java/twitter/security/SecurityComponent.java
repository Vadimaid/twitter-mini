package twitter.security;

import twitter.entity.user.User;

public interface SecurityComponent {

    User getAuthentication(String userIp);

    void setAuthentication(String userIp, User user);

    void removeAuthentication(String userIp);

}
