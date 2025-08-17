package twitter.configuration;

import java.util.Map;

public class Environment {

    private final Map<String, Object> env;
    private String applicationProfile;

    public Environment(Map<String, Object> env) {
        this.env = env;
    }

    public String getApplicationProfile() {
        return applicationProfile;
    }

    public void setApplicationProfile(String applicationProfile) {
        this.applicationProfile = applicationProfile;
    }

    public void addProperties(Map<String, Object> newProperties) {
        this.env.putAll(newProperties);
    }

    public Object get(String key) {
        return env.get(key);
    }

}
