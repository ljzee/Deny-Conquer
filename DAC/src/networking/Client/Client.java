package networking.Client;

import java.awt.Color;
import java.awt.Point;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import command.Command;
import command.PollGameDataCommand;
import command.PollGameDataCommandResponse;
import command.ScribbleCellCommand;
import game.CellPane;
import game.Model;

public class Client {
//	public static void init(String[] args) {

		public static void init() {
//		String hostName = args[0];
//		int portNumber = Integer.parseInt(args[1]);
		
		String hostName = "127.0.0.1";
		int portNumber = 9991;
		
		ConcurrentLinkedQueue<Command> commandQueue = new ConcurrentLinkedQueue<Command>();


		try {
		    Socket echoSocket = new Socket(hostName, portNumber);
		    
		    ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(echoSocket.getOutputStream()));
		    out.flush(); // flush the stream
		    ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(echoSocket.getInputStream()));
		    /*
		    for(int i = 0; i < 50; i++) {
		    	
			    long start = System.currentTimeMillis();
			    long systemTimeInMs = (long)in.readObject();
			    long end = System.currentTimeMillis();
			    long duration = end - start;
			    //System.out.println(i + " System time(ms): " + systemTimeInMs);
			    //System.out.println(i + " Client time(ms): " + end);
			    //System.out.println(i + " Delivery duration(ms): " + duration);
			    System.out.println(i + " Difference(ms): " + (systemTimeInMs - end));
		    }
		    */
		    //Blocks until server starts a game session in which colors will be assigned to all players, server blocks if not enough connections
		    Color assignedColor = (Color)in.readObject();
		    int clientID = (int)in.readObject();
		  
		    
		    Model t = new Model(assignedColor, commandQueue, clientID);
		    
		    while(true) {
		    	if(!commandQueue.isEmpty()) {
		    		if(commandQueue.peek() instanceof ScribbleCellCommand) {
		    			long initialTimestamp = commandQueue.peek().getTimeStamp();
		    			ArrayList<Point> points = new ArrayList<Point>();
		    			ScribbleCellCommand scribbleCellCommand = null;
			    		while(commandQueue.peek() instanceof ScribbleCellCommand) {
			    			scribbleCellCommand = (ScribbleCellCommand) commandQueue.poll();
			    			points.add(scribbleCellCommand.getPoint());
			    		}
			    		ScribbleCellCommand command = new ScribbleCellCommand(scribbleCellCommand.getX(),scribbleCellCommand.getY(),points,initialTimestamp);
			    		out.writeObject(command);
			    		out.flush();
		    			out.reset(); // Reset the stream
		    			in.readObject();
		    		} else {
		    			out.writeObject(commandQueue.poll());
			    		out.flush();
		    			out.reset(); // Reset the stream
		    			in.readObject();
		    		}
		    	}
		    	
		    	out.writeObject(new PollGameDataCommand());
	    		out.flush();
    			out.reset(); // Reset the stream
		    	
		    	ArrayList<PollGameDataCommandResponse> response = (ArrayList<PollGameDataCommandResponse>)in.readObject();
		    	
		    	for(PollGameDataCommandResponse command : response) {
		    		CellPane cell = (CellPane) t.getGrid().getComponentAt(command.getX(), command.getY());
		    		
		    		if(!cell.getDone()) {
			    		cell.setPoints(command.getPoints());
			    		cell.setColor(command.getBrushColor());
			    		cell.setBackground(command.getBackgroundColor());
			    		cell.setOwnerID(command.getOwnerID());
			    		cell.setDone(command.getDone());
			    		cell.repaint();
		    		}
		    	}
		    	
	    		try {
					TimeUnit.MILLISECONDS.sleep(5);
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
