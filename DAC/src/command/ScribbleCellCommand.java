package command;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

public class ScribbleCellCommand implements GameplayCommands {
	/**
	 * 
	 */
	int connectionID = -1;
	ArrayList<Point> points;
	Point point;
	long timestamp;
	
	private static final long serialVersionUID = 1146724334964356307L;
	
	int x;
	int y;
	
	public ScribbleCellCommand(int x, int y, Point point, long timestamp) {
		this.x = x;
		this.y = y;
		this.point = point;
		this.timestamp = timestamp;
	}
	
	public ScribbleCellCommand(int x, int y, ArrayList<Point> points, long timestamp) {
		this.x = x;
		this.y = y;
		this.points = new ArrayList<Point>(points);
		this.timestamp = timestamp;
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



	public long getTimeStamp() {
		return this.timestamp;
	}
	
	public void setTimeStamp(long timeStamp) {
		this.timestamp = timeStamp;
	}

	public int getConnectionID() {
		return connectionID;
	}

	public void setConnectionID(int connectionID) {
		this.connectionID = connectionID;
	}
}
