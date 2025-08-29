package twitter.dao;

import twitter.entity.post.Post;

import java.util.List;

public interface PostDAO {

    Post saveNewPost(Post post);

    List<Post> getAllPosts();
    List<Post> getAllPostsByUser(int userId);
    List<Post> getAllPostsByTag(String tag);
    List<Post> getAllPostsByUserIdIn(int[] userIds);
    Post getPostById(int postId);

}
