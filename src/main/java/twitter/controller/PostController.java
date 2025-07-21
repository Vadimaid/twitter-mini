package twitter.controller;

import java.io.IOException;

/**
 * Interface for post operations in the application.
 */
public interface PostController {
    
    /**
     * Reads posts from a file.
     */
    void executeReadPosts() throws IOException;

    /**
     * Creates a new post.
     */
    void executeAddPost() throws IOException;

    /**
     * Displays posts by the current authenticated user.
     */
    void executeMyPosts() throws IOException;

    /**
     * Displays all posts in the system.
     */
    void executeAllPosts() throws IOException;

    /**
     * Displays posts filtered by tag.
     */
    void executePostsByTag() throws IOException;

    /**
     * Displays posts by a specific user login.
     */
    void executePostsByLogin() throws IOException;

    /**
     * Displays posts filtered by user type.
     */
    void executePostsByUserType() throws IOException;
}