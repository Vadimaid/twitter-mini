package twitter.factory;

import twitter.configuration.Component;
import twitter.configuration.Injection;
import twitter.controller.v1.AuthenticationController;
import twitter.controller.v1.InfoController;
import twitter.controller.v1.RegistrationController;
import twitter.controller.v1.impl.AuthenticationControllerImpl;
import twitter.controller.v1.impl.InfoControllerImpl;
import twitter.controller.v1.impl.PostControllerImpl;
import twitter.controller.v1.impl.RegistrationControllerImpl;
import twitter.factory.command.CommandFactoryImpl;
import twitter.mapper.PostMapper;
import twitter.security.SecurityComponent;
import twitter.service.FileUploadService;
import twitter.service.PostService;
import twitter.service.UserService;

import java.io.BufferedReader;
import java.io.BufferedWriter;

@Component
public class CommandFactoryBuilder {

    private final UserService userService;
    private final SecurityComponent securityComponent;
    private final PostService postService;
    private final PostMapper postMapper;
    private final FileUploadService fileUploadService;

    @Injection
    public CommandFactoryBuilder(
            UserService userService,
            SecurityComponent securityComponent,
            PostService postService,
            PostMapper postMapper,
            FileUploadService fileUploadService
    ) {
        this.userService = userService;
        this.securityComponent = securityComponent;
        this.postService = postService;
        this.postMapper = postMapper;
        this.fileUploadService = fileUploadService;
    }

    public CommandFactory buildCommandFactoryForUser(String userIp, BufferedReader reader, BufferedWriter writer) {
        AuthenticationController authenticationController = new AuthenticationControllerImpl(userService, securityComponent, reader, writer, userIp);
        InfoController infoController = new InfoControllerImpl(userService, securityComponent, reader, writer, userIp);
        PostControllerImpl postController = new PostControllerImpl(postService, securityComponent, postMapper, userService, fileUploadService, reader, writer, userIp);
        RegistrationController registrationController = new RegistrationControllerImpl(userService, fileUploadService, securityComponent, reader, writer, userIp);
        return new CommandFactoryImpl(authenticationController, registrationController, infoController, postController);
    }
}
