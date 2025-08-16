package twitter.runner.impl;

import jakarta.servlet.DispatcherType;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import twitter.configuration.Component;
import twitter.configuration.Injection;
import twitter.configuration.Value;
import twitter.filter.TwitterApplicationFilter;
import twitter.runner.ApplicationRunner;
import twitter.servlet.*;

import java.util.EnumSet;

@Component
@Slf4j
public class JettyServerRunner implements ApplicationRunner {

    @Value(key = "application.port")
    private Integer port;

    @Injection
    public JettyServerRunner() {}

    @Override
    public void run() {
        Server server = new Server(this.port);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);


        FilterHolder corsFilterHolder = new FilterHolder(CrossOriginFilter.class);
        corsFilterHolder.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        corsFilterHolder.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        corsFilterHolder.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "Authorization, Content-Type");
        context.addFilter(corsFilterHolder, "/*", EnumSet.of(DispatcherType.REQUEST));

        context.addFilter(TwitterApplicationFilter.class, "/*", null);

        context.addServlet(LoginCommandServlet.class, "/api/login");
        context.addServlet(InfoCommandServlet.class, "/api/info");
        context.addServlet(RegisterCommandServlet.class, "/api/register");
        context.addServlet(InfoAllCommandServlet.class, "/api/info_all");
        context.addServlet(InfoByLoginCommandServlet.class, "/api/info_by_login");
        context.addServlet(AddPostCommandServlet.class, "/api/add_post");
        context.addServlet(MyPostsCommandServlet.class, "/api/my_posts");
        context.addServlet(PostsByTagCommandServlet.class, "/api/posts_by_tag");
        context.addServlet(PostsByLoginCommandServlet.class, "/api/posts_by_login");
        context.addServlet(PostsByUserTypeCommandServlet.class, "/api/posts_by_user_type");
        context.addServlet(AddLikePostCommandServlet.class, "/api/add_like");
        context.addServlet(PostLikesUsersCommandServlet.class, "/api/post_users_like");
        context.addServlet(AllPostsCommandServlet.class, "/api/all_posts");


        try {
            server.start();
            server.join();
        } catch (Exception e) {
            log.error("Error starting Jetty server: " + e.getMessage());
        }
    }
}
