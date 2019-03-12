package main;

public class GameHandler {
    public static LocalGameSession handleGameMode(int mode) {
        LocalGameSession gs;
        IGame game;

        if (mode == 1) {
            game = new ServerGame();
        } else {
            game = new ClientGame();
        }

        gs = new LocalGameSession(game);

        return gs;
    }
}
