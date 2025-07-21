package twitter.controller.impl;

import twitter.configuration.Component;
import twitter.configuration.Injection;
import twitter.controller.PostController;
import twitter.dto.PostResponseDto;
import twitter.entity.post.Post;
import twitter.entity.user.User;
import twitter.exception.TwitterUploadException;
import twitter.exception.UnknownUserTypeException;
import twitter.exception.UserNotFoundException;
import twitter.mapper.PostMapper;
import twitter.security.SecurityComponent;
import twitter.service.FileUploadService;
import twitter.service.PostService;
import twitter.service.UserService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

//@Component
public class PostControllerImpl implements PostController {
    
    private final PostService postService;
    private final SecurityComponent securityComponent;
    private final PostMapper postMapper;
    private final UserService userService;
    private final FileUploadService fileUploadService;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final String userIp;

//    @Injection
    public PostControllerImpl(
            PostService postService,
            SecurityComponent securityComponent,
            PostMapper postMapper,
            UserService userService,
            FileUploadService fileUploadService,
            BufferedReader in,
            BufferedWriter out, String userIp
    ) {
        this.postService = postService;
        this.securityComponent = securityComponent;
        this.postMapper = postMapper;
        this.userService = userService;
        this.fileUploadService = fileUploadService;
        this.in = in;
        this.out = out;
        this.userIp = userIp;
    }

    @Override
    public void executeReadPosts() throws IOException {
        if (this.securityComponent.getAuthentication(userIp) == null) {
            out.append("Для выполнения данной команды необходимо войти в систему.").append("\n");
            out.flush();
            return;
        }

        out.append("Файл для чтения: ");
        out.flush();
        String filename = in.readLine();
        out.append("Считывание публикаций...").append("\n");
        out.flush();

        Thread thread = new Thread(() -> {
            try {
                List<Post> newPosts = this.fileUploadService.uploadPosts(filename);
                List<Post> createdPosts = this.postService.createSeveralPosts(newPosts);

                out.append("Было добавлено " + createdPosts.size() + " публикаций").append("\n");
                out.flush();
            } catch (TwitterUploadException | IOException ex) {
                System.out.println(ex.getMessage());
            }
        });
        thread.start();
    }

    @Override
    public void executeAddPost() throws IOException {
        if (this.securityComponent.getAuthentication(userIp) == null) {
            out.append("Для выполнения данной команды необходимо войти в систему.").append("\n");
            out.flush();
            return;
        }

        out.append("<<<<<<  Создание новой публикации  >>>>>").append("\n");

        out.append("Введите тему публикации: ");
        out.flush();
        String topic = in.readLine();
        if (topic == null || topic.trim().isEmpty()) {
            out.append("Тема публикации не может быть пустой").append("\n");
            out.flush();
            return;
        }

        out.append("Введите текст публикации: ");
        out.flush();
        String text = in.readLine();
        if (text == null || text.trim().isEmpty()) {
            out.append("Текст публикации не может быть пустой").append("\n");
            out.flush();
            return;
        }

        out.append("Введите теги публикации(может быть пустым, для отделения тегов использовать ','): ");
        out.flush();
        String tags = in.readLine();

        Post post = new Post();

        User authenticatedUser = securityComponent.getAuthentication(userIp);
        post.setAuthor(authenticatedUser);

        post.setTopic(topic);
        post.setText(text);

        String[] tagArray = tags.split(",");
        post.setTags(tagArray);

        try {
            Post createdPost = postService.createPost(post);
            PostResponseDto responseDto = postMapper.mapToDto(createdPost);

            out.append(responseDto.toString()).append("\n");
            out.append("<<<<<<  Конец создания публикации  >>>>>").append("\n");
            out.flush();
        } catch (UserNotFoundException ex) {
            out.append(ex.getMessage()).append("\n");
            out.flush();
        }
    }

    @Override
    public void executeMyPosts() throws IOException {
        if (this.securityComponent.getAuthentication(userIp) == null) {
            out.append("Для выполнения данной команды необходимо войти в систему.").append("\n");
            out.flush();
            return;
        }

        out.append("<<<<<<  Мои публикации  >>>>>").append("\n");
        User authenticatedUser = securityComponent.getAuthentication(userIp);
        List<Post> posts = postService.getAllPostsByUser(authenticatedUser);
        try {
            for (Post post : posts) {
                PostResponseDto responseDto = postMapper.mapToDto(post);
                out.append(responseDto.toString()).append("\n");
            }
            out.append("<<<<<<  Конец моих публикаций  >>>>>").append("\n");
            out.flush();
        } catch (UserNotFoundException ex) {
            out.append(ex.getMessage()).append("\n");
            out.flush();
        }
    }

