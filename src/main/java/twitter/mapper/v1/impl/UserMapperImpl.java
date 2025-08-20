package twitter.mapper.v1.impl;

import twitter.configuration.Component;
import twitter.configuration.Injection;
import twitter.entity.user.Organization;
import twitter.entity.user.Person;
import twitter.entity.user.User;
import twitter.entity.user.UserType;
import twitter.mapper.v1.UserMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class UserMapperImpl implements UserMapper {

    @Injection
    public UserMapperImpl() {}

    @Override
    public User mapUploadFileStringToUser(String userAsString) {
        String login = userAsString.substring(1, userAsString.indexOf("}"));
        userAsString = userAsString.substring(userAsString.indexOf("}") + 1);
        String password = userAsString.substring(1, userAsString.indexOf("}"));
        userAsString = userAsString.substring(userAsString.indexOf("}") + 1);
        UserType userType = UserType.valueOf(userAsString.substring(1, userAsString.indexOf("}")));
        userAsString = userAsString.substring(userAsString.indexOf("}") + 1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (UserType.PERSON.equals(userType)) {
            Person person = new Person();
            person.setLogin(login);
            person.setPassword(password);
            person.setUserType(userType);
            String name = userAsString.substring(1, userAsString.indexOf("}"));
            userAsString = userAsString.substring(userAsString.indexOf("}") + 1);
            person.setName(name);
            String surname = userAsString.substring(1, userAsString.indexOf("}"));
            userAsString = userAsString.substring(userAsString.indexOf("}") + 1);
            person.setSurname(surname);
            LocalDate dateOfBirth = LocalDate.parse(userAsString.substring(1, userAsString.indexOf("}")), formatter);
            person.setBirthDate(dateOfBirth);
            return person;
        }

        Organization organization = new Organization();
        organization.setLogin(login);
        organization.setPassword(password);
        organization.setUserType(userType);
        String title = userAsString.substring(1, userAsString.indexOf("}"));
        userAsString = userAsString.substring(userAsString.indexOf("}") + 1);
        organization.setTitle(title);
        String occupation = userAsString.substring(1, userAsString.indexOf("}"));
        userAsString = userAsString.substring(userAsString.indexOf("}") + 1);
        organization.setOccupation(occupation);
        LocalDate foundationDate = LocalDate.parse(userAsString.substring(1, userAsString.indexOf("}")), formatter);
        organization.setDateOfFoundation(foundationDate);
        return organization;
    }

    @Override
    public User mapFileStringToUser(String userAsString) {
        Integer id = Integer.valueOf(userAsString.substring(1, userAsString.indexOf("}")));
        userAsString = userAsString.substring(userAsString.indexOf("}") + 1);
        String login = userAsString.substring(1, userAsString.indexOf("}"));
        userAsString = userAsString.substring(userAsString.indexOf("}") + 1);
        String password = userAsString.substring(1, userAsString.indexOf("}"));
        userAsString = userAsString.substring(userAsString.indexOf("}") + 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime registrationDate = LocalDateTime.parse(userAsString.substring(1, userAsString.indexOf("}")), formatter);
        userAsString = userAsString.substring(userAsString.indexOf("}") + 1);
        UserType userType = UserType.valueOf(userAsString.substring(1, userAsString.indexOf("}")));
        userAsString = userAsString.substring(userAsString.indexOf("}") + 1);

        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (UserType.PERSON.equals(userType)) {
            Person person = new Person();
            person.setId(id);
            person.setLogin(login);
            person.setPassword(password);
            person.setRegistrationDate(registrationDate);
            person.setUserType(userType);
            String name = userAsString.substring(1, userAsString.indexOf("}"));
            userAsString = userAsString.substring(userAsString.indexOf("}") + 1);
            person.setName(name);
            String surname = userAsString.substring(1, userAsString.indexOf("}"));
            userAsString = userAsString.substring(userAsString.indexOf("}") + 1);
            person.setSurname(surname);
            LocalDate dateOfBirth = LocalDate.parse(userAsString.substring(1, userAsString.indexOf("}")), formatter);
            person.setBirthDate(dateOfBirth);
            return person;
        }

        Organization organization = new Organization();
        organization.setId(id);
        organization.setLogin(login);
        organization.setPassword(password);
        organization.setRegistrationDate(registrationDate);
        organization.setUserType(userType);
        String title = userAsString.substring(1, userAsString.indexOf("}"));
        userAsString = userAsString.substring(userAsString.indexOf("}") + 1);
        organization.setTitle(title);
        String occupation = userAsString.substring(1, userAsString.indexOf("}"));
        userAsString = userAsString.substring(userAsString.indexOf("}") + 1);
        organization.setOccupation(occupation);
        LocalDate foundationDate = LocalDate.parse(userAsString.substring(1, userAsString.indexOf("}")), formatter);
        organization.setDateOfFoundation(foundationDate);
        return organization;
    }
}
