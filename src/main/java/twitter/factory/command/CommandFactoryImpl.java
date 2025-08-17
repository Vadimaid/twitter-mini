package twitter.factory.command;

import twitter.controller.v1.AuthenticationController;
import twitter.controller.v1.InfoController;
import twitter.controller.v1.PostController;
import twitter.controller.v1.RegistrationController;
import twitter.exception.UnknownCommandException;
import twitter.factory.CommandFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//@Component
public class CommandFactoryImpl implements CommandFactory {

    private final Map<TwitterCommandEnum, CommandHandler> factory;

    private final AuthenticationController authenticationController;
    private final RegistrationController registrationController;
    private final InfoController infoController;
    private final PostController postController;

//    @Injection
    public CommandFactoryImpl(
            AuthenticationController authenticationController,
            RegistrationController registrationController,
            InfoController infoController,
            PostController postController
    ) {
        this.factory = new HashMap<>();
        this.authenticationController = authenticationController;
        this.registrationController = registrationController;
        this.infoController = infoController;
        this.postController = postController;
        this.init();
    }

    private void init() {
        this.factory.put(TwitterCommandEnum.EXIT_COMMAND, () -> {
            try {
                authenticationController.executeExit();
            } catch (IOException e) {
                System.out.println("Ошибка при выполнении команды выхода: " + e.getMessage());
            }
        });
        this.factory.put(TwitterCommandEnum.HELP_COMMAND, () -> {
            try {
                infoController.executeHelp();
            } catch (IOException e) {
                System.out.println("Ошибка при выполнении команды помощи: " + e.getMessage());
            }
        });
        this.factory.put(TwitterCommandEnum.REGISTER_COMMAND, () -> {
            try {
                registrationController.executeRegister();
            } catch (IOException e) {
                System.out.println("Ошибка при выполнении регистрации: " + e.getMessage());
            }
        });
        this.factory.put(TwitterCommandEnum.LOGIN_COMMAND, () -> {
            try {
                authenticationController.executeLogin();
            } catch (IOException e) {
                System.out.println("Ошибка при выполнении входа: " + e.getMessage());
            }
        });
        this.factory.put(TwitterCommandEnum.LOGOUT_COMMAND, () -> {
            try {
                authenticationController.executeLogout();
            } catch (IOException e) {
                System.out.println("Ошибка при выполнении выхода: " + e.getMessage());
            }
        });
        this.factory.put(TwitterCommandEnum.INFO_COMMAND, () -> {
            try {
                infoController.executeInfo();
            } catch (IOException e) {
                System.out.println("Ошибка при получении информации: " + e.getMessage());
            }
        });
        this.factory.put(TwitterCommandEnum.INFO_BY_LOGIN_COMMAND, () -> {
            try {
                infoController.executeInfoByLogin();
            } catch (IOException e) {
                System.out.println("Ошибка при получении информации по логину: " + e.getMessage());
            }
        });
        this.factory.put(TwitterCommandEnum.INFO_ALL_COMMAND, () -> {
            try {
                infoController.executeInfoAll();
            } catch (IOException e) {
                System.out.println("Ошибка при получении общей информации: " + e.getMessage());
            }
        });
        this.factory.put(TwitterCommandEnum.ADD_POST_COMMAND, () -> {
            try {
                postController.executeAddPost();
            } catch (IOException e) {
                System.out.println("Ошибка при добавлении поста: " + e.getMessage());
            }
        });
        this.factory.put(TwitterCommandEnum.MY_POSTS_COMMAND, () -> {
            try {
                postController.executeMyPosts();
            } catch (IOException e) {
                System.out.println("Ошибка при получении ваших постов: " + e.getMessage());
            }
        });
        this.factory.put(TwitterCommandEnum.ALL_POSTS_COMMAND, () -> {
            try {
                postController.executeAllPosts();
            } catch (IOException e) {
                System.out.println("Ошибка при получении всех постов: " + e.getMessage());
            }
        });
        this.factory.put(TwitterCommandEnum.POSTS_BY_TAG_COMMAND, () -> {
            try {
                postController.executePostsByTag();
            } catch (IOException e) {
                System.out.println("Ошибка при поиске постов по тегу: " + e.getMessage());
            }
        });
        this.factory.put(TwitterCommandEnum.POSTS_BY_LOGIN_COMMAND, () -> {
            try {
                postController.executePostsByLogin();
            } catch (IOException e) {
                System.out.println("Ошибка при поиске постов по логину: " + e.getMessage());
            }
        });
        this.factory.put(TwitterCommandEnum.POSTS_BY_USER_TYPE_COMMAND, () -> {
            try {
                postController.executePostsByUserType();
            } catch (IOException e) {
                System.out.println("Ошибка при поиске постов по типу пользователя: " + e.getMessage());
            }
        });
        this.factory.put(TwitterCommandEnum.READ_USERS_COMMAND, () -> {
            try {
                registrationController.executeReadUsers();
            } catch (IOException e) {
                System.out.println("Ошибка при чтении пользователей: " + e.getMessage());
            }
        });
        this.factory.put(TwitterCommandEnum.READ_POSTS_COMMAND, () -> {
            try {
                postController.executeReadPosts();
            } catch (IOException e) {
                System.out.println("Ошибка при чтении постов: " + e.getMessage());
            }
        });
    }

    @Override
    public CommandHandler getHandler(String command) throws UnknownCommandException {
        if (!this.factory.containsKey(TwitterCommandEnum.getCommandByCommand(command))) {
            throw new UnknownCommandException("Команда " + command + " не распознана");
        }
        return this.factory.get(TwitterCommandEnum.getCommandByCommand(command));
    }
}
