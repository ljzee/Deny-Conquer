package game;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import javax.swing.JFrame; //imports JFrame library
import javax.swing.JButton; //imports JButton library
import java.awt.GridLayout; //imports GridLayout library

public class Model {
	 
    JFrame frame=new JFrame(); //creates frame
    JButton[][] grid; //names the grid of buttons

    public Model(int width, int length){ //constructor
            frame.setLayout(new GridLayout(width,length)); //set layout
            grid=new JButton[width][length]; //allocate the size of grid
            for(int y=0; y<length; y++){
                    for(int x=0; x<width; x++){
                            grid[x][y]=new JButton("("+x+","+y+")"); //creates new button     
                            frame.add(grid[x][y]); //adds button to grid
                    }
            }
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack(); //sets appropriate size for frame
            frame.setVisible(true); //makes frame visible
    }
    public static void main(String[] args) {
            new Model(3,3);//makes new ButtonGrid with 2 parameters
    }
}