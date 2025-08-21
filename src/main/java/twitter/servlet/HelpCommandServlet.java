package twitter.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;

public class HelpCommandServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/yml; charset=UTF-8");
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("openapi.yml")) {
            if (in == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "openapi.yml not found");
                return;
            }
            String ymlDocumentation = new String(in.readAllBytes());
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(ymlDocumentation);
        }

    }
}
