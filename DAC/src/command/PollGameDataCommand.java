package command;

import java.awt.Color;
import java.io.Serializable;

public class PollGameDataCommand implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1146724334964356307L;
	int x;
	int y;
	Color color;
	
	public PollGameDataCommand(int x, int y, Color color) {
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
}
