package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JPanel;

import command.ClearCellColorCommand;
import command.Command;
import command.LockCellCommand;
import command.ScribbleCellCommand;

//Stores game state and data of each Cell

public class CellPane extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 649469097148566456L;
	private Color defaultBackground;
    private ArrayList<Point> points;
    private Color color;
    private int ownerID;
    private ConcurrentLinkedQueue<Command> commandQueue;
    private int clientID;
    private boolean done;
    private Long offset; 
    private Long currentLatency;
    private long currentLockTimestamp;
    
    //settings
    private int penThickness;
    private double targetPercentage;
    
    //Server cellpane constructer as server cells should not contain mouse listeners
    public CellPane(Color color , int penThickness, double targetPercentage) {
    	points = new ArrayList<Point>();
    	defaultBackground = getBackground();
    	this.color = color;
    	ownerID = -1;
    	this.done = false;
    	this.offset = new Long(0);
    	this.currentLatency = new Long(0);
    	this.currentLockTimestamp = 0;
    	this.targetPercentage = targetPercentage;
    	this.penThickness = penThickness;
    }
    
    //Client CellPane constructor
    public CellPane(Color color, ConcurrentLinkedQueue<Command> commandQueue, int clientID, Long offset, Long currentLatency, int penThickness, double targetPercentage) {
    	points = new ArrayList<Point>();
    	defaultBackground = getBackground();
    	this.color = color;
    	ownerID = -1;
    	this.commandQueue = commandQueue;
    	this.clientID = clientID;
    	this.done = false;
    	this.offset = offset;
    	this.currentLatency = currentLatency;
    	this.currentLockTimestamp = 0;
    	//settings
    	this.penThickness = penThickness;
    	this.targetPercentage = targetPercentage;

    	//Mouselisteners allow players to modify cells with their mouse, will send game commands to server
    	addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
            	if(ownerID == -1 && !done) {
            		long timestamp = System.currentTimeMillis() + offset.longValue() + currentLatency.longValue();
	            	LockCellCommand command = new LockCellCommand(getX(), getY(), timestamp);
	            	commandQueue.add(command);	         
            	}
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
            	if((ownerID == -1 || ownerID == clientID) && !done) {
            		long timestamp = System.currentTimeMillis() + offset.longValue() + currentLatency.longValue();
	            	ClearCellColorCommand command = new ClearCellColorCommand(getX(), getY(), timestamp);
	            	commandQueue.add(command);
            	}
            }
        });
    	
    	
    	addMouseMotionListener(new MouseAdapter() {
	        @Override
	        public void mouseDragged(MouseEvent e) {
	        	if((ownerID == -1 || ownerID == clientID) && !done) {
            		long timestamp = System.currentTimeMillis() + offset.longValue() + currentLatency.longValue();
		        	ScribbleCellCommand command = new ScribbleCellCommand(getX(), getY(), e.getPoint(), timestamp);
		        	commandQueue.add(command);
	        	}
	        }
    	});
    }
    
    @Override
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	if(ownerID != -1) {
	        Graphics2D g2 = (Graphics2D) g;
	        g.setColor(color);
	        g2.setStroke(new BasicStroke(getWidth()/penThickness,
	                                     BasicStroke.CAP_ROUND,
	                                     BasicStroke.JOIN_ROUND));
	        for (int i = 1; i < points.size(); i++) {
	            g2.draw(new Line2D.Float(points.get(i-1), points.get(i)));
	        }
    	}
    }
    
    public boolean reachedColoredThreshold() {
    	Dimension d = getSize();
    	BufferedImage image = new BufferedImage(d.width,d.height, BufferedImage.TYPE_INT_RGB);
    	Graphics2D iG = image.createGraphics();
    	print(iG);
    	iG.dispose();
    	
    	//getting width/height/area of cell
    	int width = image.getWidth();
    	int height = image.getHeight();
    	float area = width*height;
    	
    	int amountColored = 0;
//    	int amountUncolored = 0;
    	
    	for(int x = 0; x < width; ++x) {
    		for(int y = 0; y < height; ++y) {
    			int rgb = image.getRGB(x, y);
    			if(rgb == color.getRGB()) {
    				amountColored++; //how much of cell has been colored
    			} else if(rgb == defaultBackground.getRGB()) {
//    				amountUncolored++;
    			} else {
    				//System.out.println(new Color(rgb));
    			}
    		}
    	}
    	
    	if(amountColored/area > targetPercentage) { //0.6
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public int getOwnerID() {
    	return ownerID;
    }
    
    public void setOwnerID(int id) {
    	this.ownerID = id;
    }
    
    public long getCurrentLockTimestamp() {
    	return this.currentLockTimestamp;
    }
   
    public void setCurrentLockTimestamp(long timestamp) {
    	this.currentLockTimestamp = timestamp;
    }
    
    public ArrayList<Point> getPoints() {
    	return points;
    }
    
    public void setPoints(ArrayList<Point> points) {
    	this.points = new ArrayList<Point>(points);
    }
    
    public Color getColor() {
    	return color;
    }
    
    public void setColor(Color color) {
    	this.color = color;
    }
    
    public void setDone(boolean b) {
    	this.done = b;
    }
    
    public boolean getDone() {
    	return done;
    }
    
    public void clearCell() {
    	ownerID = -1;
    	currentLockTimestamp = 0;
		points.clear();
		repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(50, 50);
    }
}
