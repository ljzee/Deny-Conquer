package command;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

public class ScribbleCellCommand implements Command {
	/**
	 * 
	 */
	int connectionID = -1;
	ArrayList<Point> points;
	Point point;
	
	private static final long serialVersionUID = 1146724334964356307L;
	
	int x;
	int y;
	
	public ScribbleCellCommand(int x, int y, Point point) {
		this.x = x;
		this.y = y;
		//this.points = new ArrayList<Point>(points);
		this.point = point;
	}
	
	public ScribbleCellCommand(int x, int y, ArrayList<Point> points) {
		this.x = x;
		this.y = y;
		this.points = new ArrayList<Point>(points);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public ArrayList<Point> getPoints() {
		return points;
	}
	
	public Point getPoint() {
		return point;
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
