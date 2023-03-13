package homework.server;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server extends Thread {
    private final List<Socket> connectedClients = new ArrayList<>();
    private final ServerSocket tcpServerSocket;
    private final DatagramSocket udpServerSocket;
    private final AtomicBoolean running = new AtomicBoolean(true);

    public Server() throws IOException {
        int portNumber = 12345;
        tcpServerSocket = new ServerSocket(portNumber);
        udpServerSocket = new DatagramSocket(portNumber);
        System.out.println("JAVA TCP/UDP SERVER");
    }

    public void run() {
        (new UDPServerListener(this)).start();
        (new TCPServerConnectionListener(this)).start();
        while (isRunning()) { }
        System.out.println("Server closed.");
    }

    public DatagramSocket getUdpServerSocket() {
        return udpServerSocket;
    }

    public ServerSocket getTcpServerSocket() {
        return tcpServerSocket;
    }

    public void addClient(Socket clientSocket) {
        connectedClients.add(clientSocket);
    }

    public List<Socket> getConnectedClients() {
        return connectedClients;
    }

    public boolean isRunning() {
        return running.get();
    }

    public void setRunning(boolean running) {
        this.running.set(running);
    }
}