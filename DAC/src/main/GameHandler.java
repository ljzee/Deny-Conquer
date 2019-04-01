package main;

class GameHandler {
    static LocalGameSession handleGameMode(int mode) {
        LocalGameSession gs;
        IGame game;

        if (mode == 1) {
            game = new ServerGame(9991);
        } else {
            game = new ClientGame();
        }

        gs = new LocalGameSession(game);

        return gs;
    }
}
