package networking.Server;

import java.awt.Color;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import command.ClearCellColorCommand;
import command.Command;
import command.LockCellCommand;
import command.ScribbleCellCommand;
import game.Model;
import game.CellPane;

public class Server {

    ServerSocket socket;
    ArrayList<ClientConnection> connections;

    Model model;
    Boolean done;

    ArrayList<Color> unusedColors;
    ArrayList<Color> usedColors;

    ConcurrentLinkedQueue<Command> commandQueue = new ConcurrentLinkedQueue<Command>();

    public Server(int port) {
        this.connections = new ArrayList<ClientConnection>();
        try {
            this.socket = new ServerSocket(port);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        this.usedColors = new ArrayList<Color>();
        this.unusedColors = new ArrayList<Color>();

        this.unusedColors.add(Color.BLUE);
        this.unusedColors.add(Color.RED);
        this.unusedColors.add(Color.YELLOW);
        this.unusedColors.add(Color.GREEN);
    }

    public void acceptConnections(int numberOfConnections) {
        System.out.println("Waiting for connections");
        int i = 0;
        while (this.connections.size() < numberOfConnections) {
            try {
                Socket clientSocket = socket.accept();
                ClientConnection clientConnection = new ClientConnection(clientSocket, this, i);
                connections.add(clientConnection);
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
        Color color = unusedColors.get(unusedColors.size() - 1);
        unusedColors.remove(unusedColors.size() - 1);
        usedColors.add(color);
        return color;
    }

    public void beginHandlingClientCommands() {
        for (ClientConnection c : connections) {
            c.start();

            Color color = getUnusedColor();
            c.setColor(color);
            c.sendToClient(color);

            c.sendToClient(c.getConnectionID());
        }
    }

    public void gameInit() {
        this.model = new Model(getUnusedColor());
        this.done = false;
        beginHandlingClientCommands();
    }

    public void handleProcessCommand() {
        while (true) {
            CommandProcessor.processCommands(commandQueue, connections, model);
            try {
                TimeUnit.MILLISECONDS.sleep(5);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        Server server = new Server(9991);
        server.acceptConnections(3);
        server.gameInit();
        server.handleProcessCommand();
    }


}
