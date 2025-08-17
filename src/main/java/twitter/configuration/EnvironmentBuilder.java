package twitter.configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EnvironmentBuilder {

    private final String defaultProfileName = "default";
    private final String defaultPropertiesFile = "application.properties";
    private final String profilePropertiesFile = "application-%s.properties";

    private Environment environment;

    private EnvironmentBuilder() {
        this.environment = new Environment(this.initProperties(this.defaultPropertiesFile));
    }

    public static EnvironmentBuilder buildEnvironment() {
        return new EnvironmentBuilder();
    }

    public EnvironmentBuilder withApplicationProfile(String applicationProfile) {
        if (this.defaultProfileName.equals(applicationProfile) || applicationProfile.isBlank()) {
            return this;
        }
        this.environment.setApplicationProfile(applicationProfile);
        Map<String, Object> properties = this.initProperties(String.format(this.profilePropertiesFile, applicationProfile));
        this.environment.addProperties(properties);
        return this;
    }

    public Environment build() {
        return this.environment;
    }

    private Map<String, Object> initProperties(String propertiesFile) {
        Map<String, Object> result = new HashMap<>();
        try (
                InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(propertiesFile);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))
        ) {
            reader.lines().forEach(line -> {
                if (Objects.nonNull(line) && !line.isBlank()) {
                    String key = line.substring(0, line.indexOf("="));
                    Object value = line.substring(line.indexOf("=") + 1);
                    result.put(key, value);
                }
            });
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        return result;
    }
}
