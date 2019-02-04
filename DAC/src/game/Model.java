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

import command.PollGameDataCommand;

public class Model {
	
	private JFrame frame;
	private Grid grid;

    public static void main(String[] args) {
    	
//    	Test t = new Test();
//		TestPane grid = t.getGrid();
//		
//		
//		System.out.println(grid.getComponent(1).getClass());
//		System.out.println(grid.getComponent(1).getName());
//		
//		Component[] cells = (Component[])grid.getComponents();
//		
//		int blueCells = 0;
//		System.out.println(grid.getComponents().getClass());
//		
//    try {
//		TimeUnit.SECONDS.sleep(5);
//	} catch (InterruptedException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
		       
    }
    
    public ArrayList<String> getBlueCells() {
		ArrayList<String> blueCells = new ArrayList<String>();
		
		Component[] cells = (Component[])grid.getComponents();
		
		StringBuilder cellLocationCommand = new StringBuilder();
		
		for(Component c : cells) {
			CellPane cell = (CellPane)c;
			if(cell.getStatus() != 0) {
				cellLocationCommand.append("Color ");
				
				cellLocationCommand.append(cell.getLocation().x);
				cellLocationCommand.append(" ");
				cellLocationCommand.append(cell.getLocation().y);
				blueCells.add(cellLocationCommand.toString());
				
				cell.clearStatus();
				
				cellLocationCommand.setLength(0);
			}
		}
		return blueCells;
	}
    
    public ArrayList<PollGameDataCommand> pollGameData() {
		ArrayList<PollGameDataCommand> gameData = new ArrayList<PollGameDataCommand>();
		
		Component[] cells = (Component[])grid.getComponents();
		
		for(Component c : cells) {
			CellPane cell = (CellPane)c;
			
			int x = cell.getLocation().x;
			int y = cell.getLocation().y;
			Color color = cell.getBackground();

			gameData.add(new PollGameDataCommand(x,y,color));
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
    	
//        EventQueue.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
//                }
//
//                JFrame frame = new JFrame("Testing");
//                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                frame.setLayout(new BorderLayout());
//                frame.add(new TestPane());
//                frame.pack();
//                frame.setLocationRelativeTo(null);
//                frame.setVisible(true);
//                
////                try {
////					TimeUnit.SECONDS.sleep(10);
////				} catch (InterruptedException e) {
////					// TODO Auto-generated catch block
////					e.printStackTrace();
////				}
////                
////                JRootPane grid = (JRootPane)frame.getComponent(0);
////                
////                Component[] cells = (Component[])grid.getComponents();
////                
////                int blueCells = 0;
////                
////                for(CellPane cell : cells) {
////                	if(cell.getBackground().equals(Color.BLUE)) {
////                		blueCells++;
////                	}
////                }
////                
////                System.out.println("Blue cells: " + blueCells);
//                
//                
//            }
//        });
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