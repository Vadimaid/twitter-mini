package twitter.service.impl;

import twitter.configuration.Component;
import twitter.configuration.Injection;
import twitter.entity.post.Post;
import twitter.entity.user.User;
import twitter.exception.TwitterUploadException;
import twitter.mapper.PostMapper;
import twitter.mapper.UserMapper;
import twitter.service.FileUploadService;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class FileUploadServiceImpl implements FileUploadService {

    @Injection
    public FileUploadServiceImpl(
            UserMapper userMapper,
            PostMapper postMapper
    ) {
        this.userMapper = userMapper;
        this.postMapper = postMapper;
    }

    private final UserMapper userMapper;
    private final PostMapper postMapper;

    @Override
    public List<User> uploadUsers(String filename) throws TwitterUploadException {
        if (Objects.isNull(filename) || filename.isBlank()) {
            throw new TwitterUploadException("Имя файла обязательно для заполнения");
        }

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
            List<String> newUsers = bufferedReader.lines().toList();
            if (newUsers.isEmpty()) {
                return Collections.emptyList();
            }

            List<User> users = new ArrayList<>();
            for (String line : newUsers) {
                if (Objects.nonNull(line) && !line.isBlank()) {
                    User user = this.userMapper.mapUploadFileStringToUser(line);
                    users.add(user);
                }
            }

            return users;

        } catch (FileNotFoundException ex) {
            throw new TwitterUploadException("Не найден файл с названием: " + filename);
        } catch (IOException ex) {
            throw new TwitterUploadException("Ошибки при чтении файла: " + filename + " " + ex.getMessage());
        }
    }

    @Override
    public List<Post> uploadPosts(String filename) throws TwitterUploadException {
        if (Objects.isNull(filename) || filename.isBlank()) {
            throw new TwitterUploadException("Имя файла обязательно для заполнения");
        }

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
            List<String> newPosts = bufferedReader.lines().toList();
            if (newPosts.isEmpty()) {
                return Collections.emptyList();
            }

            List<Post> posts = new ArrayList<>();
            for (String line : newPosts) {
                if (Objects.nonNull(line) && !line.isBlank()) {
                    Post post = this.postMapper.mapUploadFileStringToPost(line);
                    posts.add(post);
                }
            }

            return posts;

        } catch (FileNotFoundException ex) {
            throw new TwitterUploadException("Не найден файл с названием: " + filename);
        } catch (IOException ex) {
            throw new TwitterUploadException("Ошибки при чтении файла: " + filename + " " + ex.getMessage());
        }
    }

}
