package homework.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;


public class Client extends Thread {
    private final int portNumber = 12345;
    private final String hostName = "localhost";
    private final Socket tcpSocket;
    private final PrintWriter tcpOut;
    private final BufferedReader tcpIn;
    private final DatagramSocket udpSocket;
    private AtomicBoolean running = new AtomicBoolean(true);

    public Client() throws IOException {
        // create socket
        tcpSocket = new Socket(hostName, portNumber);
        udpSocket = new DatagramSocket();
        System.out.println("JAVA TCP/UDP CLIENT #" + getID());

        // TCP in & out streams
        tcpOut = new PrintWriter(tcpSocket.getOutputStream(), true);
        tcpIn = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
    }

    public void run() {
        try {
            (new TCPClientListener(this)).start();
            (new UDPClientListener(this)).start();
            while (isRunning()) { }
            System.out.println("Client #" + getID() + " closed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTCPMessage(String message) {
        System.out.println("client #" + getID() + " sends TCP message: " + message);
        tcpOut.println(message);
    }

    public void sendUDPMessage(String[] message) throws IOException {
        InetAddress address = InetAddress.getByName(hostName);
        String stringifiedMessage = Arrays.toString(message);
        byte[] sendBuffer = (getID() + stringifiedMessage).getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, portNumber);
        udpSocket.send(sendPacket);
        System.out.println("client #" + getID() + " sends UDP message: " + stringifiedMessage);
    }

    public int getID() {
        return tcpSocket.getLocalPort();
    }

    public BufferedReader getTcpIn() {
        return tcpIn;
    }

    public Socket getTcpSocket() {
        return tcpSocket;
    }

    public DatagramSocket getUdpSocket() {
        return udpSocket;
    }

    public boolean isRunning() {
        return running.get();
    }

    public void setRunning(boolean running) {
        this.running.set(running);
    }
}