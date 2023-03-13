package homework.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

public class UDPClientListener extends ClientListener {
    public UDPClientListener(Client client) {
        super(client);
    }

    @Override
    public void run() {
        DatagramSocket udpResponseSocket = null;
        try {
            udpResponseSocket = new DatagramSocket(client.getID());
            byte[] receiveBuffer = new byte[1024];
            while (client.isRunning()) {
                Arrays.fill(receiveBuffer, (byte)0);
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                udpResponseSocket.receive(receivePacket);
                System.out.println("client #" + client.getID() + " received message: ");
                displayMessage(new String(receivePacket.getData()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (udpResponseSocket != null) {
                udpResponseSocket.close();
            }
            if (client.getUdpSocket() != null) {
                client.getUdpSocket().close();
            }
        }
    }

    @Override
    protected void displayMessage(String message) {
        int beginning = message.indexOf("[") + 1;
        int end = message.lastIndexOf("]");
        String[] lines = message.substring(beginning, end).split(", ");
        for (String line : lines) {
            System.out.println(line);
        }
    }
}
