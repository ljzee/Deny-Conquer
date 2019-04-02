package networking.Client;

import networking.Server.*;
import networking.Server.*;
import networking.Shared.ClientInfo;

import java.net.Socket;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.io.*;
import java.util.ArrayList;

public class ClientErrorHandler {
    public static void handleServerDisc(Client client) {
        String localAddress = String.valueOf(client.echoSocket.getLocalAddress());
//        if (client.clientAddresses.indexOf(localAddress) == -1){
//            System.out.println("Server crashed, server side client shuts down");
//            System.exit(0);
//        }
        ClientInfo nextServerInfo = client.clientInfos.get(0);
        String nextServerAddr = nextServerInfo.addr;

        System.out.println("Next Server" + nextServerAddr);

        System.out.println(localAddress);

        //special case, latter client getting first committed due to delay
        if (localAddress.equals(nextServerAddr) | client.clientInfos.size() == 1) {
            new Thread(() -> startAltServer(client)).start();
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        if (client.clientInfos.size() == 1) {
            connectToAltServer(client, true);
        } else {
            connectToAltServer(client);
        }
    }

    public static void startAltServer(Client client) {
        System.out.println("Server moved to this machine");
        Server server = new Server(client.model, 12345+client.clientInfos.size()*10, client.clientInfos.size(),
                client.clientInfos);
        server.init(true);
    }

    public static void connectToAltServer(Client client) {
        String destAddrData = client.clientInfos.get(0).addr;
//        SocketAddress socketAddr = new InetSocketAddress(destAddrData[0].replace("/", ""),
//                Integer.valueOf(destAddrData[1])+100);
        try {
            client.echoSocket.close();
//            client.echoSocket.connect(socketAddr);
            client.echoSocket = new Socket(destAddrData.replace("/", ""),
                    12345+client.clientInfos.size()*10);
            System.out.println("Reconnect successfully");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // overload function to handle last client
    public static void connectToAltServer(Client client, Boolean isLastClient) {
        var addr = client.echoSocket.getLocalAddress().toString().replace("/", "");
        int port = 12345+client.clientInfos.size()*10;
        try {
            client.echoSocket.close();
//            client.echoSocket.connect(socketAddr);
            System.out.println(addr.toString());
            client.echoSocket = new Socket(addr, port);
            System.out.println("Last client reconnect successfully");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}