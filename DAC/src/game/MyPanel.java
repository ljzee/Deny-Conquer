package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class MyPanel extends JPanel implements MouseMotionListener
{  
    int x1,y1;
    public MyPanel()
    {
// to listen the mouse events (mouse dragging and mouse moving)
    addMouseMotionListener(this);
  
    }
  
    public void paint(Graphics g)
    {

        g.setColor(Color.black);          
        g.fillOval(x1,y1,15,15);
    }

// this method is invoked when we drag the mouse
    public void mouseDragged(MouseEvent me)
    {  
      
        x1=me.getX(); // to save the mouse pointer x-coordinate while dragging.
        y1=me.getY(); // to save the mouse pointer y-coordinate while dragging.
      
        repaint();
    }
    public void mouseMoved(MouseEvent me)
    {

    }
}