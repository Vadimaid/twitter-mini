package twitter.controller.v2.impl;

import twitter.configuration.Component;
import twitter.configuration.Injection;
import twitter.controller.v2.RegistrationController;
import twitter.dto.v2.request.RegisterRequestDto;
import twitter.dto.v2.response.InfoResponseDto;
import twitter.entity.user.Organization;
import twitter.entity.user.Person;
import twitter.entity.user.User;
import twitter.entity.user.UserType;
import twitter.exception.TwitterCommonException;
import twitter.exception.UnknownUserTypeException;
import twitter.service.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Component
public class RegistrationControllerImpl implements RegistrationController {

    private final UserService userService;

    @Injection
    public RegistrationControllerImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public InfoResponseDto register(RegisterRequestDto request) {
        if (request.getUserType() != 0 && request.getUserType() != 1) {
            throw new TwitterCommonException("Введен неверный тип пользователя.");
        }
        if (Objects.isNull(request.getLogin()) || request.getLogin().isEmpty() || request.getLogin().length() < 5) {
            throw new TwitterCommonException("Логин не может быть пустым или менее 5 символов");
        }
        if (userService.isUserExists(request.getLogin())) {
            throw new TwitterCommonException("Пользователь с таким логином уже существует.");
        }
        if (Objects.isNull(request.getPassword()) || request.getPassword().isEmpty() || request.getPassword().length() < 5) {
            throw new TwitterCommonException("Пароль не может быть пустым или менее 5 символов");
        }
        if (request.getPassword().contains(" ")) {
            throw new TwitterCommonException("Пароль не может содержать пробелы");
        }

        try {
            UserType userType = UserType.getUserType(request.getUserType());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            if (UserType.PERSON.equals(userType)) {
                Person user = new Person();
                validateField(request.getFirstName(), "Имя не может быть пустым");
                validateField(request.getLastName(), "Фамилия не может быть пустой");
                LocalDate birthDate = validateDate(request.getDateOfBirth(), "рождения");

                if (birthDate.isBefore(LocalDate.now().minusYears(100))) {
                    throw new TwitterCommonException("Дата рождения не может быть настолько в прошлом");
                }

                user.setName(request.getFirstName());
                user.setSurname(request.getLastName());
                user.setBirthDate(birthDate);
                user.setLogin(request.getLogin());
                user.setPassword(request.getPassword());
                user.setUserType(userType);

                User savedUser = userService.createUser(user);

                InfoResponseDto responseDto = new InfoResponseDto();
                responseDto.setId(savedUser.getId());
                responseDto.setLogin(savedUser.getLogin());
                responseDto.setFirstName(user.getName());
                responseDto.setLastName(user.getSurname());
                responseDto.setDateOfBirth(user.getBirthDate().format(formatter));
                return responseDto;

            } else {
                Organization user = new Organization();
                validateField(request.getTitle(), "Название не может быть пустым");
                validateField(request.getOccupation(), "Род деятельности не может быть пустым");
                LocalDate foundationDate = validateDate(request.getDateOfFoundation(), "основания");

                user.setTitle(request.getTitle());
                user.setOccupation(request.getOccupation());
                user.setDateOfFoundation(foundationDate);
                user.setLogin(request.getLogin());
                user.setPassword(request.getPassword());
                user.setUserType(userType);

                User savedUser = userService.createUser(user);

                InfoResponseDto responseDto = new InfoResponseDto();
                responseDto.setId(savedUser.getId());
                responseDto.setLogin(savedUser.getLogin());
                responseDto.setTitle(user.getTitle());
                responseDto.setOccupation(user.getOccupation());
                responseDto.setDateOfFoundation(user.getDateOfFoundation().format(formatter));
                return responseDto;
            }
        } catch (UnknownUserTypeException ex) {
            throw new TwitterCommonException(ex.getMessage());
        }
    }

    private void validateField(String field, String errorMessage) {
        if (Objects.isNull(field) || field.trim().isEmpty()) {
            throw new TwitterCommonException(errorMessage);
        }
    }

    private LocalDate validateDate(String date, String dateType) {
        if (Objects.isNull(date) || date.trim().isEmpty()) {
            throw new TwitterCommonException("Дата " + dateType + " не может быть пустой");
        }
        String regex = "^\\d{4}-\\d{2}-\\d{2}$";
        if (!date.matches(regex)) {
            throw new TwitterCommonException("Неверно введена дата " + dateType);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedDate = LocalDate.parse(date, formatter);
        if (parsedDate.isAfter(LocalDate.now())) {
            throw new TwitterCommonException("Дата " + dateType + " не может быть в будущем");
        }
        return parsedDate;
    }
}
