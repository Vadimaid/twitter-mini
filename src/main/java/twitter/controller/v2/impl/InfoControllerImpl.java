package twitter.controller.v2.impl;

import twitter.configuration.Component;
import twitter.configuration.Injection;
import twitter.controller.v2.InfoController;
import twitter.dto.v2.response.InfoResponseDto;
import twitter.entity.user.User;
import twitter.exception.TwitterCommonException;
import twitter.exception.UserNotFoundException;
import twitter.mapper.ServletUserMapper;
import twitter.service.UserService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
            return ServletUserMapper.mapEntityToDtoResponse(user);
        } catch (UserNotFoundException ex) {
            throw new TwitterCommonException(ex.getMessage());
        }
    }

    @Override
    public List<InfoResponseDto> infoAll() {
        return this.userService.getAllUsers().stream().map(
          ServletUserMapper::mapEntityToDtoResponse
        ).collect(Collectors.toList());
    }

    @Override
    public InfoResponseDto infoByLogin(String login) {
        if(Objects.isNull(login) || login.isBlank()) {
            throw new TwitterCommonException("Параметр для поиска не может быть пустым");
        }
        try {
            User user = userService.getUserByLogin(login);
            return ServletUserMapper.mapEntityToDtoResponse(user);
        } catch (UserNotFoundException e) {
            throw new TwitterCommonException(e.getMessage());
        }
    }
}
