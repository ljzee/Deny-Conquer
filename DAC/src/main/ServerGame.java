package main;
import networking.Server.Server;
import networking.Client.Client;

public class ServerGame implements IGame {
    private Server server;
    private Client client;

    public ServerGame(int port, int penThickness, int numBoxes, double percentageCovered){

        this.server = new Server(port, penThickness, numBoxes, percentageCovered);
        this.client = new Client();
    }

    @Override
    public void run() {
        new Thread(() -> server.init(false)).start();
        new Thread(() -> client.init()).start();
    }
}
