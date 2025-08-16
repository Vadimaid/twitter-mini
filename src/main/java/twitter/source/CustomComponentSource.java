package twitter.source;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.flywaydb.core.Flyway;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import twitter.configuration.ComponentMethod;
import twitter.configuration.ComponentSource;
import twitter.configuration.Value;

import java.util.HashMap;
import java.util.Map;

@ComponentSource
public class CustomComponentSource {

    @Value(key = "database.url")
    private String dbUrl;

    @Value(key = "database.user")
    private String dbUser;

    @Value(key = "database.password")
    private String dbPassword;

    @ComponentMethod
    public Flyway flyway() {
        return Flyway
                .configure()
                .driver("org.postgresql.Driver")
                .dataSource(dbUrl, dbUser, dbPassword)
                .validateMigrationNaming(true)
                .validateOnMigrate(true)
                .baselineOnMigrate(true)
                .outOfOrder(true)
                .load();
    }

    @ComponentMethod
    public EntityManagerFactory entityManagerFactory() {
        Map<String, String> properties = new HashMap<>();
        properties.put("jakarta.persistence.jdbc.driver", "org.postgresql.Driver");
        properties.put("jakarta.persistence.jdbc.url", dbUrl);
        properties.put("jakarta.persistence.jdbc.user", dbUser);
        properties.put("jakarta.persistence.jdbc.password", dbPassword);

        return Persistence.createEntityManagerFactory("MiniTwitterPU", properties);
    }

    @ComponentMethod
    public AllowedEndpoints allowedEndpoints() {
        AllowedEndpoints allowedEndpoints = new AllowedEndpoints();

        allowedEndpoints.addEndpoint("/api/login");
        allowedEndpoints.addEndpoint("/api/register");
<<<<<<< HEAD
=======
        allowedEndpoints.addEndpoint("/api/help");
>>>>>>> 6fafb11864ac6f8f4c15f331b0a7ed3bf9506cc8

        return allowedEndpoints;
    }

<<<<<<< HEAD
=======
    @ComponentMethod
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4);
    }

>>>>>>> 6fafb11864ac6f8f4c15f331b0a7ed3bf9506cc8
}
