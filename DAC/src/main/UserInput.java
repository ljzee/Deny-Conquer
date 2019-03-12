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
                continue;
            } catch (NumberFormatException ex) {
                System.out.println("Input is invalid, please enter again");
            }
        }
    }
}
