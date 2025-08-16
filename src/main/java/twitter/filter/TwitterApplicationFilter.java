package twitter.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import twitter.configuration.ComponentFactory;
import twitter.security.JwtHandler;
import twitter.source.AllowedEndpoints;

import java.io.IOException;

public class TwitterApplicationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        if ("OPTIONS".equalsIgnoreCase(httpServletRequest.getMethod())) {
            httpResponse.setHeader(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
            httpResponse.setHeader(CrossOriginFilter.ACCESS_CONTROL_ALLOW_METHODS_HEADER, "GET, POST, OPTIONS");
            httpResponse.setHeader(CrossOriginFilter.ACCESS_CONTROL_ALLOW_HEADERS_HEADER, "Authorization, Content-Type");
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        AllowedEndpoints allowedEndpoints = ComponentFactory.getComponent(AllowedEndpoints.class);
        if (allowedEndpoints.isEndpointAllowed(httpServletRequest.getRequestURI())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String authorization = httpServletRequest.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
            httpResponse.getWriter().write("Отсутствует или неверный токен авторизации.");
            return;
        }

        String token = authorization.substring(7);
        JwtHandler jwtHandler = ComponentFactory.getComponent(JwtHandler.class);
        if (!jwtHandler.validateToken(token)) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        filterChain.doFilter(httpServletRequest, httpResponse);
    }

}
