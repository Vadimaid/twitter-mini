package twitter.controller.v2.impl;

import twitter.configuration.Component;
import twitter.configuration.Injection;
import twitter.controller.v2.InfoController;
import twitter.dto.v2.response.InfoResponseDto;
import twitter.entity.user.Organization;
import twitter.entity.user.Person;
import twitter.entity.user.User;
import twitter.entity.user.UserType;
import twitter.exception.TwitterCommonException;
import twitter.exception.UserNotFoundException;
import twitter.service.UserService;

import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Component
public class InfoControllerImpl implements InfoController {

    private final UserService userService;

    @Injection
    public InfoControllerImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public InfoResponseDto info(String username) {
        if (Objects.isNull(username) || username.isBlank()) {
            throw new TwitterCommonException("Некорректные данные пользователя");
        }
        try {
            User user = this.userService.getUserByLogin(username);
            InfoResponseDto responseDto = new InfoResponseDto();
            responseDto.setId(user.getId());
            responseDto.setLogin(user.getLogin());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            if (UserType.PERSON.equals(user.getUserType())) {
                Person person = (Person) user;
                responseDto.setFirstName(person.getName());
                responseDto.setLastName(person.getSurname());
                responseDto.setDateOfBirth(person.getBirthDate().format(formatter));
            }

            if (UserType.ORGANIZATION.equals(user.getUserType())) {
                Organization organization = (Organization) user;
                responseDto.setTitle(organization.getTitle());
                responseDto.setOccupation(organization.getOccupation());
                responseDto.setDateOfFoundation(organization.getDateOfFoundation().format(formatter));
            }

            return responseDto;
        } catch (UserNotFoundException ex) {
            throw new TwitterCommonException(ex.getMessage());
        }
    }
}
