package networking.Client;

import java.awt.Color;
import java.awt.Point;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import command.Command;
import command.PollGameDataCommand;
import command.PollGameDataCommandResponse;
import command.ScribbleCellCommand;
import game.CellPane;
import game.Model;
import networking.Shared.ClientInfo;

public class Client {
//	public static void init(String[] args) {

    Model model;
    String hostName;
    int portNumber;

    ArrayList<ClientInfo> clientInfos;

    Socket echoSocket;
    ConcurrentLinkedQueue<Command> commandQueue;
    Color assignedColor;
    int clientID = -1;
    ArrayList<Color> clientColorList;
    int penThickness;
    int numBoxes;
    double percentageCovered;

    //test
    //test
    //to be filled in in the future, right now it's just simple constructor for testing.

    public Client() {
        this.hostName = "142.58.15.37";
        this.portNumber = 9991;
    }

    public void init() {
//		String hostName = args[0];
//		int portNumber = Integer.parseInt(args[1]);


        int syncIteration = 5; //number of iterations to run in the initial clock synchronization process
        Long currentLatency = new Long(0); //updated by timing PollGameDataCommand
        Long offset = new Long(0); //offset between client time (currentTimeMillis) and server time

        if (commandQueue == null){
            commandQueue = new ConcurrentLinkedQueue<Command>();
        }

        try {
            if (echoSocket == null) {
                echoSocket = new Socket(hostName, portNumber);
            }
            ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(echoSocket.getOutputStream()));
            out.flush(); // flush the stream
            ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(echoSocket.getInputStream()));

            long cumulativeOffset = 0;
            for (int i = 0; i < syncIteration; i++) {

                //measuring RTT
                long start = System.currentTimeMillis();

                out.writeObject("time");
                out.flush();
                out.reset(); // Reset the stream
                long systemTimeInMs = (long) in.readObject();

                long stop = System.currentTimeMillis();

                currentLatency = (stop - start) / 2;
                cumulativeOffset = cumulativeOffset + (systemTimeInMs + currentLatency - stop);

            }
            offset = cumulativeOffset / syncIteration; //average offset is calculated and used for future
            out.writeObject("synced");
            out.flush();
            out.reset(); // Reset the stream

            //Blocks until server starts a game session in which colors will be assigned to all players, server blocks if not enough connections
            if (assignedColor == null){
                assignedColor = (Color) in.readObject();
            }
            if (clientID == -1){
                clientID = (int) in.readObject();
            }
            //settings
            if (penThickness == 0){
                penThickness = (int)in.readObject();
            }
            if (numBoxes == 0){
                numBoxes = (int)in.readObject();
            }
            if (percentageCovered == 0){
                percentageCovered = (double)in.readObject();

            }
		    List<String> winningPlayers;

            clientInfos = (ArrayList<ClientInfo>) in.readObject();

            if(model == null) {
                model = new Model(assignedColor, commandQueue, clientID, offset, currentLatency, penThickness, numBoxes, percentageCovered);
            }

            System.out.println(clientInfos);
            System.out.println(echoSocket.getLocalPort());
            while (true) {
                if (!commandQueue.isEmpty()) {
                    if (commandQueue.peek() instanceof ScribbleCellCommand) {
                        long initialTimestamp = commandQueue.peek().getTimeStamp();
                        ArrayList<Point> points = new ArrayList<Point>();
                        ScribbleCellCommand scribbleCellCommand = null;
                        while (commandQueue.peek() instanceof ScribbleCellCommand) {
                            scribbleCellCommand = (ScribbleCellCommand) commandQueue.poll();
                            points.add(scribbleCellCommand.getPoint());
                        }
                        ScribbleCellCommand command = new ScribbleCellCommand(scribbleCellCommand.getX(), scribbleCellCommand.getY(), points, initialTimestamp);
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

                ArrayList<PollGameDataCommandResponse> response = (ArrayList<PollGameDataCommandResponse>) in.readObject();
                long stop = System.currentTimeMillis();

                currentLatency = (stop - start) / 2;

                for (PollGameDataCommandResponse command : response) {
                    
                	if(command.getPlayingState() == false) { //indicates game is over
		    			System.out.println("code goes here");
		    			winningPlayers = command.getWinningPlayers();
		    			//endGame();
		    			model.endGame(winningPlayers);
		    			System.out.println("GAME HAS COMPLETED, WINNERS ARE:");
		    			break;
		    		} else {

		            	CellPane cell = (CellPane) model.getGrid().getComponentAt(command.getX(), command.getY());
		
		                if (!cell.getDone()) {
		                    cell.setPoints(command.getPoints());
		                    cell.setColor(command.getBrushColor());
		                    cell.setBackground(command.getBackgroundColor());
		                    cell.setOwnerID(command.getOwnerID());
		                    cell.setDone(command.getDone());
		                    cell.repaint();
		                }
		    		}
	            }

                try {
                    TimeUnit.MILLISECONDS.sleep(5);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (Exception ex) {
            if (ex instanceof EOFException | (ex instanceof SocketException && clientInfos.size()!=0)) {
                System.out.println("Server Disconnected. Waiting for game to resume");
                ClientErrorHandler.handleServerDisc(this);
                commandQueue.clear();
                this.init();
            } else {
                ex.printStackTrace();
            }
//            ex.printStackTrace();
        }
//		} finally {
//			System.exit(0);
//		}
    }
}
