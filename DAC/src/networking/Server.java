package networking;

import java.awt.Color;
import java.awt.Component;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;

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
		while(this.connections.size() < numberOfConnections) {
			try {
				Socket clientSocket = socket.accept();
				ClientConnection clientConnection = new ClientConnection(clientSocket, this);
				connections.add(clientConnection);
				System.out.println("New connection: " + clientSocket.getRemoteSocketAddress().toString());
				System.out.println("Number of connections needed: " + (numberOfConnections - this.connections.size()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
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
	
    public static void main(String[] args) {
        Server server = new Server(9991);
    	server.acceptConnections(3);
    	server.gameInit();
    	
    }
}
