package twitter.controller.impl;

import twitter.configuration.Component;
import twitter.configuration.Injection;
import twitter.controller.RegistrationController;
import twitter.entity.user.Organization;
import twitter.entity.user.Person;
import twitter.entity.user.User;
import twitter.entity.user.UserType;
import twitter.exception.TwitterUploadException;
import twitter.exception.UnknownUserTypeException;
import twitter.security.SecurityComponent;
import twitter.service.FileUploadService;
import twitter.service.UserService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

//@Component
public class RegistrationControllerImpl implements RegistrationController {

    private final UserService userService;
    private final FileUploadService fileUploadService;
    private final SecurityComponent securityComponent;
    private final BufferedReader in;
    private final BufferedWriter out;
    private final String userIp;

//    @Injection
    public RegistrationControllerImpl(
            UserService userService,
            FileUploadService fileUploadService,
            SecurityComponent securityComponent,
            BufferedReader in,
            BufferedWriter out, String userIp
    ) {
        this.securityComponent = securityComponent;
        this.userService = userService;
        this.fileUploadService = fileUploadService;
        this.in = in;
        this.out = out;
        this.userIp = userIp;
    }

    @Override
    public void executeReadUsers() throws IOException {
        if (this.securityComponent.getAuthentication(userIp) == null) {
            out.append("Для выполнения данной команды необходимо войти в систему.").append("\n");
            out.flush();
            return;
        }

        out.append("Файл для чтения: ");
        out.flush();
        String filename = in.readLine();
        out.append("Считывание пользователей...").append("\n");
        out.flush();

        Thread thread = new Thread(() -> {
            try {
                List<User> newUsers = this.fileUploadService.uploadUsers(filename);
                List<User> createdUsers = this.userService.createSeveralUsers(newUsers);

                out.append("Всего пользователей в файле: ").append(String.valueOf(newUsers.size())).append(", из них были добавлены: ").append(String.valueOf(createdUsers.size())).append("\n");
                out.flush();
            } catch (TwitterUploadException | IOException ex) {
                System.out.println(ex.getMessage());
            }
        });
        thread.start();
    }

    @Override
    public void executeRegister() throws IOException {
        if (this.securityComponent.getAuthentication(userIp) != null) {
            out.append("Для выполнения данной команды необходимо выйти из системы.").append("\n");
            out.flush();
            return;
        }

        out.append("<<<<<<  Регистрация нового пользователя  >>>>>").append("\n");

        out.append("Введите тип пользователя (0 - человек, 1 - организация): ");
        out.flush();
        int userType = Integer.parseInt(in.readLine());
        if (userType != 0 && userType != 1) {
            out.append("Введен неверный тип пользователя.").append("\n");
            out.flush();
            return;
        }

        out.append("Введите логин: ");
        out.flush();
        String login = in.readLine();
        if (login == null || login.trim().isEmpty() || login.length() < 5) {
            out.append("Логин не может быть пустым или менее 5 символов").append("\n");
            out.flush();
            return;
        }
        login = login.trim();
        if (login.contains(" ")) {
            out.append("Логин не может содержать пробелы").append("\n");
            out.flush();
            return;
        }
        boolean isExists = userService.isUserExists(login);
        if (isExists) {
            out.append("Пользователь с таким логином уже существует.").append("\n");
            out.flush();
            return;
        }

        out.append("Введите пароль: ");
        out.flush();
        String password = in.readLine();
        if (password == null || password.trim().isEmpty() || password.length() < 5) {
            out.append("Пароль не может быть пустым или менее 5 символов").append("\n");
            out.flush();
            return;
        }
        password = password.trim();
        if (login.contains(" ")) {
            out.append("Пароль не может содержать пробелы").append("\n");
            out.flush();
            return;
        }

        User user = null;
        if (userType == 0) {
            user = registerPerson();
        } else {
            user = registerOrganization();
        }

        if (user == null) {
            return;
        }

        try {
            user.setLogin(login);
            user.setPassword(password);
            user.setUserType(UserType.getUserType(userType));

            User savedUser = userService.createUser(user);
            out.append("Новый пользователь: ").append("\n");
            out.append(savedUser.beautify()).append("\n");
            out.flush();
        } catch (UnknownUserTypeException ex) {
            out.append(ex.getMessage()).append("\n");
            out.flush();
        }
    }

    private User registerPerson() throws IOException {
        Person person = new Person();

        out.append("Введите имя: ");
        out.flush();
        String name = in.readLine();
        if (name == null || name.trim().isEmpty()) {
            out.append("Имя не может быть пустым").append("\n");
            out.flush();
            return null;
        }

        out.append("Введите фамилию: ");
        out.flush();
        String surname = in.readLine();
        if (surname == null || surname.trim().isEmpty()) {
            out.append("Фамилия не может быть пустой").append("\n");
            out.flush();
            return null;
        }

        out.append("Введите дату рождения (формат: гггг-мм-дд): ");
        out.flush();
        String dateOfBirth = in.readLine();
        if (dateOfBirth == null || dateOfBirth.trim().isEmpty()) {
            out.append("Дата рождения не может быть пустой").append("\n");
            out.flush();
            return null;
        }
        String regex = "^\\d{4}-\\d{2}-\\d{2}$";
        if (!dateOfBirth.matches(regex)) {
            out.append("Неверно введена дата рождения").append("\n");
            out.flush();
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDate = LocalDate.parse(dateOfBirth, formatter);
        if (birthDate.isAfter(LocalDate.now())) {
            out.append("Дата рождения не может быть в будущем").append("\n");
            out.flush();
            return null;
        }
        if (birthDate.isBefore(LocalDate.now().minusYears(100))) {
            out.append("Дата рождения не может настолько в прошлом").append("\n");
            out.flush();
            return null;
        }

        person.setName(name);
        person.setSurname(surname);
        person.setBirthDate(birthDate);

        return person;
    }

    private User registerOrganization() throws IOException {
        Organization organization = new Organization();

        out.append("Введите название: ");
        out.flush();
        String title = in.readLine();
        if (title == null || title.trim().isEmpty()) {
            out.append("Название не может быть пустым").append("\n");
            out.flush();
            return null;
        }

        out.append("Введите род деятельности: ");
        out.flush();
        String occupation = in.readLine();
        if (occupation == null || occupation.trim().isEmpty()) {
            out.append("Род деятельности не может быть пустым").append("\n");
            out.flush();
            return null;
        }

        out.append("Введите дату основания (формат: гггг-мм-дд): ");
        out.flush();
        String dateOfFoundation = in.readLine();
        if (dateOfFoundation == null || dateOfFoundation.trim().isEmpty()) {
            out.append("Дата основания не может быть пустой").append("\n");
            out.flush();
            return null;
        }
        String regex = "^\\d{4}-\\d{2}-\\d{2}$";
        if (!dateOfFoundation.matches(regex)) {
            out.append("Неверно введена дата основания").append("\n");
            out.flush();
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate foundationDate = LocalDate.parse(dateOfFoundation, formatter);
        if (foundationDate.isAfter(LocalDate.now())) {
            out.append("Дата основания не может быть в будущем").append("\n");
            out.flush();
            return null;
        }

        organization.setTitle(title);
        organization.setOccupation(occupation);
        organization.setDateOfFoundation(foundationDate);

        return organization;
    }
}