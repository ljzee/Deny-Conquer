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
import java.util.concurrent.TimeUnit;

import command.Command;
import command.PollGameDataCommand;
import command.PollGameDataCommandResponse;
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

		    ArrayList<Command> commands = new ArrayList<Command>();
		    
		    while(true) {
		    	commands = t.getBlueCells();
		    	if(!commands.isEmpty()) {
		    		for(Command command : commands) {
		    			out.writeObject(command);
		    			in.readObject();
		    		}
		    	}
		    	
		    	out.writeObject(new PollGameDataCommand());
		    	ArrayList<PollGameDataCommandResponse> response = (ArrayList<PollGameDataCommandResponse>)in.readObject();
		    	
		    	for(PollGameDataCommandResponse command : response) {
		    		t.getGrid().getComponentAt(command.getX(), command.getY()).setBackground(command.getColor());
		    	}
		    	
	    		try {
					TimeUnit.MILLISECONDS.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
