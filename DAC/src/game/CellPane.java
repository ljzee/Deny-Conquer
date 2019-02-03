package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class CellPane extends JPanel {

    private Color defaultBackground;
    private int status = 0;

    public CellPane(Color color) {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                defaultBackground = getBackground();
                setBackground(color);
                status = 1;
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //setBackground(defaultBackground);
            }
        });
    }
    
    public int getStatus() {
    	return status;
    }
    
    public void clearStatus() {
    	status = 0;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(50, 50);
    }
}