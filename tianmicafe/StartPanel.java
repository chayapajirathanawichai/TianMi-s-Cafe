/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tianmicafe;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class StartPanel extends JPanel{
    private Image backgroundImage;
    private MainFrame mainFrame;

    public StartPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        try {
            backgroundImage = new ImageIcon(getClass().getResource("/assets/tianmi cafe.png")).getImage();
        } catch (Exception e) {
            System.out.println("Error loading background image: " + e.getMessage());
        }
        setPreferredSize(new Dimension(1024, 576));
        setLayout(null);
        
        //edit button
        JButton startButton = createCustomButton("START", 310, 360, 200, 80);
        JButton exitButton = createCustomButton("EXIT", 520, 360, 200, 80);     
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showScreen("GameScreen");
                System.out.println("Start Game!");
            }
        });
        exitButton.addActionListener(e -> System.exit(0));
        add(startButton);
        add(exitButton);
        SoundManager.playBGM("cafe_intro");
    }

    
    private JButton createCustomButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);

        
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/assets/buttonwood01.png"));
            Image scaledImg = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImg));
        } catch (Exception e) {
            System.out.println("Error loading button icon: " + e.getMessage());
        }

        
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.CENTER);

        
        button.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        button.setForeground(new Color(90, 50, 20));

        
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);

        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
