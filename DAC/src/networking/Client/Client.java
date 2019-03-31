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
		
		String hostName = "192.168.0.2";
		int portNumber = 9991;
		
		int syncIteration = 50; //number of iterations to run in the initial clock synchronization process
		Long currentLatency = new Long(0); //updated by timing PollGameDataCommand
		Long offset = new Long(0); //offset between client time (currentTimeMillis) and server time
		
		ConcurrentLinkedQueue<Command> commandQueue = new ConcurrentLinkedQueue<Command>();


		try {
		    Socket echoSocket = new Socket(hostName, portNumber);
		    
		    ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(echoSocket.getOutputStream()));
		    out.flush(); // flush the stream
		    ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(echoSocket.getInputStream()));
		    
		    long cumulativeOffset = 0;
		    for(int i = 0; i < syncIteration; i++) {
		    	
		    	//measuring RTT
			    long start = System.currentTimeMillis();
			    
			    out.writeObject("time");
	    		out.flush();
    			out.reset(); // Reset the stream
    			long systemTimeInMs = (long)in.readObject();
			    
    			long stop = System.currentTimeMillis();
    			
    			currentLatency = (stop - start)/2;
    			cumulativeOffset = cumulativeOffset + (systemTimeInMs + currentLatency - stop);
    			
		    }
		    offset = cumulativeOffset/syncIteration; //average offset is calculated and used for future
		    out.writeObject("synced");
    		out.flush();
			out.reset(); // Reset the stream
		    
		    //Blocks until server starts a game session in which colors will be assigned to all players, server blocks if not enough connections
		    Color assignedColor = (Color)in.readObject();
		    int clientID = (int)in.readObject();
		  
		    
		    Model t = new Model(assignedColor, commandQueue, clientID, offset, currentLatency);
		    
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
		    	
		    	long start = System.currentTimeMillis();
		    	out.writeObject(new PollGameDataCommand());
	    		out.flush();
    			out.reset(); // Reset the stream
		    	
		    	ArrayList<PollGameDataCommandResponse> response = (ArrayList<PollGameDataCommandResponse>)in.readObject();
		    	long stop = System.currentTimeMillis();
		    	
		    	currentLatency = (stop - start)/2;
		    	
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
