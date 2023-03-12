package homework.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPServerListener extends ServerListener {
    private final Socket clientSocket;

    public TCPServerListener(Socket clientSocket, Server server) {
        super(server);
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            int senderID = clientSocket.getPort();
            while (true) {
                String message = in.readLine();
                if (message != null) {
                    System.out.println("server received TCP msg: " + message + " from client #" + senderID);
                    passMessage(senderID, message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void passMessage(int senderID, String message) throws IOException {
        PrintWriter out;
        for (Socket client : server.getConnectedClients()) {
            int clientID = client.getPort();
            if (clientID != senderID) {
                System.out.println("server passes message from client #" + senderID + " to client #" + clientID + " via TCP");
                out = new PrintWriter(client.getOutputStream(), true);
                out.println("FROM #" + senderID + ": " + message);
            }
        }
    }
}
