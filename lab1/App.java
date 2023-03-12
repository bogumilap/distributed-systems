package homework;

import homework.client.Client;
import homework.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class App {
    public static void main(String[] args) throws IOException {
        new Server().start();
        Map<Integer, Client> clients = new HashMap<>();

        System.out.println("""
                type:
                 C to create new client;
                 T [client ID] [message] to send message to server from client with given ID using TCP;
                 U [client ID] [number of lines of message] to start creating message to server from client with given ID using UDP.""");

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println();
            String command = reader.readLine();
            String[] split_command = command.split(" ");
            switch (split_command[0]) {
                case "C":
                    Client client = new Client();
                    client.start();
                    clients.put(client.getID(), client);
                    break;
                case "T":
                    if (split_command.length < 3) {
                        System.out.println("expected: T [client ID] [message]");
                        break;
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
                    break;
                case "U":
                    if (split_command.length < 3) {
                        System.out.println("expected: U [client ID] [number of lines of message]");
                        break;
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
                    }
                    break;
                default:
                    System.out.println("""
                            type:
                             C to create new client;
                             T [client ID] [message] to send message to server from client with given ID using TCP;
                             U [client ID] [number of lines of message] to start creating message to server from client with given ID using UDP.""");
                    break;
            }
        }
    }
}
