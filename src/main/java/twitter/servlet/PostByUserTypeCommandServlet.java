package twitter.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import twitter.configuration.ComponentFactory;
import twitter.controller.v2.PostController;
import twitter.controller.v2.impl.PostControllerImpl;
import twitter.dao.PostDAO;
import twitter.dto.v1.PostResponseDto;
import twitter.entity.user.User;
import twitter.entity.user.UserType;
import twitter.exception.UnknownUserTypeException;
import twitter.security.JwtHandler;
import twitter.service.PostService;
import twitter.service.UserService;
import twitter.service.impl.PostServiceImpl;

import java.io.IOException;
import java.util.List;

public class PostByUserTypeCommandServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authorization = req.getHeader("Authorization");
        String token = authorization.substring(7);

        JwtHandler jwtHandler = ComponentFactory.getComponent(JwtHandler.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try{
            String username = jwtHandler.getUsernameFromToken(token);
            UserService userService = ComponentFactory.getComponent(UserService.class);
            PostDAO postDAO = ComponentFactory.getComponent(PostDAO.class);
            User user = userService.getUserByLogin(username);
            int userType = (user.getUserType() == UserType.PERSON) ? 0 : 1;

            PostService postService = new PostServiceImpl(postDAO, userService);
            PostController postController = new PostControllerImpl(postService);
            List<PostResponseDto> posts = postController.postsByUserType(userType);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(posts));
        }catch (UnknownUserTypeException ex){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(objectMapper.writeValueAsString(ex.getMessage()));
        }catch (Exception ex){
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(objectMapper.writeValueAsString(ex.getMessage()));
        }
    }
}
