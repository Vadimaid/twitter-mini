package twitter.controller.v2.impl;

import twitter.configuration.Component;
import twitter.configuration.Injection;
import twitter.controller.v2.InfoController;
import twitter.dto.v2.response.InfoResponseDto;
import twitter.entity.user.User;
import twitter.exception.TwitterCommonException;
import twitter.exception.UserNotFoundException;
import twitter.mapper.v2.HttpUserMapper;
import twitter.service.UserService;

import java.util.List;
import java.util.Objects;

@Component
public class InfoControllerImpl implements InfoController {

    private final UserService userService;
    private final HttpUserMapper httpUserMapper;

    @Injection
    public InfoControllerImpl(UserService userService, HttpUserMapper httpUserMapper) {
        this.userService = userService;
        this.httpUserMapper = httpUserMapper;
    }

    @Override
    public InfoResponseDto info(String username) {
        if (Objects.isNull(username) || username.isBlank()) {
            throw new TwitterCommonException("Некорректные данные пользователя");
        }
        try {
            User user = this.userService.getUserByLogin(username);
            return httpUserMapper.mapUserToInfoResponseDto(user);
        } catch (UserNotFoundException ex) {
            throw new TwitterCommonException(ex.getMessage());
        }
    }

    @Override
    public List<InfoResponseDto> infoAll() {
        return this.userService.getAllUsers().stream()
                .map(httpUserMapper::mapUserToInfoResponseDto)
                .toList();
    }

    @Override
    public InfoResponseDto infoByLogin(String login) {
        if (Objects.isNull(login) || login.isBlank()) {
            throw new TwitterCommonException("Некорректный логин");
        }
        try {
            User user = this.userService.getUserByLogin(login);
            return httpUserMapper.mapUserToInfoResponseDto(user);
        } catch (UserNotFoundException ex) {
            throw new TwitterCommonException(ex.getMessage());
        }
    }
}
