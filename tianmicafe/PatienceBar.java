/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tianmicafe;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JProgressBar;

public class PatienceBar extends JProgressBar{
    public PatienceBar() {
        super(0, 100);
        setValue(100);
        setOpaque(false);
        setBorderPainted(false);
        setStringPainted(false); 
        setForeground(new Color(46, 204, 113)); 
    }

    public void updatePatience(int currentVal) {
        setValue(currentVal);

        if (currentVal > 60) {
            setForeground(new Color(46, 204, 113)); 
        } else if (currentVal > 30) {
            setForeground(new Color(241, 196, 15)); 
        } else {
            setForeground(new Color(231, 76, 60));  
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int cornerRadius = 15; 

        
        g2.setColor(new Color(220, 220, 220));
        g2.fill(new RoundRectangle2D.Float(0, 0, width, height, cornerRadius, cornerRadius));

        
        double progress = (double) getValue() / getMaximum();
        int fillWidth = (int) (width * progress);

        if (fillWidth > 0) {
            g2.setColor(getForeground());
            g2.fill(new RoundRectangle2D.Float(0, 0, fillWidth, height, cornerRadius, cornerRadius));
        }

        
        g2.setColor(new Color(120, 80, 40));
        g2.setStroke(new BasicStroke(2));
        g2.draw(new RoundRectangle2D.Float(0, 0, width - 1, height - 1, cornerRadius, cornerRadius));

        g2.dispose();
    }
}
