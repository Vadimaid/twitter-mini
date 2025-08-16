package twitter.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import twitter.configuration.ComponentFactory;
import twitter.controller.v2.AuthenticationController;
import twitter.dto.v2.request.LoginRequestDto;
import twitter.dto.v2.response.LoginResponseDto;
import twitter.exception.TwitterCommonException;

import java.io.IOException;

public class LoginCommandServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        LoginRequestDto requestDto = mapper.readValue(req.getInputStream(), LoginRequestDto.class);
        try {
            AuthenticationController authController = ComponentFactory.getComponent(AuthenticationController.class);
            LoginResponseDto responseDto = authController.login(requestDto);
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
