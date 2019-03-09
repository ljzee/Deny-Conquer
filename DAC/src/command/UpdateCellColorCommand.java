package command;

import java.awt.Color;
import java.io.Serializable;

public class UpdateCellColorCommand implements Command {
	/**
	 * 
	 */
	int connectionID = -1;
	
	private static final long serialVersionUID = 1146724334964356307L;
	
	int x;
	int y;
	
	public UpdateCellColorCommand(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
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
