package twitter.service;

import twitter.dto.v2.request.PostRequestDto;
import twitter.entity.post.Post;
import twitter.entity.user.User;
import twitter.exception.UnknownUserTypeException;

import java.util.List;

public interface PostService {

    Post createPost(Post post);

    List<Post> getAllPostsByUser(User user);
    List<Post> getAllPostsByUser(String username);
    List<Post> getAllPostsByUser(Integer userId);

    List<Post> getAllPosts();

   List<Post> getAllPostsByTag(String tag);

   List<Post> getAllPostsByUserType(int userType) throws UnknownUserTypeException;

   List<Post> createSeveralPosts(List<Post> posts);

    Post getById(Integer postId);

    Post addLike(Integer postId, String username);

    Post createPost(PostRequestDto postRequestDto);
}
