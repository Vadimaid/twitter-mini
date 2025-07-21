package twitter.dao.impl;

import twitter.configuration.Component;
import twitter.configuration.Injection;
import twitter.configuration.Profile;
import twitter.dao.UserDAO;
import twitter.entity.user.User;
import twitter.entity.user.UserType;
import twitter.exception.UnknownUserTypeException;
import twitter.exception.UserNotFoundException;
import twitter.mapper.UserMapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

//@Component
public class FIleUserDAO implements UserDAO {

    private final String filename = "UserData.txt";

    private final Set<User> users;
    private int id;

    private final UserMapper userMapper;

//    @Injection
    public FIleUserDAO(UserMapper userMapper) {
        this.users = new HashSet<>();
        this.userMapper = userMapper;
        this.init();
    }

    private void init() {
        try (
                BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))
        ) {
            List<String> lines = bufferedReader.lines().toList();
            for (String line : lines) {
                if (Objects.nonNull(line) && !line.isBlank()) {
                    User user = this.userMapper.mapFileStringToUser(line);
                    this.users.add(user);
                }
            }
            this.id = this.users.size() + 1;
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            System.exit(1);
        }
    }

    @Override
    public synchronized User saveNewUser(User user) {
        user.setId(this.id);
        user.setRegistrationDate(LocalDateTime.now());
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filename, true))) {
            bufferedWriter.newLine();
            bufferedWriter.append(user.toFileString());
            id++;
            this.users.add(user);
        } catch (IOException ex) {
            System.out.println("Не получилось создать пользователя. Причина: ");
            System.out.println(ex.getMessage());
        }
        return user;
    }

    @Override
    public synchronized User getById(int id) throws UserNotFoundException {
        if (this.users.isEmpty()) {
            throw new UserNotFoundException("Пользователь с ID: " + id + " не найден");
        }

        return this.users
                .stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("Пользователь с ID: " + id + " не найден"));
    }

    @Override
    public synchronized User getByLogin(String login) throws UserNotFoundException {
        if (this.users.isEmpty()) {
            throw new UserNotFoundException("Пользователь с логином: " + login + " не найден");
        }

        return this.users
                .stream()
                .filter(user -> user.getLogin().equals(login))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException("Пользователь с логином: " + login + " не найден"));
    }

    @Override
    public synchronized List<User> getAllUsers() {
        if (this.users.isEmpty()) {
            return Collections.emptyList();
        }
        return this.users.stream().toList();
    }

    @Override
    public synchronized List<User> getAllUsersByUserType(int userType) throws UnknownUserTypeException {
        if (this.users.isEmpty()) {
            return Collections.emptyList();
        }

        UserType type = UserType.getUserType(userType);
        return this.users
                .stream()
                .filter(user -> type.equals(user.getUserType()))
                .toList();
    }
}
