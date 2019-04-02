package main;

import networking.Client.*;

import java.io.*;

public class ClientGame implements IGame {
    private Client client;

    public ClientGame(){
        this.client = new Client();
    }

    @Override
    public void run() throws IOException {
        client.init();
    }
}
