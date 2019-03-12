package main;

public class LocalGameSession {
    private IGame game;
    public LocalGameSession(IGame g){
        game = g;
    }
    public void run(){
        game.run();
    }
}
