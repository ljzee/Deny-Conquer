package main;

import networking.Client.*;

import java.io.*;

public class ClientGame implements IGame {
    private Client client;

    public ClientGame() {
        this.client = new Client();
    }

    @Override
    public void run() throws IOException {
        int count = 0;
        int maxTries = 10;
        boolean isTimeout = false;
        while (!isTimeout) {
            try {
                client.init();
                break;

            } catch (Exception ex) {
                System.out.println("Cannot find server, " +
                        (maxTries - count) + " attempt(s) remaining ");

                if (++count == maxTries) {
                    System.out.println("Timeout. Goodbye!");
                    ex.printStackTrace();
                    isTimeout = true;
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
    }
}
