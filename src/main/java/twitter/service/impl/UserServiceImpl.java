package twitter.service.impl;

import twitter.configuration.Component;
import twitter.configuration.Injection;
import twitter.dao.UserDAO;
import twitter.entity.user.User;
import twitter.exception.UnknownUserTypeException;
import twitter.exception.UserNotFoundException;
import twitter.service.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class UserServiceImpl implements UserService {
    
    private final UserDAO userDAO;

    @Injection
    public UserServiceImpl(
            UserDAO userDAO
    ) {
        this.userDAO = userDAO;
    }

    @Override
    public boolean isUserExists(String login) {
        try {
            User user = this.getUserByLogin(login);
            return Objects.nonNull(user);
        } catch (UserNotFoundException ex) {
            return false;
        }
    }

    @Override
    public User createUser(User user) {
        return userDAO.saveNewUser(user);
    }

    @Override
    public User getUserById(int id) throws UserNotFoundException {
        return userDAO.getById(id);
    }

    @Override
    public User getUserByLogin(String login) throws UserNotFoundException {
        return userDAO.getByLogin(login);
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    public List<User> getUsersByType(int userType) throws UnknownUserTypeException {
        return userDAO.getAllUsersByUserType(userType);
    }

    @Override
    public List<User> createSeveralUsers(List<User> users) {
        if (Objects.isNull(users) || users.isEmpty()) {
            return Collections.emptyList();
        }

        List<User> createdUsers = new ArrayList<>();
        for (User user : users) {
            if (!isUserExists(user.getLogin())) {
                User createdUser = createUser(user);
                createdUsers.add(createdUser);
            }
        }
        
        return createdUsers;
    }

}
