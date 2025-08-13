package twitter.dto.v1;

public class PostResponseDto {

    private String author; // user.whatIsYourName()
    private String createdAt; // post.getCreatedAt()
    private String topic; // post.getTopic()
    private String text; // post.getText()
    private String tags; // aggregation of post.getTags()
    private Integer likesCount;

    public PostResponseDto() {
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    @Override
    public String toString() {
        return String.format("Публикация: {\n    Автор: %s;\n    Создано: %s;\n    Тема: %s;\n    Текст: %s;\n    Теги: %s;\n    Количество лайков: %d;\n}",
                this.author, this.createdAt, this.topic, this.text, this.tags, this.likesCount);
    }
}
