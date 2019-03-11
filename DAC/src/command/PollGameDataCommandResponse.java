package command;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

public class PollGameDataCommandResponse implements GameplayCommands {
	/**
	 * 
	 */
	private int connectionID = -1;
	
	private static final long serialVersionUID = 1146724334964356307L;
	
	private int x;
	private int y;
	private Color backgroundColor;
	private Color brushColor;
	private ArrayList<Point> points;
	private int ownerID;
	private boolean done;
	
	public PollGameDataCommandResponse(int x, int y, Color backgroundColor, Color brushColor, ArrayList<Point> points, int ownerID, boolean done) {
		this.x = x;
		this.y = y;
		this.backgroundColor = backgroundColor;
		this.brushColor = brushColor;
		this.points = new ArrayList<Point>(points);
		this.ownerID = ownerID;
		this.done = done;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Color getBrushColor() {
		return brushColor;
	}
	
	public Color getBackgroundColor() {
		return backgroundColor;
	}
	
	public ArrayList<Point> getPoints() {
		return points;
	}
	
	public int getOwnerID() {
		return ownerID;
	}
	
	public boolean getDone() {
		return done;
	}

	public int getTimeStamp() {
		return 0;
	}
	
	public void setTimeStamp(int timeStamp) {
		// TODO Auto-generated method stub
		
	}

	public int getConnectionID() {
		return connectionID;
	}

	public void setConnectionID(int connectionID) {
		this.connectionID = connectionID;
	}
}
