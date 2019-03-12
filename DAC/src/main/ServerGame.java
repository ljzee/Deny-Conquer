package main;

import networking.Server.Server;

public class ServerGame implements IGame {
    private Server server;

    public ServerGame(){
        this.server = new Server(9991);
    }

    @Override
    public void run() {
        server.init();
    }
}
