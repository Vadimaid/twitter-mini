package twitter.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import twitter.configuration.ComponentFactory;
import twitter.controller.v2.InfoController;
import twitter.dto.v2.response.InfoResponseDto;
import twitter.exception.TwitterCommonException;
import twitter.security.JwtHandler;

import java.io.IOException;
import java.util.List;

public class InfoAllCommandServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authorization = req.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
        String token = authorization.substring(7);

        JwtHandler jwtHandler = ComponentFactory.getComponent(JwtHandler.class);
        ObjectMapper mapper = new ObjectMapper();
        try {

            if (!jwtHandler.validateToken(token)) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            InfoController infoController = ComponentFactory.getComponent(InfoController.class);
            List<InfoResponseDto> respAllUsers = infoController.infoAll();

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().write(mapper.writeValueAsString(respAllUsers));

        } catch (TwitterCommonException ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(mapper.writeValueAsString(ex.getMessage()));
        } catch (Exception ex) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(mapper.writeValueAsString(ex.getMessage()));
        }
    }

}
