package main;

import java.util.Scanner;

public class UserInput {
    public static int getMode() {
        Scanner in = new Scanner(System.in);
        int mode;
        while (true) {
            try {
                mode = Integer.parseInt(in.nextLine());
                if(mode ==1 || mode ==2){
                    return mode;
                }
                System.out.println("Option does not exist, please enter again");
            } catch (NumberFormatException ex) {
                System.out.println("Input is invalid, please enter again");
            }
        }
    }
    
    public static int getNumberOfBoxes() {
        Scanner in = new Scanner(System.in);
        int numBoxes;
        while (true) {
            try {
                numBoxes = Integer.parseInt(in.nextLine());
                if(numBoxes >= 2 && numBoxes <= 10){
                    return numBoxes;
                }
                System.out.println("Option does not exist, please enter a value between 4 and 10");
                continue;
            } catch (NumberFormatException ex) {
                System.out.println("Input is invalid, please enter again");
            }
        }
    }
    
    public static int getPenThickness() {
        Scanner in = new Scanner(System.in);
        int penThickness;
        while (true) {
            try {
                penThickness = Integer.parseInt(in.nextLine());
                if(penThickness >= 1 && penThickness <= 10){
                    return penThickness;
                }
                System.out.println("Option does not exist, please enter a value between 1 and 10");
                continue;
            } catch (NumberFormatException ex) {
                System.out.println("Input is invalid, please enter again");
            }
        }
    }
    
    public static double getTargetPercentage() {
        Scanner in = new Scanner(System.in);
        double amountCovered;
        while (true) {
            try {
                amountCovered = Double.parseDouble(in.nextLine());
                if(amountCovered >= 0.1 && amountCovered < 1.0){
                    return amountCovered;
                }
                System.out.println("Option does not exist, please enter a value between 0.1 and 1");
                continue;
            } catch (NumberFormatException ex) {
                System.out.println("Input is invalid, please enter again");
            }
        }
    }

}
