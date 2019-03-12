package main;

public class Starter {
    public static void main(String args[]){
        System.out.println("---  Welcome to Deny and Conquer IGame  ---");
        System.out.println("Please choose to the following options: 1.Server, 2.Client");
        LocalGameSession gs = GameHandler.handleGameMode(UserInput.getMode());
        gs.run();
    }
}
