package main;

class GameHandler {
    static LocalGameSession handleGameMode(int mode, int numBoxes, int penThickness, double targetPercentage) {
        LocalGameSession gs;
        IGame game;

        if (mode == 1) {
            game = new ServerGame(9991, numBoxes, penThickness, targetPercentage);
        } else {
            game = new ClientGame();
        }

        gs = new LocalGameSession(game);

        return gs;
    }
}
