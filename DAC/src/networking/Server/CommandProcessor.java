package networking.Server;

import command.*;
import game.*;

import java.awt.Component;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CommandProcessor {
    public static void processCommands(ConcurrentLinkedQueue<Command> commandQueue, ArrayList<ClientConnection> connections, Model model) {
        while (!commandQueue.isEmpty()) {
            GameplayCommands command = (GameplayCommands) commandQueue.poll();
            int x = command.getX();
            int y = command.getY();

            ClientConnection connection = findConnByID(connections, command.getConnectionID());
            Grid grid = (Grid) model.getGrid();


//          ClientConnection connection = connections.get(command.getConnectionID());
            CellPane cell = (CellPane) model.getGrid().getComponentAt(x, y);

            if (command instanceof LockCellCommand) {
                LockCell(command, x, y, connection, cell);
            } else if (command instanceof ScribbleCellCommand) {
                ScribbleCell((ScribbleCellCommand) command, x, y, cell);
            } else if (command instanceof ClearCellColorCommand) {
                if (cell.reachedColoredThreshold()) {
                    ReachColorThreshold(command, connection, cell);
                    
                  //Determine winner                    
                    if(allCellsColored(grid)) {
                    	System.out.print("ALL CELLS COLORED!!");
                    	model.endPlayingState();
                    	//endGame(winningPlayer);
                    	//endgame for server maybe?
                    }

                } else {
                    cell.clearCell();
                    //System.out.println(command.getConnectionID() + " - Successfully cleared! " + x + " " + y);
                }
            }
        }
    }

    private static ClientConnection findConnByID(ArrayList<ClientConnection> connections, int commandConnID ){
        for(ClientConnection c:connections){
            if(c.connectionID == commandConnID){
                return c;
            }
        }
        return null;
    }

    private static void ReachColorThreshold(GameplayCommands command, ClientConnection connection, CellPane cell) {
        int ownerID = cell.getOwnerID();
        cell.clearCell();
        cell.setOwnerID(ownerID);
        cell.setBackground(connection.playerColor);
        cell.setDone(true);
        System.out.println(command.getConnectionID() + " Clear TS: " + command.getTimeStamp());
        //System.out.println(command.getConnectionID() + " - Successfully colored! " + x + " " + y);
    }

    private static void ScribbleCell(ScribbleCellCommand command, int x, int y, CellPane cell) {
        if (cell.getOwnerID() == command.getConnectionID()) {
            cell.getPoints().addAll(command.getPoints());
            cell.repaint();
            System.out.println(command.getConnectionID() + " Scribble TS: " + command.getTimeStamp());
            //System.out.println(cell.getPoints().size() + " " + command.getPoints().size());
            //System.out.println(command.getConnectionID() + " - Successfully scribbled! " + x + " " + y);
        } else {
            //System.out.println(command.getConnectionID() + " - Failed scribbled!: client ID: " + cell.getOwnerID() + " has locked this cell.");
        }
    }

    private static void LockCell(GameplayCommands command, int x, int y, ClientConnection connection, CellPane cell) {
        if (cell.getOwnerID() == -1 || (command.getTimeStamp() < cell.getCurrentLockTimestamp())) {
            cell.setOwnerID(command.getConnectionID());
            cell.setColor(connection.playerColor);
            System.out.println(command.getConnectionID() + " Lock TS: " + command.getTimeStamp());
            //System.out.println(command.getConnectionID() + " - Successfully locked! " + x + " " + y);
        } else {
            //System.out.println(command.getConnectionID() + " - Failed lock!: client ID: " + cell.getOwnerID() + " has already locked this cell.");
        }
    }
    
    private static boolean allCellsColored(Grid grid) {
    	//int width = grid.getWidth();
    	int numComponenents = grid.getComponentCount();
    	int countBoxes = 0;
    	Component[] cells = null;
		cells = (Component[])grid.getComponents();
		
    	for(Component c : cells) {
			CellPane cell = (CellPane)c;
            if(cell.getDone()) {
            	countBoxes++;
            }	
        }
        
    	//int numBoxes = width*width;
    	if(countBoxes == numComponenents) {
    		return true;
    	} else {
        	return false;
    	}
    }

}