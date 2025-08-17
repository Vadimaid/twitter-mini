package twitter.service;

import twitter.entity.post.Post;
import twitter.entity.user.User;
import twitter.exception.TwitterUploadException;

import java.util.List;

public interface FileUploadService {

    List<User> uploadUsers(String filename) throws TwitterUploadException;

    List<Post> uploadPosts(String filename) throws TwitterUploadException;

}
