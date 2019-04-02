package networking.Server;

import networking.Client.Client;

public class ServerSideClient extends Thread {
    Client client;

    public ServerSideClient(){
        client = new Client();
    }


    public void run(){
        client.run();
    }
}
