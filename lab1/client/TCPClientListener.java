package homework.client;

import java.io.IOException;

public class TCPClientListener extends ClientListener {
    public TCPClientListener(Client client) {
        super(client);
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = client.getTcpIn().readLine();
                displayMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void displayMessage(String message) {
        System.out.println("client #" + client.getID() + " received message: " + message);
    }
}
