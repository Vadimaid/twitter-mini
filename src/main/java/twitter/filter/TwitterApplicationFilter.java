package twitter.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import twitter.configuration.ComponentFactory;
import twitter.security.JwtHandler;
import twitter.source.AllowedEndpoints;

import java.io.IOException;

public class TwitterApplicationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        AllowedEndpoints allowedEndpoints = ComponentFactory.getComponent(AllowedEndpoints.class);
        if (allowedEndpoints.isEndpointAllowed(httpServletRequest.getRequestURI())) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String authorization = httpServletRequest.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String token = authorization.substring(7);
        JwtHandler jwtHandler = ComponentFactory.getComponent(JwtHandler.class);
        if (!jwtHandler.validateToken(token)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

}
