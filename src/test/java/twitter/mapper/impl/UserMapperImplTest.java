package twitter.mapper.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import twitter.entity.user.Person;
import twitter.entity.user.User;
import twitter.entity.user.UserType;

class UserMapperImplTest {

    @Test
    public void mapUploadFileStringToUser_OK() {
        UserMapperImpl userMapper = new UserMapperImpl();
        final String userAsString = "{vadim}{12345}{PERSON}{Vadim}{Maidanov}{1995-04-01}";
        User user = userMapper.mapUploadFileStringToUser(userAsString);
        Person person = (Person) user;

        Assertions.assertEquals("vadim", person.getLogin());
        Assertions.assertEquals("12345", person.getPassword());
        Assertions.assertEquals("Vadim", person.getName());
        Assertions.assertEquals("Maidanov", person.getSurname());
        Assertions.assertEquals(1995, person.getBirthDate().getYear());
        Assertions.assertEquals(4, person.getBirthDate().getMonthValue());
        Assertions.assertEquals(1, person.getBirthDate().getDayOfMonth());
        Assertions.assertEquals(UserType.PERSON, person.getUserType());
    }

//    @Test
//    public void mapUploadFileStringToUser_DateOfBirthWrongFormat() {
//        UserMapperImpl userMapper = new UserMapperImpl();
//        final String userAsString = "{vadim}{12345}{PERSON}{Vadim}{Maidanov}{01.04.1995}";
//
//        Throwable ex = Assertions.assertThrows(IllegalArgumentException.class, () -> userMapper.mapUploadFileStringToUser(userAsString));
//        Assertions.assertEquals("Wrong date format", ex.getMessage());
//    }
}