package homework.client;

public abstract class ClientListener extends Thread {
    protected final Client client;

    public ClientListener(Client client) {
        this.client = client;
    }

    protected void displayMessage(String message) { }
}
