package networking;

import java.awt.Color;
import java.awt.Component;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;

import game.Test;
import game.TestPane;

/**
* Written by Martin Ombura Jr. <@martinomburajr>
*/
public class Server {
    public static void main(String[] args) {
        connectToServer();
    }

    public static void connectToServer() {
        //Try connect to the server on an unused port eg 9991. A successful connection will return a socket
        try(ServerSocket serverSocket = new ServerSocket(9991)) {
            Socket connectionSocket = serverSocket.accept();

            //Create Input&Outputstreams for the connection
            InputStream inputToServer = connectionSocket.getInputStream();
            OutputStream outputFromServer = connectionSocket.getOutputStream();

            ObjectInputStream serverIn = new ObjectInputStream(inputToServer);
//            PrintWriter serverPrintOut = new PrintWriter(new OutputStreamWriter(outputFromServer, "UTF-8"), true);

            ObjectOutputStream serverOut = new ObjectOutputStream(outputFromServer);
            
            //serverOut.println("Hello World! Enter Peace to exit.");

            //Have the server take input from the client and echo it back
            //This should be placed in a loop that listens for a terminator text e.g. bye
            boolean done = false;
            

		    ArrayList<String> commands = new ArrayList<String>();
		    
            
            Test t = new Test(Color.BLUE);

            while(!done) {
                Object command = null;
				try {
					command = serverIn.readObject();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
               if(command instanceof String) {
            	   String line = (String) command;
            	   if(line.toLowerCase().trim().equals("peace")) {
                       done = true;
                   }

                   if(line.startsWith("Color ")) {
               	   String[] coordinates = line.trim().substring(6).split("\\s+");
               	   
               	   int x = Integer.parseInt(coordinates[0]);
               	   int y = Integer.parseInt(coordinates[1]);
               	   
               	   t.getGrid().getComponentAt(x, y).setBackground(Color.RED);
               	   serverOut.writeObject("Successfully colored!");
                  } else if(line.startsWith("Poll")) {
                	  serverOut.writeObject(t.pollGameData());
                  }
               }
               
               
               //System.out.println("dsafs");
//               commands = t.getBlueCells();
//               //System.out.println(commands);
//		    	if(!commands.isEmpty()) {
//		    		for(String command : commands) {
//		    			System.out.println(command);
//		    			serverPrintOut.println(command);
//		    		}
//		    	}
               
                
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
