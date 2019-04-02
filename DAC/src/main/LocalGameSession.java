package main;

import java.io.IOException;

public class LocalGameSession {
    private IGame game;

    public LocalGameSession(IGame g) {
        game = g;
    }

    public void run() throws IOException {
        game.run();
    }
}
