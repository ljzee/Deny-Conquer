package networking;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import game.Model;

public class ClientConnection extends Thread {
	
	Server server;
	Socket socket;
	ObjectInputStream oinstream;
	ObjectOutputStream ooutstream;
	
	Boolean playerHasQuit;
	
	Color playerColor;
	
	public ClientConnection(Socket socket, Server server) {
		super();
		this.server = server;
		this.socket = socket;
		
		try {
			this.oinstream = new ObjectInputStream(socket.getInputStream());
			this.ooutstream = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		playerHasQuit = false;
	}
	
	public void sendToClient(Object obj) {
		try {
			this.ooutstream.writeObject(obj);
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
		
	    ArrayList<String> commands = new ArrayList<String>();

        while(!this.playerHasQuit && !this.server.done) {
            Object command = null;
			try {
				command = this.oinstream.readObject();
            
			    if(command instanceof String) {
			    	String line = (String) command;
				    if(line.toLowerCase().trim().equals("peace")) {
			        this.playerHasQuit = true;
				}
			
			    if(line.startsWith("Color ")) {
			    String[] coordinates = line.trim().substring(6).split("\\s+");
			   
			    int x = Integer.parseInt(coordinates[0]);
			    int y = Integer.parseInt(coordinates[1]);
			   
			    server.model.getGrid().getComponentAt(x, y).setBackground(this.playerColor);
			    ooutstream.writeObject("Successfully colored!");
			    }else if(line.startsWith("Poll")) {
			    		ooutstream.writeObject(server.model.pollGameData());
			    	}
			    }
           
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
           //System.out.println("dsafs");
//           commands = t.getBlueCells();
//           //System.out.println(commands);
//	    	if(!commands.isEmpty()) {
//	    		for(String command : commands) {
//	    			System.out.println(command);
//	    			serverPrintOut.println(command);
//	    		}
//	    	}

           
            
            
        }
	}
	
	public void run(){
		
		handlePlayerCommands();
		
	}
}
