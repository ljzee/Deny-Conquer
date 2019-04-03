package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

import command.ClearCellColorCommand;
import command.Command;
import command.PollGameDataCommandResponse;
import command.ScribbleCellCommand;
import command.UpdateCellColorCommand;

public class Model {
	
	private JFrame frame;
	private Grid grid;
	private boolean playingState;


    public static void main(String[] args) {
    	  
    }
    
//    public ArrayList<Command> getUpdatedState() {
//		ArrayList<Command> gameCommands = new ArrayList<Command>();
//		
//		Component[] cells = (Component[])grid.getComponents();
//		
//		for(Component c : cells) {
//			CellPane cell = (CellPane)c;
//			if(!cell.getIsModified()) {
//				continue;
//			} else if(cell.getStatus() == 1) {
////				ScribbleCellCommand command = new ScribbleCellCommand(cell.getLocation().x, cell.getLocation().y, cell.getPoints());
////				gameCommands.add(command);
//				cell.setIsModified(false);
//			} else if(cell.getStatus() == 2) {
//				UpdateCellColorCommand command = new UpdateCellColorCommand(cell.getLocation().x, cell.getLocation().y);
//				gameCommands.add(command);
//				cell.setIsModified(false);
//			} else if(cell.getStatus() == 3) {
//				ClearCellColorCommand command = new ClearCellColorCommand(cell.getLocation().x, cell.getLocation().y);
//				gameCommands.add(command);
//				cell.setIsModified(false);
//			}
//		}
//		return gameCommands;
//	}
//    
    public ArrayList<PollGameDataCommandResponse> pollGameData() {
		ArrayList<PollGameDataCommandResponse> gameData = new ArrayList<PollGameDataCommandResponse>();
		
		Component[] cells = null;
		cells = (Component[])grid.getComponents();
		
		for(Component c : cells) {
			CellPane cell = (CellPane)c;
			
			int x = cell.getLocation().x;
			int y = cell.getLocation().y;
			Color backgroundColor = cell.getBackground();
			Color brushColor = cell.getColor();
			ArrayList<Point> points = cell.getPoints();
			int ownerID = cell.getOwnerID();
			boolean done = cell.getDone();
			
			gameData.add(new PollGameDataCommandResponse(x,y,backgroundColor,brushColor, points, ownerID, done, true));
		}
		return gameData;
	}

    public Model(Color color, int numBoxes, int penThickness, double targetPercentage) {
    	try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        }

        frame = new JFrame("TestingServer");
        grid = new Grid(color, numBoxes, penThickness, targetPercentage); 
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(grid);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        playingState = true;
    }
    
    
    public Model(Color color, ConcurrentLinkedQueue<Command> commandQueue, int clientID, Long offset, Long currentLatency, int penThickness, int numBoxes, double targetPercentage) {
    	try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        }

        frame = new JFrame("Testing");
        grid = new Grid(color, commandQueue, clientID, offset, currentLatency, penThickness, numBoxes, targetPercentage); 
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(grid);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        playingState = true;
    }

    public Model(Model model, int numBoxes, int penThickness, double targetPercentage) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        }

        frame = new JFrame("Server");
        grid = new Grid(Color.BLACK, numBoxes, penThickness, targetPercentage);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(grid);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        Component[] newCells = (Component[])grid.getComponents();
        Component[] oldCells = (Component[])model.grid.getComponents();

        for(int i = 0; i < oldCells.length; ++i) {
            CellPane oldCell = (CellPane)oldCells[i];
            CellPane newCell = (CellPane)newCells[i];

            newCell.setBackground(oldCell.getBackground());
            newCell.setColor(oldCell.getColor());
            newCell.setPoints(oldCell.getPoints());
            newCell.setOwnerID(oldCell.getOwnerID());
            newCell.setDone(oldCell.getDone());
            newCell.repaint();
        }
        
        playingState = true;
    }
    
    public void endGame(List<String> winningPlayers) {
    	
    	String endGameMsg = "Terminating game..." + System.lineSeparator() + "Winning Players: " + System.lineSeparator();
    	int size = winningPlayers.size();
    	for(int i = 0; i < size - 1; i++) {
    		endGameMsg = endGameMsg +  "Player " + winningPlayers.get(i) + ", with a score of " + winningPlayers.get(size - 1) + System.lineSeparator();
    	}
        frame.setVisible(false);
        
        //display message dialog with information
        JOptionPane.showMessageDialog(null, endGameMsg);
    }
    
    public JFrame getFrame() {
    	return frame;
    }
    
    public Grid getGrid() {
//    	JRootPane rootPane = (JRootPane)t.getFrame().getComponent(0);
//		JLayeredPane layeredPanel = (JLayeredPane)rootPane.getComponent(1);
//		JPanel panel = (JPanel)layeredPanel.getComponent(0);
    	
    	return grid;
    }

    public void clear(){
        Component[] cells = (Component[])this.grid.getComponents();

        for(Component c : cells) {
            CellPane cell = (CellPane)c;
            if(!cell.getDone()) {
                cell.clearCell();
            }
        }
    }
    
    public boolean getPlayingState() {
    	return playingState;
    }
    
    public void endPlayingState() {
    	playingState = false;
    }

}