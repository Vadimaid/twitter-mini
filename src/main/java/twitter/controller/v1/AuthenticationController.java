package twitter.controller.v1;

import twitter.exception.ClientDisconnectedException;

import java.io.IOException;

/**
 * Interface for authentication operations in the application.
 */
public interface AuthenticationController {
    
    /**
     * Exits the application.
     */
    void executeExit() throws IOException, ClientDisconnectedException;

    /**
     * Performs user login.
     */
    void executeLogin() throws IOException;

    /**
     * Performs user logout.
     */
    void executeLogout() throws IOException;
}