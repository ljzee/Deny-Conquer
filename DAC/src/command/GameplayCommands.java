package command;

//Game commands interface

public interface GameplayCommands extends Command {
    public int getX();
    public int getY();
    public long getTimeStamp();
    public void setTimeStamp(long timeStamp);
    public int getConnectionID();
    public void setConnectionID(int connectionID);
}
