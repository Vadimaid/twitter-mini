package twitter.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import twitter.configuration.ComponentFactory;
import twitter.controller.v2.impl.PostsControllerImpl;
import twitter.dto.v2.response.PostsResponseDto;
import twitter.exception.TwitterCommonException;
import twitter.security.JwtHandler;

import java.io.IOException;
import java.util.List;

public class MyPostsCommandServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authorization = req.getHeader("Authorization");
        String token = authorization.substring(7);

        JwtHandler jwtHandler = ComponentFactory.getComponent(JwtHandler.class);
        ObjectMapper mapper = new ObjectMapper();

        try {
            String username = jwtHandler.getUsernameFromToken(token);
            PostsControllerImpl controller = ComponentFactory.getComponent(PostsControllerImpl.class);
            List<PostsResponseDto> response = controller.myPosts(username);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(mapper.writeValueAsString(response));



        }catch (TwitterCommonException ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(mapper.writeValueAsString(ex.getMessage()));
        } catch (Exception ex) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(mapper.writeValueAsString(ex.getMessage()));
        }

    }
}
