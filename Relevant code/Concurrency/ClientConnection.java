package networking.Server;

import java.awt.Color;
import java.awt.Component;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import command.Command;
import command.PollGameDataCommand;
import command.PollGameDataCommandResponse;
import game.CellPane;
import game.Grid;

//Client connection created to run on a separate thread every time a new client connects to server

public class ClientConnection extends Thread {

    Server server;
    Socket socket;
    ObjectInputStream oinstream;
    ObjectOutputStream ooutstream;
    
    Boolean playerHasQuit;
    Boolean isSynced;
    
    Color playerColor;

    int connectionID;

    public ClientConnection(Socket socket, Server server, int connectionID) {
        super();
        this.server = server;
        this.socket = socket;
        this.connectionID = connectionID;

        try {
            this.oinstream = new ObjectInputStream(new BufferedInputStream(this.socket.getInputStream()));
            this.ooutstream = new ObjectOutputStream(new BufferedOutputStream(this.socket.getOutputStream()));
            this.ooutstream.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
        }

        playerHasQuit = false;
        isSynced = false;
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

    public void syncPlayer() {
    	while(!this.playerHasQuit && !this.isSynced) {
    		try {
				String request = (String) this.oinstream.readObject();
				if (request.equals("time")) {
                    this.ooutstream.writeObject(System.currentTimeMillis());
                    this.ooutstream.flush();
                    this.ooutstream.reset(); // Reset the stream
				} else if (request.contentEquals("synced")){
					this.isSynced = true;
			        System.out.println(connectionID + " is Synced");
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
    
    private static List<String> getWinningPlayers(Grid grid) { 	//returns list of clientID's of winners
    	int width = grid.getWidth();
    	HashMap<String, Integer> map = new HashMap<String, Integer>();
    	List<String> keys = new ArrayList<>();

    	Component[] cells = null;
		cells = (Component[])grid.getComponents();
		
    	for(Component c : cells) {
			CellPane cell = (CellPane)c;

			Color color = cell.getColor();
        	String stColor = "";
        	
        	if(color.equals(Color.BLUE)) {
        		stColor = "BLUE";
			} else if (color.equals(Color.RED)) {
				stColor = "RED";
			} else if (color.equals(Color.YELLOW)) {
				stColor = "YELLOW";
			} else if (color.equals(Color.GREEN)) {
				stColor = "GREEN";
			} else if (color.equals(Color.ORANGE)) {
				stColor = "ORANGE";
			} else if (color.equals(Color.DARK_GRAY)) {
				stColor = "DARK GRAY";
			} else if (color.equals(Color.MAGENTA)) {
				stColor = "MAGENTA";
			}

        	
        	Integer sum = map.get(stColor);
        	if(sum == null) { //map with key does not exist
        		map.put(stColor, 1);
        	} else { //map with key exists
        		map.put(stColor, sum + 1);
        	} 
        }
            			
           
    	
    	//get max value
    	int max_Key_Value = Collections.max(map.values());
    	for(java.util.Map.Entry<String, Integer> mapEntry : map.entrySet()) {
    		if(mapEntry.getValue() == max_Key_Value) {
    			keys.add(mapEntry.getKey());
    		}
    	}
    	
    	//add score to end of list
    	keys.add(Integer.toString(max_Key_Value));
    	return keys;   	
    }

    public void handlePlayerCommands() {
        //move this to server
    	boolean serverDone = false;
    	boolean clientDone = false;

        while (!this.playerHasQuit  && !serverDone) {
            Command command = null;
            try {
                command = (Command) this.oinstream.readObject();

                //I want to move this out, but can't figure out how
                if (command instanceof PollGameDataCommand) {
                    
                	if(server.model.getPlayingState() == false) { //end of game
                		
                		if(clientDone) {
                			List<String> winningPlayers = getWinningPlayers(server.model.getGrid());
                    		ArrayList<PollGameDataCommandResponse> response = new ArrayList<PollGameDataCommandResponse>();
                    		response.add( new PollGameDataCommandResponse(server.model.getPlayingState(), winningPlayers) );
                    		this.ooutstream.writeObject(response);
                    		this.ooutstream.flush();
                            this.ooutstream.reset(); // Reset the stream
                    		//server done handling commands
                    		serverDone = true;
                		} else {
                			this.ooutstream.writeObject(server.model.pollGameData());
                			this.ooutstream.flush();
                            this.ooutstream.reset(); // Reset the stream
                			clientDone = true;
                		}
                		
                	} else {
                		this.ooutstream.writeObject(server.model.pollGameData());
                		this.ooutstream.flush();
                        this.ooutstream.reset(); // Reset the stream
                	}


                } else {
                    command.setConnectionID(connectionID);
                    server.commandQueue.add(command);
                    this.ooutstream.writeObject(0);
                    this.ooutstream.flush();
                    this.ooutstream.reset(); // Reset the stream
                }
            } catch (Exception ex) {
                // TODO Auto-generated catch block
               if (ex instanceof SocketException || ex instanceof EOFException) {
                   this.playerHasQuit = true;
                    System.out.println("The player has quit.");
                    server.connections.remove(this);
               }else{
                   ex.printStackTrace();
               }
            }
        }
    }

    public void run() {
    	syncPlayer();
        handlePlayerCommands();
    }
}
