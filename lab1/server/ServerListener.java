package homework.server;

import java.io.IOException;

public abstract class ServerListener extends Thread {
    protected final Server server;

    public ServerListener(Server server) {
        this.server = server;
    }

    protected void passMessage(int senderID, String message) throws IOException { }
}
