package twitter.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import twitter.configuration.ComponentFactory;
import twitter.controller.v2.PostsController;
import twitter.dto.v2.response.PostsResponseDto;
import twitter.exception.TwitterCommonException;


import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PostsByTagServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        resp.setContentType("application/json; charset=UTF-8");

        String tag = req.getParameter("tag");
        try {
            PostsController controller = ComponentFactory.getComponent(PostsController.class);
            List<PostsResponseDto> res = controller.postByTag(tag);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(mapper.writeValueAsString(res));
        }catch (TwitterCommonException ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(mapper.writeValueAsString(Map.of("error", ex.getMessage())));
        } catch (Exception ex) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(mapper.writeValueAsString(Map.of("error", ex.getMessage())));
        }
    }
}
