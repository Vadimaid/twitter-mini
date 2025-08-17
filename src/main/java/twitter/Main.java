package twitter;

import org.flywaydb.core.Flyway;
import twitter.configuration.ComponentFactory;
import twitter.configuration.Environment;
import twitter.configuration.EnvironmentBuilder;
import twitter.runner.ApplicationRunner;

public class Main {
    public static void main(String[] args) {
        final String profilePrefix = "application.profile=";

        String applicationProfile = "default";
        if (args.length > 0) {
            for (String arg : args) {
                if (arg.startsWith(profilePrefix)) {
                    applicationProfile = arg.substring(profilePrefix.length());
                }
            }
        }

        Environment environment = EnvironmentBuilder
                .buildEnvironment()
                .withApplicationProfile(applicationProfile)
                .build();

        ComponentFactory.use(Main.class, environment);
        ComponentFactory.configure();

        Flyway flyway = ComponentFactory.getComponent(Flyway.class);
        flyway.migrate();

        ApplicationRunner runner = ComponentFactory.getComponent(ApplicationRunner.class);
        runner.run();
    }
}