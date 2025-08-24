package twitter.mapper.v2.impl;
import twitter.configuration.Component;
import twitter.configuration.Injection;
import twitter.dto.v2.response.PostsResponseDto;
import twitter.entity.post.Post;
import twitter.exception.UserNotFoundException;

@Component
public class PostsMapper {

    @Injection
    public PostsMapper() {
    }

    public PostsResponseDto mapForDto(Post post) throws UserNotFoundException {
        PostsResponseDto result = new PostsResponseDto();

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

}
