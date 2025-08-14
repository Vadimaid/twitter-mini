package twitter.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import twitter.configuration.ComponentFactory;
import twitter.controller.v2.PostController;
import twitter.dto.v2.request.PostRequestDto;
import twitter.dto.v2.response.PostResponseDto;
import twitter.exception.TwitterCommonException;
import twitter.security.JwtHandler;

import java.io.IOException;

public class AddPostCommandServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        PostRequestDto postRequestDto = mapper.readValue(req.getInputStream(), PostRequestDto.class);
        try {
            PostController postController = ComponentFactory.getComponent(PostController.class);

            String authorization = req.getHeader("Authorization");
            String token = authorization.substring(7);
            JwtHandler jwtHandler = ComponentFactory.getComponent(JwtHandler.class);
            String username = jwtHandler.getUsernameFromToken(token);
            postRequestDto.setAuthor(username);

            PostResponseDto postResponseDto = postController.addNewPost(postRequestDto);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(mapper.writeValueAsString(postResponseDto));
        } catch (TwitterCommonException ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(mapper.writeValueAsString(ex.getMessage()));
        } catch (Exception ex) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(mapper.writeValueAsString(ex.getMessage()));
        }
    }
}
