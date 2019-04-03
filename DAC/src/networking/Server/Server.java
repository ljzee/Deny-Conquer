package networking.Server;

import java.awt.Color;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import command.Command;

import java.util.Map;

import game.Model;
import networking.Client.Client;
import networking.Shared.ClientInfo;

public class Server {

    public ServerSocket socket;
    ArrayList<ClientConnection> connections;
    Integer NumberOfConnections;
    Model model;
    Boolean done;
    Boolean isReconnect;

    ArrayList<Color> unusedColors;
    ArrayList<Color> usedColors;
    ArrayList<ClientInfo> clientInfos;
    //ArrayList<String> clientAddresses;

    ConcurrentLinkedQueue<Command> commandQueue = new ConcurrentLinkedQueue<Command>();
    
    //configurations
    int penThickness;
    int numBoxes;
    double targetPercentage;

    public Server(Model model, int port, int numOfConnections, ArrayList<ClientInfo> infos) {
        System.out.println(numOfConnections);
        this.NumberOfConnections = numOfConnections;

        model.clear();
        this.model = new Model(model, numBoxes, penThickness, targetPercentage);

        this.connections = new ArrayList<ClientConnection>();
        try {
            this.socket = new ServerSocket(port);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.clientInfos = new ArrayList<>(infos);
//        this.clientAddresses = new ArrayList<String>();
    }

    public Server(int port, int penThickness, int numBoxes, double targetPercentage) {
        this.NumberOfConnections = 1;

        this.connections = new ArrayList<ClientConnection>();
        try {
            this.socket = new ServerSocket(port);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
//        this.clientAddresses = new ArrayList<String>();
        this.clientInfos = new ArrayList<>();
        this.usedColors = new ArrayList<Color>();
        this.unusedColors = new ArrayList<Color>();
        this.unusedColors.add(Color.ORANGE);
        this.unusedColors.add(Color.BLUE);
        this.unusedColors.add(Color.RED);
        this.unusedColors.add(Color.YELLOW);
        this.unusedColors.add(Color.GREEN);
        this.unusedColors.add(Color.DARK_GRAY);
        this.unusedColors.add(Color.MAGENTA);
        
        //settings
        this.penThickness = penThickness;
        this.numBoxes = numBoxes;
        this.targetPercentage = targetPercentage;

    }

    public void acceptConnections(int numberOfConnections) {
        System.out.println("Waiting for connections");
        int i = 0;
        if (isReconnect) {
            try{
                socket.setSoTimeout(3000);
            } catch (SocketException se){
                se.printStackTrace();
            }
        }
        while (this.connections.size() < numberOfConnections) {
            try {
                Socket clientSocket = socket.accept();
                ClientConnection clientConnection;
                if (!this.isReconnect) {
                    clientConnection = new ClientConnection(clientSocket, this, i);
                } else {
                    int connectionID = ServerHelper.getOldConnectionID(clientSocket.getInetAddress().toString(),
                            clientInfos);
                    clientConnection = new ClientConnection(clientSocket, this, connectionID);
                }
                clientConnection.start();
                connections.add(clientConnection);
                System.out.println("New connection: " + clientSocket.getRemoteSocketAddress().toString());
                System.out.println("Number of connections needed: " + (numberOfConnections - this.connections.size()));
            } catch (IOException e) {
                // if player is not connect back in time, server will give up listening.
                if(e instanceof SocketTimeoutException){
                    numberOfConnections -=1;
                } else{
                    e.printStackTrace();

                }
            }
            i++;
        }
    }

    public Color getUnusedColor() {
//        System.out.println(unusedColors.size() - 1);
        Color color = unusedColors.get(unusedColors.size() - 1);
        unusedColors.remove(unusedColors.size() - 1);
        usedColors.add(color);
        return color;
    }

    public void beginHandlingClientCommands() {
        for (ClientConnection c : connections) {
            if (!this.isReconnect) {
                Color color = getUnusedColor();
                c.setColor(color);
                c.sendToClient(color);

                c.sendToClient(c.getConnectionID());
                
                //settings
                c.sendToClient(penThickness);
                c.sendToClient(numBoxes);
                c.sendToClient(targetPercentage);

                clientInfos.add(new ClientInfo(color, c.socket.getInetAddress().toString(), c.getConnectionID()));
            } else {
                c.setColor(ServerHelper.getPreassignedColor(c.socket.getInetAddress().toString(), clientInfos));
            }
        }
        for (ClientConnection c : connections) {
            c.sendToClient(new ArrayList<ClientInfo>(clientInfos.subList(1, clientInfos.size())));
        }
    }

    public void gameInit() {
        this.model = new Model(getUnusedColor(), numBoxes, penThickness, targetPercentage);
        this.done = false;
        beginHandlingClientCommands();
    }


    public void handleProcessCommand() {
        while (connections.size() != 0) {
            CommandProcessor.processCommands(commandQueue, connections, model);
            try {
                TimeUnit.MILLISECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("All clients have quited, game terminated.");
        System.exit(0);
    }

    //    public static void init(String[] args) {
    public void init(Boolean isReconnect) {

//        Server server = new Server(9991);
        this.isReconnect = isReconnect;
        this.acceptConnections(this.NumberOfConnections);
        try {
            TimeUnit.SECONDS.sleep(1); //to ensure clock synchronization tasks are done
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!this.isReconnect) {
            this.gameInit();
        } else {
            beginHandlingClientCommands();
        }
        System.out.println(clientInfos);
        this.handleProcessCommand();
    }

}