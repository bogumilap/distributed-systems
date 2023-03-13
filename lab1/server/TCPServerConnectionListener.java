package homework.server;

import java.io.IOException;
import java.net.Socket;

public class TCPServerConnectionListener extends Thread {
    private final Server server;

    public TCPServerConnectionListener(Server server) {
        this.server = server;
    }

    @Override
    public void run() {
        while (server.isRunning()) {
            Socket clientSocket = null;
            try {
                clientSocket = server.getTcpServerSocket().accept();
                server.addClient(clientSocket);
                System.out.println("client #" + clientSocket.getPort() + " connected to server");
                (new TCPServerListener(clientSocket, server)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
