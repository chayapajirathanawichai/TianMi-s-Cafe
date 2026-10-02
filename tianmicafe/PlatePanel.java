/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tianmicafe;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PlatePanel extends JPanel{
    private List<Order> items = new ArrayList<>();
    private Image plateImage;
    private int dragStartX, dragStartY;
    
    public PlatePanel() {
        setLayout(null); 
        setOpaque(false);
        try {
            plateImage = new ImageIcon(getClass().getResource("/assets/plate.png")).getImage();
        } catch (Exception e) {
            System.out.println("image not found");
        }
    }

    public void addItem(Order order) {
        if (items.size() < 3) { 
            items.add(order);
            updatePlateDisplay();
        }
    }
    public void clearPlate(){
        items.clear();
        removeAll();
        revalidate();
        repaint();
        updatePlateDisplay();
    }

    private void updatePlateDisplay() {
        removeAll();
        int itemCount = items.size();

        for (int i = 0; i < itemCount; i++) {
            Order item = items.get(i);
            JLabel itemLabel = new JLabel();

            try {
                ImageIcon icon = new ImageIcon(getClass().getResource(item.getImagePath()));              
                Image scaledImg = icon.getImage().getScaledInstance(38, 38, Image.SCALE_SMOOTH);
                itemLabel.setIcon(new ImageIcon(scaledImg));
            } catch (Exception e) {
                itemLabel.setText(item.getName());
            }

            int x = 0, y = 0;

            if (itemCount == 1) {               
                x = 61;
                y = 21;
            } else if (itemCount == 2) {               
                if (i == 0) { x = 38; y = 21; }
                else if (i == 1) { x = 84; y = 21; }
            } else if (itemCount == 3) {
                
                if (i == 0) { x = 35; y = 14; }      
                else if (i == 1) { x = 87; y = 14; } 
                else if (i == 2) { x = 61; y = 28; } 
            }

            itemLabel.setBounds(x, y, 38, 38);
            add(itemLabel);           
            setComponentZOrder(itemLabel, 0); 
        }

        revalidate();
        repaint();
    }
    
    public List<Order> getPlateItems() {
        return items;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (plateImage != null) {
            g.drawImage(plateImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}