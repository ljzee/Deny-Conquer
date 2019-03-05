package command;

import java.io.Serializable;

public interface Command extends Serializable {
	public int getTimeStamp();
	public void setTimeStamp(int timeStamp);
	public int getConnectionID();
	public int setConnectionID(int connectionID);
}
