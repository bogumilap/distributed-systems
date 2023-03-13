package homework;

import homework.client.Client;
import homework.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class App {
    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
        Map<Integer, Client> clients = new HashMap<>();

        printOptions();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (server.isRunning()) {
            System.out.println();
            String command = reader.readLine();
            String[] split_command = command.split(" ");
            switch (split_command[0]) {
                case "C" -> {
                    Client client = new Client();
                    client.start();
                    clients.put(client.getID(), client);
                }
                case "T" -> sendTCPMessage(clients, split_command, command);
                case "U" -> sendUDPMessage(clients, split_command, reader);
                case "close" -> stopAllThreads(server, clients);
                default -> printOptions();
            }
        }
        System.out.println("app is closed.");
    }

    public static void printOptions() {
        System.out.println("""
                type:
                 C to create new client;
                 T [client ID] [message] to send message to server from client with given ID using TCP;
                 U [client ID] [number of lines of message] to start creating message to server from client with given ID using UDP;
                 close to stop all threads and quit the app.""");
    }

    public static void sendTCPMessage(Map<Integer, Client> clients, String[] split_command, String command) {
        if (split_command.length < 3) {
            System.out.println("expected: T [client ID] [message]");
            return;
        }
        try {
            Client selectedClient = clients.get(Integer.parseInt(split_command[1]));
            if (selectedClient != null) {
                String message = command.substring(split_command[0].length() + split_command[1].length() + 2);
                selectedClient.sendTCPMessage(message);
            } else {
                System.out.println("client with this ID doesn't exist.");
            }
        } catch (NumberFormatException e) {
            System.out.println("client ID is a number.");
        }
    }

    public static void sendUDPMessage(Map<Integer, Client> clients, String[] split_command, BufferedReader reader) {
        if (split_command.length < 3) {
            System.out.println("expected: U [client ID] [number of lines of message]");
            return;
        }
        try {
            Client selectedClient = clients.get(Integer.parseInt(split_command[1]));
            if (selectedClient != null) {
                int length = Integer.parseInt(split_command[2]);
                System.out.println("type message:");
                int line_count = 0;
                String[] buffer = new String[length];
                while (line_count < length) {
                    buffer[line_count] = reader.readLine();
                    line_count++;
                }
                selectedClient.sendUDPMessage(buffer);
            } else {
                System.out.println("client with this ID doesn't exist.");
            }
        } catch (NumberFormatException e) {
            System.out.println("client ID and number of lines are numbers.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void stopAllThreads(Server server, Map<Integer, Client> clients) {
        for (Client client : clients.values()) {
            client.setRunning(false);
        }
        server.setRunning(false);
    }
}
