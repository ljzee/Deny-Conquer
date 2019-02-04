package game.demos;

import javax.swing.*;
public class PaintBrush extends JFrame
{

      // initializing the frame
 public PaintBrush()
{
     setVisible(true);
     setTitle("Paint Brush");
     setSize(400,400);
     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}
 public static void main(String args[])
{
       PaintBrush pbrush=new PaintBrush();
       MyPanel p = new MyPanel();
}
}