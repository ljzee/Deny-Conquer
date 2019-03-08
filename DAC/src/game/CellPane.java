package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

public class CellPane extends JPanel {

    private Color defaultBackground;
    private int status;
    private ArrayList<Point> points;
    private Color color;

    public CellPane(Color color) {
    	status = 0;
    	points = new ArrayList<Point>();
    	defaultBackground = getBackground();
    	this.color = color;
    	addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
//                defaultBackground = getBackground();
//                setBackground(color);
//                status = 2;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //setBackground(defaultBackground);
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
            	points.add(e.getPoint());
            	System.out.println("MOUSE PRESSED- MOMAN!!!");
            	repaint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
            	//setBackground(defaultBackground);
            	Dimension d = getSize();
            	BufferedImage image = new BufferedImage(d.width,d.height, BufferedImage.TYPE_INT_RGB);
            	Graphics2D iG = image.createGraphics();
            	print(iG);
            	iG.dispose();
            	
            	//getting width/height/area of cell
            	int width = image.getWidth();
            	int height = image.getHeight();
            	float area = width*height;
            	
            	int amountColored = 0;
            	int amountUncolored = 0;
            	
            	for(int x = 0; x < width; ++x) {
            		for(int y = 0; y < height; ++y) {
            			int rgb = image.getRGB(x, y);
            			if(rgb == color.getRGB()) {
            				amountColored++; //how much of cell has been colored
            			} else if(rgb == defaultBackground.getRGB()) {
            				amountUncolored++;
            			} else {
            				//System.out.println(new Color(rgb));
            			}
            		}
            	}
            	
            	if(amountColored/area > 0.6) {
            		status = 2;
            		setBackground(color);
            	} else {
            		clearCell();
            	}

            }
        });
        
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e)
            {  
            	points.add(e.getPoint());
            	status = 1;
                repaint();
            	System.out.println(e.getPoint());
            }
            
        });

    }
    
    @Override
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	if(status != 3) {
	        Graphics2D g2 = (Graphics2D) g;
	        g.setColor(color);
	        g2.setStroke(new BasicStroke(getWidth()/5,
	                                     BasicStroke.CAP_ROUND,
	                                     BasicStroke.JOIN_ROUND));
	        for (int i = 1; i < points.size(); i++) {
	            g2.draw(new Line2D.Float(points.get(i-1), points.get(i)));
	        }
    	}
    }
    
    public ArrayList<Point> getPoints() {
    	return points;
    }
    
    public void scribble(ArrayList<Point> points) {
    	this.points = new ArrayList<Point>(points);
    	status = 1;
        repaint();
    }
    
    public Color getColor() {
    	return color;
    }
    
    public void setColor(Color color) {
    	this.color = color;
    }

    public int getStatus() {
    	return status;
    }
    
    public void setStatus(int status) {
    	this.status = status;
    }

    
    public void clearStatus() {
    	status = 0;
    }
    
    public void clearCell() {
		status = 3;
		points.clear();
		repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(50, 50);
    }
}
