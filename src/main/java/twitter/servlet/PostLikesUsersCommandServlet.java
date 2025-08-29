package twitter.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import twitter.configuration.ComponentFactory;
import twitter.controller.v2.PostsController;
import twitter.dto.v2.response.InfoResponseDto;
import twitter.exception.TwitterCommonException;
import twitter.security.JwtHandler;

import java.io.IOException;
import java.util.List;

public class PostLikesUsersCommandServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String authorization = req.getHeader("Authorization");
        String token = authorization.substring(7);
        JwtHandler jwtHandler = ComponentFactory.getComponent(JwtHandler.class);
        if (!jwtHandler.validateToken(token)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().println("Invalid token");
        }

        int postId = Integer.parseInt(req.getParameter("post"));
        ObjectMapper mapper = new ObjectMapper();

        try {
            PostsController postController = ComponentFactory.getComponent(PostsController.class);
            List<InfoResponseDto> users = postController.getPostUsersLikes(postId);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(mapper.writeValueAsString(users));
        } catch (TwitterCommonException ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(mapper.writeValueAsString(ex.getMessage()));
        } catch (Exception ex) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(mapper.writeValueAsString(ex.getMessage()));
        }
    }
}
