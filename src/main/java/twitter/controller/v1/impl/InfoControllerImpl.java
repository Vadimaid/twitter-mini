package twitter.controller.v1.impl;

import twitter.controller.v1.InfoController;
import twitter.entity.user.User;
import twitter.exception.UserNotFoundException;
import twitter.factory.command.TwitterCommandEnum;
import twitter.security.SecurityComponent;
import twitter.service.UserService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

//@Component
public class InfoControllerImpl implements InfoController {

    private final UserService userService;
    private final SecurityComponent securityComponent;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final String userIp;

//    @Injection
    public InfoControllerImpl(
            UserService userService,
            SecurityComponent securityComponent, BufferedReader in, BufferedWriter out, String userIp
    ) {
        this.userService = userService;
        this.securityComponent = securityComponent;
        this.in = in;
        this.out = out;
        this.userIp = userIp;
    }

    @Override
    public void executeHelp() throws IOException {
        out.write(TwitterCommandEnum.info());
        out.flush();
    }

    @Override
    public void executeInfo() throws IOException {
        if (this.securityComponent.getAuthentication(userIp) == null) {
            out.append("Для выполнения данной команды необходимо войти в систему.").append("\n");
            out.flush();
            return;
        }

        out.append("<<<<<<  Информация о пользователе  >>>>>").append("\n");
        out.append(securityComponent.getAuthentication(userIp).beautify()).append("\n");
        out.append("<<<<<<  Конец информации  >>>>>").append("\n");
        out.flush();
    }

    @Override
    public void executeInfoByLogin() throws IOException {
        if (this.securityComponent.getAuthentication(userIp) == null) {
            out.append("Для выполнения данной команды необходимо войти в систему.").append("\n");
            out.flush();
            return;
        }

        out.append("Введите логин пользователя для получения информации: ");
        out.flush();
        String login = in.readLine();
        if (login == null || login.trim().isEmpty()) {
            out.append("Логин не может быть пустым").append("\n");
            out.flush();
            return;
        }
        login = login.trim();
        if (login.contains(" ")) {
            out.append("Логин не может содержать пробелы").append("\n");
            out.flush();
            return;
        }
        try {
            User user = userService.getUserByLogin(login);

            out.append("<<<<<<  Информация о пользователе  >>>>>").append("\n");
            out.append(user.beautify()).append("\n");
            out.append("<<<<<<  Конец информации  >>>>>").append("\n");
            out.flush();
        } catch (UserNotFoundException ex) {
            out.append(ex.getMessage()).append("\n");
            out.flush();
        }
    }

    @Override
    public void executeInfoAll() throws IOException {
        if (this.securityComponent.getAuthentication(userIp) == null) {
            out.append("Для выполнения данной команды необходимо войти в систему.").append("\n");
            out.flush();
            return;
        }

        out.append("<<<<<<  Информация о всех пользователях системы  >>>>>").append("\n");
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            out.append(user.beautify()).append("\n");
            out.append("-----------------------------------------------------------------------------------------").append("\n");
        }
        out.append("<<<<<<  Конец информации  >>>>>").append("\n");
        out.flush();
    }
}