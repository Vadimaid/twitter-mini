package twitter.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import twitter.configuration.ComponentFactory;
import twitter.controller.v2.PostsController;
import twitter.dto.v2.request.PostsRequestDto;
import twitter.dto.v2.response.PostsResponseDto;
import twitter.exception.TwitterCommonException;
import twitter.security.JwtHandler;
import java.io.IOException;

public class AddPostCommandServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authorization = req.getHeader("Authorization");
        String token = authorization.substring(7);
        JwtHandler jwtHandler = ComponentFactory.getComponent(JwtHandler.class);
        ObjectMapper mapper = new ObjectMapper();
        PostsRequestDto requestDto = mapper.readValue(req.getInputStream(), PostsRequestDto.class);

        try {
            String username = jwtHandler.getUsernameFromToken(token);
            PostsController postController = ComponentFactory.getComponent(PostsController.class);
            PostsResponseDto responseDto = postController.addPost(requestDto, username);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(mapper.writeValueAsString(responseDto));

        } catch (TwitterCommonException ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(mapper.writeValueAsString(ex.getMessage()));
        } catch (Exception ex) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(mapper.writeValueAsString(ex.getMessage()));
        }
    }
}

