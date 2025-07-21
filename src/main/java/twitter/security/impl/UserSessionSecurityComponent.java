package twitter.security.impl;

import twitter.configuration.Component;
import twitter.configuration.Injection;
import twitter.entity.user.User;
import twitter.security.SecurityComponent;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserSessionSecurityComponent implements SecurityComponent {

    private final Map<String, User> userSessions;

    @Injection
    public UserSessionSecurityComponent() {
        this.userSessions = new HashMap<>();
    }


    @Override
    public User getAuthentication(String userIp) {
        if (!this.userSessions.containsKey(userIp)) {
            return null;
        }
        return this.userSessions.get(userIp);
    }

    @Override
    public void setAuthentication(String userIp, User user) {
        if (this.userSessions.containsKey(userIp)) {
            this.userSessions.remove(userIp);
        }
        this.userSessions.put(userIp, user);
        System.out.println("User " + user.whatIsYourName() + " logged in");
        System.out.println("Current sessions: " + this.userSessions.size());
        for (String key : this.userSessions.keySet()) {
            System.out.println("Session: " + key);
        }
    }

    @Override
    public void removeAuthentication(String userIp) {
        if (this.userSessions.containsKey(userIp)) {
            this.userSessions.remove(userIp);
            System.out.println("User logged out");
            System.out.println("Current sessions: " + this.userSessions.size());
            for (String key : this.userSessions.keySet()) {
                System.out.println("Session: " + key);
            }
        } else {
            System.out.println("User is not logged in");
        }
    }
}
