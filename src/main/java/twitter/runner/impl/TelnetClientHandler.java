package twitter.runner.impl;

import twitter.exception.ClientDisconnectedException;
import twitter.exception.UnknownCommandException;
import twitter.factory.CommandFactory;
import twitter.factory.CommandFactoryBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Objects;

public class TelnetClientHandler implements Runnable {

    private final Socket clientSocket;
    private final CommandFactoryBuilder commandFactoryBuilder;

    public TelnetClientHandler(Socket clientSocket, CommandFactoryBuilder commandFactoryBuilder) {
        this.clientSocket = clientSocket;
        this.commandFactoryBuilder = commandFactoryBuilder;
    }

    @Override
    public void run() {
        String command = "";
        String clientId = clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort();
        System.out.println("New client connected: " + clientId);
        BufferedReader reader = null;
        BufferedWriter writer = null;

        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            CommandFactory commandFactory = commandFactoryBuilder.buildCommandFactoryForUser(clientId, reader, writer);
            while (true) {
                try {
                    writer.append("Для получения помощи по командам, используйте команду help.").append("\n");
                    writer.append("Введите команду: ");
                    writer.flush();
                    command = reader.readLine();
                    commandFactory.getHandler(command).handle();
                } catch (UnknownCommandException ex) {
                    try {
                        writer.write("Команда неопознана, проверьте список команд и попробуйте снова.");
                    } catch (IOException ex1) {
                        System.out.println(ex.getMessage());
                    }
                } catch (ClientDisconnectedException ex) {
                    System.out.println("Client with IP " + clientId + " disconnected.");
                    return;
                }
            }
        } catch (IOException ex) {
            System.out.println("Что то сломалось, сообщение: " + ex.getMessage());
        } finally {
            try {
                if (Objects.nonNull(reader)) {
                    reader.close();
                }
                if (Objects.nonNull(writer)) {
                    writer.close();
                }
                clientSocket.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }

    }
}