    @Override
    public void executeAllPosts() throws IOException {
        if (this.securityComponent.getAuthentication(userIp) == null) {
            out.append("Для выполнения данной команды необходимо войти в систему.").append("\n");
            out.flush();
            return;
        }

        out.append("<<<<<<  Все публикации  >>>>>").append("\n");
        List<Post> posts = postService.getAllPosts();
        try {
            for (Post post : posts) {
                PostResponseDto responseDto = postMapper.mapToDto(post);
                out.append(responseDto.toString()).append("\n");
            }
            out.append("<<<<<<  Конец всех публикаций  >>>>>").append("\n");
            out.flush();
        } catch (UserNotFoundException ex) {
            out.append(ex.getMessage()).append("\n");
            out.flush();
        }
    }

    @Override
    public void executePostsByTag() throws IOException {
        if (this.securityComponent.getAuthentication(userIp) == null) {
            out.append("Для выполнения данной команды необходимо войти в систему.").append("\n");
            out.flush();
            return;
        }

        out.append("Введите тег публикаций: ");
        out.flush();
        String tag = in.readLine();
        if (tag == null || tag.trim().isEmpty()) {
            out.append("Тег не может быть пустым").append("\n");
            out.flush();
            return;
        }
        try {
            out.append("<<<<<<  Все публикации по тегу >>>>>").append("\n");
            List<Post> posts = postService.getAllPostsByTag(tag);
            for (Post post : posts) {
                PostResponseDto responseDto = postMapper.mapToDto(post);
                out.append(responseDto.toString()).append("\n");
            }
            out.append("<<<<<<  Конец всех публикаций по тегу  >>>>>").append("\n");
            out.flush();
        } catch (UserNotFoundException ex) {
            out.append(ex.getMessage()).append("\n");
            out.flush();
        }
    }

    @Override
    public void executePostsByLogin() throws IOException {
        if (this.securityComponent.getAuthentication(userIp) == null) {
            out.append("Для выполнения данной команды необходимо войти в систему.").append("\n");
            out.flush();
            return;
        }

        out.append("Введите логин, чьи публикации показать: ");
        out.flush();
        String login = in.readLine();
        if (login == null || login.trim().isEmpty()) {
            out.append("Логин не может быть пустым").append("\n");
            out.flush();
            return;
        }
        login = login.trim();
        if (login.contains(" ")) {
            out.append("Логин не может содержать пробелы");
            out.flush();
            return;
        }

        try {
            User user = userService.getUserByLogin(login);

            out.append("<<<<<<  Публикации по логину  >>>>>").append("\n");
            List<Post> posts = postService.getAllPostsByUser(user);
            for (Post post : posts) {
                PostResponseDto responseDto = postMapper.mapToDto(post);
                out.append(responseDto.toString()).append("\n");
            }
            out.append("<<<<<<  Конец публикаций по логину  >>>>>").append("\n");
            out.flush();
        } catch (UserNotFoundException ex) {
            out.append(ex.getMessage()).append("\n");
            out.flush();
        }
    }

    @Override
    public void executePostsByUserType() throws IOException {
        if (this.securityComponent.getAuthentication(userIp) == null) {
            out.append("Для выполнения данной команды необходимо войти в систему.").append("\n");
            out.flush();
            return;
        }

        out.append("Введите тип пользователя (0 - человек, 1 - организация): ");
        out.flush();
        int userType = Integer.parseInt(in.readLine());
        if (userType != 0 && userType != 1) {
            out.append("Введен неверный тип пользователя.").append("\n");
            out.flush();
            return;
        }

        try {
            out.append("<<<<<<  Публикации по типу пользователя  >>>>>").append("\n");
            List<Post> posts = postService.getAllPostsByUserType(userType);
            for (Post post : posts) {
                PostResponseDto responseDto = postMapper.mapToDto(post);
                out.append(responseDto.toString()).append("\n");
            }
            out.append("<<<<<<  Конец публикаций по типу пользователя  >>>>>").append("\n");
            out.flush();
        } catch (UserNotFoundException | UnknownUserTypeException ex) {
            out.append(ex.getMessage()).append("\n");
            out.flush();
        }
    }
}