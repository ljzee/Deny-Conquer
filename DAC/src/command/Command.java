package command;

import java.io.Serializable;

public interface Command extends Serializable {
	public long getTimeStamp();
	public void setTimeStamp(long timeStamp);
	public int getConnectionID();
	public void setConnectionID(int connectionID);
}
