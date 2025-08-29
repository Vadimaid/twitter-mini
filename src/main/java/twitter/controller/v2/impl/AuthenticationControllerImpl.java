package twitter.controller.v2.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import twitter.configuration.Component;
import twitter.configuration.Injection;
import twitter.controller.v2.AuthenticationController;
import twitter.dto.v2.request.LoginRequestDto;
import twitter.dto.v2.response.LoginResponseDto;
import twitter.entity.user.User;
import twitter.exception.TwitterCommonException;
import twitter.exception.UserNotFoundException;
import twitter.security.JwtHandler;
import twitter.service.UserService;

import java.util.Objects;

@Component
public class AuthenticationControllerImpl implements AuthenticationController {

    private final UserService userService;
    private final JwtHandler jwtHandler;
    private final PasswordEncoder passwordEncoder;

    @Injection
    public AuthenticationControllerImpl(UserService userService, JwtHandler jwtHandler, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtHandler = jwtHandler;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public LoginResponseDto login(LoginRequestDto request) {
        if (Objects.isNull(request.getLogin()) || request.getLogin().isEmpty()) {
            throw new TwitterCommonException("Логин не может быть пустым");
        }
        if (request.getLogin().contains(" ")) {
            throw new TwitterCommonException("Логин не может содержать пробелы");
        }

        try {
            User user = userService.getUserByLogin(request.getLogin());
            if (!user.getPassword().equals(this.passwordEncoder.encode(request.getPassword()))) {
                throw new TwitterCommonException("Пароль введен неверно");
            }

            String token = this.jwtHandler.generateToken(request.getLogin());

            LoginResponseDto response = new LoginResponseDto();
            response.setToken(token);

            return response;
        } catch (UserNotFoundException ex) {
            throw new TwitterCommonException("Не найден пользователь с логином: " + request.getLogin());
        }
    }
}
