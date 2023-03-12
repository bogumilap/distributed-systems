package homework.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;

public class UDPServerListener extends ServerListener {
    public UDPServerListener(Server server) {
        super(server);
    }

    @Override
    public void run() {
        byte[] receiveBuffer = new byte[1024];
        while (true) {
            Arrays.fill(receiveBuffer, (byte) 0);
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            try {
                if (! server.getTcpServerSocket().isClosed()) {
                    server.getUdpServerSocket().receive(receivePacket);
                    String message = new String(receivePacket.getData());
                    int senderID = Integer.parseInt(message.substring(0, 5));
                    System.out.println("server received UDP msg: " + message + " from client #" + senderID);
                    passMessage(senderID, message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void passMessage(int senderID, String message) throws IOException {
        InetAddress address = InetAddress.getByName("localhost");
        byte[] sendBuffer = message.getBytes();
        for (Socket clientSocket : server.getConnectedClients()) {
            int clientID = clientSocket.getPort();
            if (clientID != senderID) {
                System.out.println("server passes message from client #" + senderID + " to client #" + clientID + " via UDP");
                DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, clientID);
                server.getUdpServerSocket().send(sendPacket);
            }
        }
    }
}
