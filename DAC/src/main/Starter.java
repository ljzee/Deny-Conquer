package main;

import java.io.IOException;

//Command line interface for starting the game

public class Starter {
    public static void main(String args[]) throws IOException {
        System.out.println("---  Welcome to Deny and Conquer IGame  ---");
        System.out.println("Please choose to the following options: 1.Server, 2.Client");
        int mode = UserInput.getMode();


        int penThickness = 0;
        int numBoxes = 0;
        double targetPercentage = 0;
        
        if(mode == 1) {
        	//server configures settings
        	System.out.println("Please set the pen thickness (from 1 to 10):");
            penThickness = UserInput.getPenThickness();
            System.out.println("Please set the width for the X by X grid (from 4 to 8):");
            numBoxes = UserInput.getNumberOfBoxes();
            System.out.println("Please set the x% needed to color a cell (from 0.1 to 1):");
            targetPercentage = UserInput.getTargetPercentage();     
        }

        LocalGameSession gs = GameHandler.handleGameMode(mode, penThickness, numBoxes, targetPercentage);
        gs.run();
    }
}
