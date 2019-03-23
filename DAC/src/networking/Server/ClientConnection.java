package networking.Server;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import command.Command;
import command.PollGameDataCommand;

public class ClientConnection extends Thread {

    Server server;
    Socket socket;
    ObjectInputStream oinstream;
    ObjectOutputStream ooutstream;

    Boolean playerHasQuit;

    Color playerColor;

    int connectionID;

    public ClientConnection(Socket socket, Server server, int connectionID) {
        super();
        this.server = server;
        this.socket = socket;
        this.connectionID = connectionID;

        try {
            this.oinstream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            this.ooutstream = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            this.ooutstream.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        playerHasQuit = false;
    }

    public void setConnectionID(int connectionID) {
        this.connectionID = connectionID;
    }

    public int getConnectionID() {
        return connectionID;
    }

    public void sendToClient(Object obj) {
        try {
            this.ooutstream.writeObject(obj);
            this.ooutstream.flush();
            this.ooutstream.reset(); // Reset the stream
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setColor(Color color) {
        this.playerColor = color;
    }

    public void handlePlayerCommands() {
        //move this to server


        while (!this.playerHasQuit /* && !this.server.done*/) {
            Command command = null;
            try {
                command = (Command) this.oinstream.readObject();

                //I want to move this out, but can't figure out how
                if (command instanceof PollGameDataCommand) {
                    this.ooutstream.writeObject(server.model.pollGameData());
                    this.ooutstream.flush();
                    this.ooutstream.reset(); // Reset the stream
                } else {
                    command.setConnectionID(connectionID);
                    server.commandQueue.add(command);
                    this.ooutstream.writeObject(0);
                    this.ooutstream.flush();
                    this.ooutstream.reset(); // Reset the stream
                }
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void run() {

        handlePlayerCommands();

    }
}