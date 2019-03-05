package networking;

import java.awt.Color;
import java.awt.Component;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;

import command.Command;
import command.PollGameDataCommand;
import command.UpdateCellColorCommand;
import game.Model;
import game.Grid;

/**
* Written by Martin Ombura Jr. <@martinomburajr>
*/
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
		while(this.connections.size() < numberOfConnections) {
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
		for(ClientConnection c : connections) {
			c.start();
			
			Color color = getUnusedColor();
			c.setColor(color);
			c.sendToClient(color);
		}
	}
	
	public void gameInit() {
		this.model = new Model(getUnusedColor());
		this.done = false;
		beginHandlingClientCommands();
	}
	
	public void processCommands() {
		while(!commandQueue.isEmpty()) {
			Command command = commandQueue.poll();
			if(command instanceof UpdateCellColorCommand) {
				UpdateCellColorCommand updateCellColorCommand = (UpdateCellColorCommand) command;
				
				int x = updateCellColorCommand.getX();
			    int y = updateCellColorCommand.getY();
			    
			    //synchronized (this) {
		    		//server.model.getGrid().getComponentAt(x, y).setBackground(this.playerColor);
			    //}
			    ClientConnection connection = connections.get(command.getConnectionID());
			    model.getGrid().getComponentAt(x, y).setBackground(connection.playerColor);
			    //connection.sendToClient("Successfully colored!");
			}
//			} else if(command instanceof PollGameDataCommand) {
//				ClientConnection connection = connections.get(command.getConnectionID());
//				connection.sendToClient(model.pollGameData());
//		    }
		}
	}
	
    public static void main(String[] args) {
        Server server = new Server(9991);
    	server.acceptConnections(3);
    	server.gameInit();
    	
    	
    	while(true) {
    		server.processCommands();
    		try {
				TimeUnit.MILLISECONDS.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    }
}
