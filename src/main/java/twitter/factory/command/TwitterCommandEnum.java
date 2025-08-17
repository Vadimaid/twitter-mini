package twitter.factory.command;

public enum TwitterCommandEnum {

    EXIT_COMMAND("exit", "Выход из системы"),
    HELP_COMMAND("help", "Помощь по командам в системе"),
    REGISTER_COMMAND("register", "Регистрация нового пользователя"),
    LOGIN_COMMAND("login", "Аутентификация пользователя"),
    LOGOUT_COMMAND("logout", "Выход из аутентификации"),
    INFO_COMMAND("info", "Вывод информации об аутентифицированном пользователе"),
    INFO_BY_LOGIN_COMMAND("info_by_login", "Вывод информации о пользователе по его логину"),
    INFO_ALL_COMMAND("info_all", "Вывод информации о всех пользователях системы"),
    ADD_POST_COMMAND("add_post", "Добавление новой публикации"),
    MY_POSTS_COMMAND("my_posts", "Показать мои публикации"),
    ALL_POSTS_COMMAND("all_posts", "Показать все публикации"),
    POSTS_BY_TAG_COMMAND("posts_by_tag", "Показать публикации по тегу"),
    POSTS_BY_LOGIN_COMMAND("posts_by_login", "Показать публикации по логину пользователя"),
    POSTS_BY_USER_TYPE_COMMAND("posts_by_user_type", "Показать публикации по типу пользователя"),
    READ_USERS_COMMAND("read_users", "Зарегистрировать новых пользователей из файла"),
    READ_POSTS_COMMAND("read_posts", "Опубликовать новые публикации из файла");

    private final String command;
    private final String description;

    TwitterCommandEnum(String command, String description) {
        this.command = command;
        this.description = description;
    }

    public static TwitterCommandEnum getCommandByCommand(String command) {
        for (TwitterCommandEnum twitterCommandEnum : TwitterCommandEnum.values()) {
            if (twitterCommandEnum.command.equals(command)) {
                return twitterCommandEnum;
            }
        }
        return null;
    }

    public static String info() {
        StringBuilder stringBuilder = new StringBuilder();
        for (TwitterCommandEnum twitterCommandEnum : TwitterCommandEnum.values()) {
            stringBuilder.append(twitterCommandEnum.command).append(" - ").append(twitterCommandEnum.description).append("\n");
        }
        return stringBuilder.toString();
    }
}
