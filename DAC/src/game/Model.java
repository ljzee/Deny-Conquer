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
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
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

    public static void main(String[] args) {
    	  
    }
    
    public ArrayList<Command> getUpdatedState() {
		ArrayList<Command> gameCommands = new ArrayList<Command>();
		
		Component[] cells = (Component[])grid.getComponents();
		
		for(Component c : cells) {
			CellPane cell = (CellPane)c;
			if(cell.getStatus() == 0) {
				continue;
			} else if(cell.getStatus() == 1) {
				ScribbleCellCommand command = new ScribbleCellCommand(cell.getLocation().x, cell.getLocation().y, cell.getPoints());
				gameCommands.add(command);
				cell.clearStatus();
			} else if(cell.getStatus() == 2) {
				UpdateCellColorCommand command = new UpdateCellColorCommand(cell.getLocation().x, cell.getLocation().y);
				gameCommands.add(command);
				cell.clearStatus();
			} else if(cell.getStatus() == 3) {
				ClearCellColorCommand command = new ClearCellColorCommand(cell.getLocation().x, cell.getLocation().y);
				gameCommands.add(command);
				cell.clearStatus();
			}
		}
		return gameCommands;
	}
    
    public ArrayList<PollGameDataCommandResponse> pollGameData() {
		ArrayList<PollGameDataCommandResponse> gameData = new ArrayList<PollGameDataCommandResponse>();
		
		Component[] cells = (Component[])grid.getComponents();
		
		for(Component c : cells) {
			CellPane cell = (CellPane)c;
			
			int x = cell.getLocation().x;
			int y = cell.getLocation().y;
			Color color = cell.getBackground();

			gameData.add(new PollGameDataCommandResponse(x,y,color));
		}
		return gameData;
	}

    public Model(Color color) {
    	try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        }

        frame = new JFrame("Testing");
        grid = new Grid(color); 
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(grid);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
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

}