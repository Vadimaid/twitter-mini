package twitter;

import org.flywaydb.core.Flyway;
import twitter.configuration.ComponentFactory;
import twitter.configuration.Environment;
import twitter.configuration.EnvironmentBuilder;
import twitter.runner.ApplicationRunner;

public class Main {
    public static void main(String[] args) {
        String applicationProfile = "default";
        if (args.length > 0) {
            for (String arg : args) {
                if (arg.startsWith("application.profile=")) {
                    applicationProfile = arg.substring("application.profile=".length());
                }
            }
        }

        Environment environment = EnvironmentBuilder
                .buildEnvironment()
                .withApplicationProfile(applicationProfile)
                .build();

        ComponentFactory factory = new ComponentFactory(Main.class, environment);
        factory.configure();

        Flyway flyway = factory.getComponent(Flyway.class);
        flyway.migrate();

        ApplicationRunner runner = factory.getComponent(ApplicationRunner.class);
        runner.run();
    }
}