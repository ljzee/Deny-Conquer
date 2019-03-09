package networking;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
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

import command.ClearCellColorCommand;
import command.Command;
import command.LockCellCommand;
import command.PollGameDataCommand;
import command.ScribbleCellCommand;
import command.UpdateCellColorCommand;
import game.Model;
import game.CellPane;
import game.Grid;

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
			
			c.sendToClient(c.getConnectionID());
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
			if(command instanceof LockCellCommand) {
				LockCellCommand lockCellCommand = (LockCellCommand) command;
				
				int x = lockCellCommand.getX();
			    int y = lockCellCommand.getY();
			    
			    ClientConnection connection = connections.get(command.getConnectionID());
			    
			    CellPane cell = (CellPane) model.getGrid().getComponentAt(x, y);
			    
		    	if(cell.getOwnerID() == -1) {
		    		cell.setOwnerID(command.getConnectionID());
		    		cell.setColor(connection.playerColor);
		    		System.out.println(command.getConnectionID() + " - Successfully locked! " + x + " " + y);
		    	} else {
		    		System.out.println(command.getConnectionID() + " - Failed lock!: client ID: " + cell.getOwnerID() + " has already locked this cell.");
		    	} 
			} else if(command instanceof ScribbleCellCommand) {
				ScribbleCellCommand scribbleCellCommand = (ScribbleCellCommand) command;
				
				int x = scribbleCellCommand.getX();
			    int y = scribbleCellCommand.getY();
			    
			    ClientConnection connection = connections.get(command.getConnectionID());
			    
			    CellPane cell = (CellPane) model.getGrid().getComponentAt(x, y);
			    
		    	if(cell.getOwnerID() == command.getConnectionID()) {
				    cell.getPoints().addAll(scribbleCellCommand.getPoints());
				    cell.repaint();
		    		System.out.println(cell.getPoints().size() + " " + scribbleCellCommand.getPoints().size());
				    System.out.println(command.getConnectionID() + " - Successfully scribbled! " + x + " " + y);
		    	} else {
		    		System.out.println(command.getConnectionID() + " - Failed scribbled!: client ID: " + cell.getOwnerID() + " has locked this cell.");
		    	}
			} else if(command instanceof ClearCellColorCommand) {
				ClearCellColorCommand clearCellColorCommand = (ClearCellColorCommand) command;
				
				int x = clearCellColorCommand.getX();
			    int y = clearCellColorCommand.getY();
			    
			    ClientConnection connection = connections.get(command.getConnectionID());
			    
			    CellPane cell = (CellPane) model.getGrid().getComponentAt(x, y);
			    
			    if(cell.reachedColoredThreshold()) {
			    	int ownerID = cell.getOwnerID();
			    	cell.clearCell();
			    	cell.setOwnerID(ownerID);
			    	cell.setBackground(connection.playerColor);
			    	cell.setDone(true);
			    	System.out.println(command.getConnectionID() + " - Successfully colored! " + x + " " + y);
			    } else {
			    	cell.clearCell();
				    System.out.println(command.getConnectionID() + " - Successfully cleared! " + x + " " + y);
			    }
			}
		}
	}
	
    public static void main(String[] args) {
        Server server = new Server(9991);
    	server.acceptConnections(3);
    	server.gameInit();
    	
    	
    	while(true) {
    		server.processCommands();
    		try {
				TimeUnit.MILLISECONDS.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    }
}
