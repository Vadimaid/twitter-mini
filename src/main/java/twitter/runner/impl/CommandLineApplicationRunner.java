package twitter.runner.impl;

import twitter.configuration.Component;
import twitter.configuration.Injection;
import twitter.exception.UnknownCommandException;
import twitter.factory.CommandFactory;
import twitter.factory.CommandFactoryBuilder;
import twitter.runner.ApplicationRunner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

//@Component
public class CommandLineApplicationRunner implements ApplicationRunner {

    private final CommandFactoryBuilder commandFactoryBuilder;

//    @Injection
    public CommandLineApplicationRunner(CommandFactoryBuilder commandFactoryBuilder) {
        this.commandFactoryBuilder = commandFactoryBuilder;
    }

    @Override
    public void run() {
        String command = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
        CommandFactory commandFactory = commandFactoryBuilder.buildCommandFactoryForUser(null, reader, writer);
        while (true) {
            try {
                writer.append("Для получения помощи по командам, используйте команду help.").append("\n");
                writer.append("Введите команду: ");
                writer.flush();
                command = reader.readLine();
                commandFactory.getHandler(command).handle();
            } catch (UnknownCommandException ex) {
                System.out.println("Команда неопознана, проверьте список команд и попробуйте снова.");
            } catch (IOException ex) {
                System.out.println("Что то сломалось");
            }
        }
    }
}
