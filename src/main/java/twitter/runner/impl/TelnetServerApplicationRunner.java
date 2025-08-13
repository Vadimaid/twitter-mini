package twitter.runner.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter.configuration.Component;
import twitter.configuration.Injection;
import twitter.configuration.Value;
import twitter.factory.CommandFactoryBuilder;
import twitter.runner.ApplicationRunner;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//@Component
public class TelnetServerApplicationRunner implements ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(TelnetServerApplicationRunner.class);

    @Value(key = "application.port")
    private Integer port;

    @Value(key = "max.users.count")
    private Integer maxUsersCount;

    private ExecutorService threadPool;
    private volatile boolean running;

    private final CommandFactoryBuilder commandFactoryBuilder;

//    @Injection
    public TelnetServerApplicationRunner(CommandFactoryBuilder commandFactoryBuilder) {
        this.commandFactoryBuilder = commandFactoryBuilder;
    }

    @Override
    public void run() {
        this.threadPool = Executors.newFixedThreadPool(maxUsersCount);
        new Thread(() -> {
            running = true;
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                logger.info("Telnet server started on port " + port);

                while (running) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        Runnable clientHandling = new TelnetClientHandler(clientSocket, commandFactoryBuilder);
                        threadPool.execute(clientHandling);
                    } catch (IOException e) {
                        if (!running) {
                            logger.error("Server stopped.");
                            break;
                        }
                        logger.error("Error with client connection: " + e.getMessage());
                    }
                }
            } catch (IOException e) {
                logger.error("Error starting Telnet server: " + e.getMessage());
            } finally {
                threadPool.shutdown();
            }
        }).start();
    }
}
