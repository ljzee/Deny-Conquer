package networking.Server;

import java.awt.Color;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import command.Command;
import game.Model;

public class Server {

    public ServerSocket socket;
    ArrayList<ClientConnection> connections;
    Integer NumberOfConnections;
    Model model;
    Boolean done;
    Boolean isReconnect;

    ArrayList<Color> unusedColors;
    ArrayList<Color> usedColors;

    ArrayList<String> clientAddresses;

    ConcurrentLinkedQueue<Command> commandQueue = new ConcurrentLinkedQueue<Command>();

    public Server(Model model, int port, int numOfConnections) {
        System.out.println(numOfConnections);
        this.NumberOfConnections = numOfConnections;
        this.model = new Model(model);
        this.connections = new ArrayList<ClientConnection>();
        try {
            this.socket = new ServerSocket(port);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.clientAddresses = new ArrayList<String>();
    }

    public Server(int port) {
        this.NumberOfConnections = 4;

        this.connections = new ArrayList<ClientConnection>();
        try {
            this.socket = new ServerSocket(port);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.clientAddresses = new ArrayList<String>();

        this.usedColors = new ArrayList<Color>();
        this.unusedColors = new ArrayList<Color>();

        this.unusedColors.add(Color.BLUE);
        this.unusedColors.add(Color.RED);
        this.unusedColors.add(Color.YELLOW);
        this.unusedColors.add(Color.GREEN);
        this.unusedColors.add(Color.DARK_GRAY);
    }

    public void acceptConnections(int numberOfConnections) {
        System.out.println("Waiting for connections");
        int i = 0;
        while (this.connections.size() < numberOfConnections) {
            try {
                Socket clientSocket = socket.accept();
                ClientConnection clientConnection = new ClientConnection(clientSocket, this, i);
                clientConnection.start();
                connections.add(clientConnection);
                clientAddresses.add(clientSocket.getRemoteSocketAddress().toString());
                System.out.println("New connection: " + clientSocket.getRemoteSocketAddress().toString());
                System.out.println("Number of connections needed: " + (numberOfConnections - this.connections.size()));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
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
            if(!this.isReconnect){
                Color color = getUnusedColor();
                c.setColor(color);
                c.sendToClient(color);

                c.sendToClient(c.getConnectionID());
            }
            c.sendToClient( new ArrayList<String>(clientAddresses.subList(1, clientAddresses.size())));
        }
    }

    public void gameInit() {
        this.model = new Model(getUnusedColor());
        this.done = false;
        beginHandlingClientCommands();
    }


    public void handleProcessCommand() {
        while (connections.size() != 0) {
            CommandProcessor.processCommands(commandQueue, connections, model);
            try {
                TimeUnit.MILLISECONDS.sleep(5);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        if (!this.isReconnect) {
            this.gameInit();
        }
        else{
            beginHandlingClientCommands();
        }
        System.out.println(clientAddresses);
        this.handleProcessCommand();
    }

}