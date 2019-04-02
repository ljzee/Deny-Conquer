package command;

import java.awt.Color;
import java.io.Serializable;

public class LockCellCommand implements GameplayCommands {
	/**
	 * 
	 */
	int connectionID = -1;
	
	private static final long serialVersionUID = 1146724334964356307L;
	
	int x;
	int y;
	long timestamp;
	
	public LockCellCommand(int x, int y, long timestamp) {
		this.x = x;
		this.y = y;
		this.timestamp = timestamp;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}


	public long getTimeStamp() {
		return this.timestamp;
	}
	
	public void setTimeStamp(long timeStamp) {
		// TODO Auto-generated method stub
		this.timestamp = timeStamp;
		
	}

	public int getConnectionID() {
		return connectionID;
	}

	public void setConnectionID(int connectionID) {
		this.connectionID = connectionID;
	}
}
