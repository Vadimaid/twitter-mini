package twitter.controller.v1;

import java.io.IOException;

/**
 * Interface for registration operations in the application.
 */
public interface RegistrationController {
    
    /**
     * Reads users from a file.
     */
    void executeReadUsers() throws IOException;

    /**
     * Registers a new user.
     */
    void executeRegister() throws IOException;
}