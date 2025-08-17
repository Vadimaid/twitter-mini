package twitter.security.impl;

import twitter.configuration.Component;
import twitter.configuration.Injection;
import twitter.entity.user.User;
import twitter.security.SecurityComponent;

//@Component
public class InMemorySecurityComponent implements SecurityComponent {
    
    private User authenticatedUser;

//    @Injection
    public InMemorySecurityComponent() {}
    
    @Override
    public User getAuthentication(String userIp) {
        return this.authenticatedUser;
    }

    @Override
    public void setAuthentication(String userIp, User user) {
        this.authenticatedUser = user;
    }

    @Override
    public void removeAuthentication(String userIp) {
        this.authenticatedUser = null;
    }
}
