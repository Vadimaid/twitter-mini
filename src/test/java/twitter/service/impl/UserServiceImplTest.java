package twitter.service.impl;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import twitter.dao.UserDAO;
import twitter.entity.user.Person;
import twitter.entity.user.User;
import twitter.entity.user.UserType;
import twitter.exception.UserNotFoundException;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(value = {MockitoExtension.class})
class UserServiceImplTest {

    @Mock
    private UserDAO userDAO;

    @Spy
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        System.out.println("setUp");
    }

    @Test
    public void isUserExists_OK() throws Exception {
        final String login = "test";

        Mockito.when(userDAO.getByLogin("test")).thenReturn(new Person());

        UserServiceImpl userService = new UserServiceImpl(userDAO, passwordEncoder);
        boolean result = userService.isUserExists(login);

        Assertions.assertTrue(result);
    }

    @Test
    public void isUserExists_UserNotFoundException() throws Exception {
        final String login = "test";

        Mockito.when(userDAO.getByLogin("test")).thenThrow(new UserNotFoundException("Пользователь с логином: " + login + " не найден"));

        UserServiceImpl userService = new UserServiceImpl(userDAO, passwordEncoder);
        boolean result = userService.isUserExists(login);

        Assertions.assertFalse(result);
    }

    @Test
    public void getUserById_UserNotFoundException() throws Exception {
        final int id = 1;
        Mockito.when(userDAO.getById(id)).thenThrow(new UserNotFoundException("Пользователь с id: " + id + " не найден"));

        UserServiceImpl userService = new UserServiceImpl(userDAO, passwordEncoder);

        Throwable exception = Assertions.assertThrows(UserNotFoundException.class, () -> userService.getUserById(id));
        Assertions.assertEquals("Пользователь с id: 1 не найден", exception.getMessage());
    }

    @Test
    public void createUser_OK() {
        Person person = new Person();
        person.setLogin("vadim");
        person.setPassword("12345");
        person.setUserType(UserType.PERSON);

        User result = new Person();
        result.setId(1);
        result.setLogin("vadim");
        result.setPassword("$2a$04$XQecoL1IBcbaaSReAwNrxec5ek9AzglDUIvaR1CQ.Sb27ynr/NJPG");
        result.setUserType(UserType.PERSON);

        Mockito.when(userDAO.saveNewUser(any(Person.class))).thenReturn(result);

        UserServiceImpl userService = new UserServiceImpl(userDAO, passwordEncoder);
        User createdUser = userService.createUser(person);

        Assertions.assertNotNull(createdUser);
        Assertions.assertEquals(1, createdUser.getId());
        Assertions.assertEquals(person.getLogin(), createdUser.getLogin());
        Assertions.assertEquals("$2a$04$XQecoL1IBcbaaSReAwNrxec5ek9AzglDUIvaR1CQ.Sb27ynr/NJPG", createdUser.getPassword());
        Assertions.assertEquals(person.getUserType(), createdUser.getUserType());
    }

}