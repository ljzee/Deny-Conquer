package command;

import java.awt.Color;

public class PollGameDataCommandResponse implements Command {
	/**
	 * 
	 */
	int connectionID = -1;
	
	private static final long serialVersionUID = 1146724334964356307L;
	
	int x;
	int y;
	Color color;
	
	public PollGameDataCommandResponse(int x, int y, Color color) {
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Color getColor() {
		return color;
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

	public int setConnectionID(int connectionID) {
		this.connectionID = connectionID;
		return 0;
	}
}
