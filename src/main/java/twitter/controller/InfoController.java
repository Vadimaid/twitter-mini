package twitter.controller;

import java.io.IOException;

/**
 * Interface for information operations in the application.
 */
public interface InfoController {
    
    /**
     * Displays help information about available commands.
     */
    void executeHelp() throws IOException;

    /**
     * Displays information about the current authenticated user.
     */
    void executeInfo() throws IOException;

    /**
     * Displays information about a user by their login.
     */
    void executeInfoByLogin() throws IOException;

    /**
     * Displays information about all users in the system.
     */
    void executeInfoAll() throws IOException;
}