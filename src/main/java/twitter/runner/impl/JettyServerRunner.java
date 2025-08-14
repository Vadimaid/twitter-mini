package twitter.runner.impl;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import twitter.configuration.Component;
import twitter.configuration.Injection;
import twitter.configuration.Value;
import twitter.filter.TwitterApplicationFilter;
import twitter.runner.ApplicationRunner;
import twitter.servlet.*;

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

        context.addFilter(TwitterApplicationFilter.class, "/*", null);

        context.addServlet(LoginCommandServlet.class, "/api/login");
        context.addServlet(InfoCommandServlet.class, "/api/info");
        context.addServlet(RegisterCommandServlet.class, "/api/register");
        context.addServlet(InfoAllCommandServlet.class, "/api/info_all");
        context.addServlet(InfoByLoginCommandServlet.class, "/api/info_by_login");
        context.addServlet(AddPostCommandServlet.class, "/api/add_post");
        context.addServlet(MyPostsCommandServlet.class, "/api/my_posts");
        context.addServlet(PostsByTagCommandServlet.class, "/api/posts_by_tag");

        try {
            server.start();
            server.join();
        } catch (Exception e) {
            log.error("Error starting Jetty server: " + e.getMessage());
        }
    }
}
