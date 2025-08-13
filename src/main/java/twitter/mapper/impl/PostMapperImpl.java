package twitter.mapper.impl;

import twitter.configuration.Component;
import twitter.configuration.Injection;
import twitter.dto.v1.PostResponseDto;
import twitter.entity.post.Post;
import twitter.entity.user.Person;
import twitter.entity.user.User;
import twitter.exception.UserNotFoundException;
import twitter.mapper.PostMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class PostMapperImpl implements PostMapper {

    @Injection
    public PostMapperImpl() {
    }

    @Override
    public PostResponseDto mapToDto(Post post) throws UserNotFoundException {
        PostResponseDto result = new PostResponseDto();

        result.setCreatedAt(post.getCreationDate().toString());
        result.setTopic(post.getTopic());
        result.setText(post.getText());
        result.setTags("Тегов не найдено");

        if (post.getTags() != null && post.getTags().length > 0) {
            String tags = "";
            for (String tag : post.getTags()) {
                tags += " #" + tag + ",";
            }
            tags = tags.substring(1, tags.length() - 1);
            result.setTags(tags);
        }

        result.setAuthor(post.getAuthor().whatIsYourName());
        result.setLikesCount(post.getUsersWhoLiked().size());

        return result;
    }

    @Override
    public Post mapFileStringToPost(String postAsString) {
        Integer id = Integer.valueOf(postAsString.substring(1, postAsString.indexOf("}")));
        postAsString = postAsString.substring(postAsString.indexOf("}") + 1);

        Integer authorId = Integer.valueOf(postAsString.substring(1, postAsString.indexOf("}")));
        postAsString = postAsString.substring(postAsString.indexOf("}") + 1);

        String topic = postAsString.substring(1, postAsString.indexOf("}"));
        postAsString = postAsString.substring(postAsString.indexOf("}") + 1);

        String text = postAsString.substring(1, postAsString.indexOf("}"));
        postAsString = postAsString.substring(postAsString.indexOf("}") + 1);

        String[] tags = postAsString.substring(1, postAsString.indexOf("}")).split(",");
        postAsString = postAsString.substring(postAsString.indexOf("}") + 1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime createdAt = LocalDateTime.parse(postAsString.substring(1, postAsString.indexOf("}")), formatter);

        Post post = new Post();
        User user = new Person();
        user.setId(authorId);

        post.setId(id);
        post.setAuthor(user);
        post.setTopic(topic);
        post.setText(text);
        post.setTags(tags);
        post.setCreationDate(createdAt);
        return post;
    }

    @Override
    public Post mapUploadFileStringToPost(String postAsString) {
        Integer authorId = Integer.valueOf(postAsString.substring(1, postAsString.indexOf("}")));
        postAsString = postAsString.substring(postAsString.indexOf("}") + 1);

        String topic = postAsString.substring(1, postAsString.indexOf("}"));
        postAsString = postAsString.substring(postAsString.indexOf("}") + 1);

        String text = postAsString.substring(1, postAsString.indexOf("}"));
        postAsString = postAsString.substring(postAsString.indexOf("}") + 1);

        String[] tags = postAsString.substring(1, postAsString.indexOf("}")).split(",");

        Post post = new Post();
        User user = new Person();
        user.setId(authorId);

        post.setAuthor(user);
        post.setTopic(topic);
        post.setText(text);
        post.setTags(tags);
        return post;
    }
}
