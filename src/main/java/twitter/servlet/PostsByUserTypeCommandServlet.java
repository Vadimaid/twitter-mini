package twitter.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import twitter.configuration.ComponentFactory;
import twitter.controller.v2.PostController;
import twitter.dto.v2.response.PostResponseDto;
import twitter.exception.TwitterCommonException;

import java.io.IOException;
import java.util.List;

public class PostsByUserTypeCommandServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        String userTypeParam =  req.getParameter("user_type");

        try {
            if("0".equals(userTypeParam) ||  "1".equals(userTypeParam)){
                PostController postController = ComponentFactory.getComponent(PostController.class);
                List<PostResponseDto> responseDto = postController.getAllPostsByUserType(Integer.valueOf(userTypeParam));
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(mapper.writeValueAsString(responseDto));
                return;
            }
            throw new TwitterCommonException("Некорректный параметр для поиска публикаций.");

        } catch (TwitterCommonException ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(mapper.writeValueAsString(ex.getMessage()));
        } catch (Exception ex) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(mapper.writeValueAsString(ex.getMessage()));
        }
    }
}
