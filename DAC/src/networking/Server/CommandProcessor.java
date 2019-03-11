package networking.Server;

import command.*;
import game.*;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CommandProcessor {
    public static void processCommands(ConcurrentLinkedQueue<Command> commandQueue, ArrayList<ClientConnection> connections, Model model) {
        while (!commandQueue.isEmpty()) {
            GameplayCommands command = (GameplayCommands) commandQueue.poll();
            int x = command.getX();
            int y = command.getY();
            ClientConnection connection = connections.get(command.getConnectionID());
            CellPane cell = (CellPane) model.getGrid().getComponentAt(x, y);

            if (command instanceof LockCellCommand) {
                if (cell.getOwnerID() == -1) {
                    cell.setOwnerID(command.getConnectionID());
                    cell.setColor(connection.playerColor);
                    System.out.println(command.getConnectionID() + " - Successfully locked! " + x + " " + y);
                } else {
                    System.out.println(command.getConnectionID() + " - Failed lock!: client ID: " + cell.getOwnerID() + " has already locked this cell.");
                }
            } else if (command instanceof ScribbleCellCommand) {
                var cmd = (ScribbleCellCommand)command;
                if (cell.getOwnerID() == cmd.getConnectionID()) {
                    cell.getPoints().addAll(cmd.getPoints());
                    cell.repaint();
                    System.out.println(cell.getPoints().size() + " " + cmd.getPoints().size());
                    System.out.println(cmd.getConnectionID() + " - Successfully scribbled! " + x + " " + y);
                } else {
                    System.out.println(cmd.getConnectionID() + " - Failed scribbled!: client ID: " + cell.getOwnerID() + " has locked this cell.");
                }
            } else if (command instanceof ClearCellColorCommand) {
                if (cell.reachedColoredThreshold()) {
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


//        while (!commandQueue.isEmpty()) {
//            Command command = commandQueue.poll();
//            if (command instanceof LockCellCommand) {
//                LockCellCommand lockCellCommand = (LockCellCommand) command;
//
//                int x = lockCellCommand.getX();
//                int y = lockCellCommand.getY();
//
//                ClientConnection connection = connections.get(command.getConnectionID());
//
//                CellPane cell = (CellPane) model.getGrid().getComponentAt(x, y);
//
//                if (cell.getOwnerID() == -1) {
//                    cell.setOwnerID(command.getConnectionID());
//                    cell.setColor(connection.playerColor);
//                    System.out.println(command.getConnectionID() + " - Successfully locked! " + x + " " + y);
//                } else {
//                    System.out.println(command.getConnectionID() + " - Failed lock!: client ID: " + cell.getOwnerID() + " has already locked this cell.");
//                }
//            } else if (command instanceof ScribbleCellCommand) {
//                ScribbleCellCommand scribbleCellCommand = (ScribbleCellCommand) command;
//
//                int x = scribbleCellCommand.getX();
//                int y = scribbleCellCommand.getY();
//
//                ClientConnection connection = connections.get(command.getConnectionID());
//
//                CellPane cell = (CellPane) model.getGrid().getComponentAt(x, y);
//
//                if (cell.getOwnerID() == command.getConnectionID()) {
//                    cell.getPoints().addAll(scribbleCellCommand.getPoints());
//                    cell.repaint();
//                    System.out.println(cell.getPoints().size() + " " + scribbleCellCommand.getPoints().size());
//                    System.out.println(command.getConnectionID() + " - Successfully scribbled! " + x + " " + y);
//                } else {
//                    System.out.println(command.getConnectionID() + " - Failed scribbled!: client ID: " + cell.getOwnerID() + " has locked this cell.");
//                }
//            } else if (command instanceof ClearCellColorCommand) {
//                ClearCellColorCommand clearCellColorCommand = (ClearCellColorCommand) command;
//
//                int x = clearCellColorCommand.getX();
//                int y = clearCellColorCommand.getY();
//
//                ClientConnection connection = connections.get(command.getConnectionID());
//
//                CellPane cell = (CellPane) model.getGrid().getComponentAt(x, y);
//
//                if (cell.reachedColoredThreshold()) {
//                    int ownerID = cell.getOwnerID();
//                    cell.clearCell();
//                    cell.setOwnerID(ownerID);
//                    cell.setBackground(connection.playerColor);
//                    cell.setDone(true);
//                    System.out.println(command.getConnectionID() + " - Successfully colored! " + x + " " + y);
//                } else {
//                    cell.clearCell();
//                    System.out.println(command.getConnectionID() + " - Successfully cleared! " + x + " " + y);
//                }
//            }
        }
    }
}