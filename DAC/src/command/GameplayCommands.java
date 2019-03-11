package command;

public interface GameplayCommands extends Command {
    public int getX();
    public int getY();
    public int getTimeStamp();
    public void setTimeStamp(int timeStamp);
    public int getConnectionID();
    public void setConnectionID(int connectionID);
}
