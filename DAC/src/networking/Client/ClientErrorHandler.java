package networking.Client;

import networking.Server.*;

import java.net.Socket;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.io.*;

public class ClientErrorHandler {
    public static void handleServerDisc(Client client) {
        String localAddress = String.valueOf(client.echoSocket.getLocalSocketAddress());
//        if (client.clientAddresses.indexOf(localAddress) == -1){
//            System.out.println("Server crashed, server side client shuts down");
//            System.exit(0);
//        }
        String nextServerDestination = client.clientAddresses.get(0);
        System.out.println(nextServerDestination);
        System.out.println(localAddress);

        //special case, latter client getting first committed due to delay
        if (localAddress.equals(nextServerDestination) | client.clientAddresses.size()==1 ) {
            new Thread(() -> startAltServer(client)).start();
        }

        try{
            Thread.sleep(500);
        }catch(InterruptedException ie){
            ie.printStackTrace();
        }
        if (client.clientAddresses.size()==1){
            connectToAltServer(client, true);
        } else {
            connectToAltServer(client);
        }
    }

    public static void startAltServer(Client client) {
        int clientPort = client.echoSocket.getLocalPort();
        System.out.println("Server moved to this machine");
        Server server = new Server(client.model, clientPort + 100, client.clientAddresses.size());
        System.out.println(server.socket.getLocalPort());
        server.init(true);
    }

    public static void connectToAltServer(Client client){
        String[] destAddrData = client.clientAddresses.get(0).split(":");
//        SocketAddress socketAddr = new InetSocketAddress(destAddrData[0].replace("/", ""),
//                Integer.valueOf(destAddrData[1])+100);
        try{
            client.echoSocket.close();
//            client.echoSocket.connect(socketAddr);
            client.echoSocket = new Socket(destAddrData[0].replace("/", ""),
                    Integer.valueOf(destAddrData[1]) + 100);
            System.out.println("Reconnect successfully");
            client.init();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public static void connectToAltServer(Client client, Boolean isLastClient){
        var addr = client.echoSocket.getInetAddress().toString().replace("/", "");
        int port = client.echoSocket.getLocalPort()+100;
        System.out.println(port);
        try{
            client.echoSocket.close();
//            client.echoSocket.connect(socketAddr);
            System.out.println(addr.toString());
            client.echoSocket = new Socket(addr, port);
            System.out.println("Last client reconnect successfully");
            client.init();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}