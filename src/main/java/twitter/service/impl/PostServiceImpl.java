package twitter.service.impl;

import twitter.configuration.Component;
import twitter.configuration.Injection;
import twitter.dao.PostDAO;
import twitter.dto.v2.request.PostRequestDto;
import twitter.entity.post.Post;
import twitter.entity.user.User;
import twitter.exception.TwitterCommonException;
import twitter.exception.UnknownUserTypeException;
import twitter.exception.UserNotFoundException;
import twitter.service.PostService;
import twitter.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Component
public class PostServiceImpl implements PostService {

    private final PostDAO postDAO;
    private final UserService userService;

    @Injection
    public PostServiceImpl(
            PostDAO postDAO,
            UserService userService
    ) {
        this.postDAO = postDAO;
        this.userService = userService;
    }

    @Override
    public Post createPost(Post post) {
        return this.postDAO.saveNewPost(post);
    }

    @Override
    public List<Post> getAllPostsByUser(User user) {
        List<Post> userPosts = postDAO.getAllPostsByUser(user.getId());
        if (userPosts.isEmpty()) {
            return List.of();
        }
        List<Post> result = new java.util.ArrayList<>();
        for (int i = userPosts.size() - 1; i >= 0; i--) {
            result.add(userPosts.get(i));
        }

        return result;
    }

    @Override
    public List<Post> getAllPostsByUser(String username) {
        try {
            User user = userService.getUserByLogin(username);
            return getAllPostsByUser(user);
        } catch (UserNotFoundException e) {
            throw new TwitterCommonException(e.getMessage());
        }
    }

    @Override
    public List<Post> getAllPostsByUser(Integer userId) {
        try {
            User user = userService.getUserById(userId);
            return getAllPostsByUser(user);
        } catch (UserNotFoundException e) {
            throw new TwitterCommonException(e.getMessage());
        }
    }

    @Override
    public List<Post> getAllPosts() {
        return postDAO.getAllPosts();
    }

    @Override
    public List<Post> getAllPostsByTag(String tag) {
        return postDAO.getAllPostsByTag(tag);
    }

    @Override
    public List<Post> getAllPostsByUserType(int userType) throws UnknownUserTypeException {
        List<User> usersByType = userService.getUsersByType(userType);
        if (usersByType.isEmpty()) {
            return List.of();
        }

        int[] usersIds = new int[usersByType.size()];
        for (int i = 0; i < usersByType.size(); i++) {
            usersIds[i] = usersByType.get(i).getId();
        }

        return postDAO.getAllPostsByUserIdIn(usersIds);
    }

    @Override
    public List<Post> createSeveralPosts(List<Post> posts) {
        if (Objects.isNull(posts) || posts.isEmpty()) {
            return List.of();
        }

        for (Post post : posts) {
            createPost(post);
        }

        return posts;
    }

    @Override
    public Post getById(Integer postId) {
        return postDAO.getPostById(postId);
    }

    @Override
    public Post addLike(Integer postId, String username) {


        try {
            return  postDAO.addLike( postId, userService.getUserByLogin(username).getId());
        } catch (UserNotFoundException e) {
            throw new TwitterCommonException(e.getMessage());
        }

    }

    @Override
    public Post createPost(PostRequestDto postRequestDto) {
        Post post = new Post();
        post.setTopic(postRequestDto.getTopic());
        post.setText(postRequestDto.getText());
        String[] tagArray = postRequestDto.getTags().split(",");
        post.setTags(tagArray);
        post.setCreationDate(LocalDateTime.now());
        try {
            User author = userService.getUserByLogin(postRequestDto.getAuthor());
            post.setAuthor(author);
        } catch (UserNotFoundException e) {
            throw new TwitterCommonException("Автор публикации не найден в системе");
        }
        return createPost(post);
    }


}
