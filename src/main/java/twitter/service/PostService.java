package twitter.service;

import twitter.entity.post.Post;
import twitter.entity.user.User;
import twitter.exception.UnknownUserTypeException;

import java.util.List;

public interface PostService {

    Post createPost(Post post);

    List<Post> getAllPostsByUser(User user);

    List<Post> getAllPosts();

   List<Post> getAllPostsByTag(String tag);

   List<Post> getAllPostsByUserType(int userType) throws UnknownUserTypeException;

   List<Post> createSeveralPosts(List<Post> posts);

}
