package homework.server;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {
    private final List<Socket> connectedClients = new ArrayList<>();
    private final ServerSocket tcpServerSocket;
    private final DatagramSocket udpServerSocket;

    public Server() throws IOException {
        int portNumber = 12345;
        tcpServerSocket = new ServerSocket(portNumber);
        udpServerSocket = new DatagramSocket(portNumber);
        System.out.println("JAVA TCP/UDP SERVER");
    }

    public void run() {
        try {
            (new UDPServerListener(this)).start();
            while (true) {
                Socket clientSocket = null;
                try {
                    clientSocket = tcpServerSocket.accept();
                    addClient(clientSocket);
                    System.out.println("client #" + clientSocket.getPort() + " connected to server");
                    (new TCPServerListener(clientSocket, this)).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            if (tcpServerSocket != null){
                try {
                    tcpServerSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (udpServerSocket != null) {
                udpServerSocket.close();
            }
        }
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
}