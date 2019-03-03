package networking;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import command.PollGameDataCommand;
import game.Model;

public class Client {
	public static void main(String args[]) {
//		String hostName = args[0];
//		int portNumber = Integer.parseInt(args[1]);
		
		String hostName = "127.0.0.1";
		int portNumber = 9991;


		try {
		    Socket echoSocket = new Socket(hostName, portNumber);
		    
		    ObjectOutputStream out = new ObjectOutputStream(echoSocket.getOutputStream());
		    ObjectInputStream in = new ObjectInputStream(echoSocket.getInputStream());
		    
		    
		    BufferedReader stdIn =
		        new BufferedReader(
		            new InputStreamReader(System.in));
		    
		    //Blocks until server starts a game session in which colors will be assigned to all players, server blocks if not enough connections
		    Color assignedColor = (Color)in.readObject();
		    
		    Model t = new Model(assignedColor);
		    
		    String userInput;

		    ArrayList<String> commands = new ArrayList<String>();
		    
		    while(true) {
		    	commands = t.getBlueCells();
		    	if(!commands.isEmpty()) {
		    		for(String command : commands) {
		    			out.writeObject(command);
		    			System.out.println((String)in.readObject());
		    		}
		    	}
		    	
		    	out.writeObject("Poll");
		    	ArrayList<PollGameDataCommand> response = (ArrayList<PollGameDataCommand>)in.readObject();
		    	
		    	for(PollGameDataCommand command : response) {
		    		t.getGrid().getComponentAt(command.getX(), command.getY()).setBackground(command.getColor());;
		    	}
//		    	
//		    	if(response.startsWith("Color ")) {
//            	   String[] coordinates = response.trim().substring(6).split("\\s+");
//            	   
//            	   int x = Integer.parseInt(coordinates[0]);
//            	   int y = Integer.parseInt(coordinates[1]);
//            	   
//            	   t.getGrid().getComponentAt(x, y).setBackground(Color.BLUE);
//               }
		    	
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
		}
	}
}
