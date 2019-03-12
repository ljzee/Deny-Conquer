package main;

import networking.Client.*;

public class ClientGame implements IGame {
    private Client client;

    public ClientGame(){
        this.client = new Client();
    }

    @Override
    public void run() {
        client.init();
    }
}
