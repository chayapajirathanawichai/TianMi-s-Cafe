/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tianmicafe;

import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame{
    private CardLayout cardLayout;
    private JPanel mainContainer;
    private GamePanel gamePanel;
    private ResultPanel resultPanel;
    
    //constructor
    public MainFrame() {
        setTitle("TianMi's Cafe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);       
        //page
        StartPanel startPanel = new StartPanel(this);
        gamePanel =new GamePanel(this);
        resultPanel = new ResultPanel(this);
        
        mainContainer.add(startPanel, "StartScreen");
        mainContainer.add(gamePanel, "GameScreen");
        mainContainer.add(resultPanel, "ResultPanel");
        add(mainContainer);
        pack(); 
        setLocationRelativeTo(null); 
    }
    
    public ResultPanel getResultPanel() { return resultPanel; }
    public GamePanel getGamePanel() { return gamePanel; }
    
    public void showScreen(String screenName) {
        cardLayout.show(mainContainer, screenName);
        if (screenName.equals("GameScreen")) {
            gamePanel.startGame();
        }
    }
    public void showPanel(String screenName) {
        showScreen(screenName);
    }
    
    //run here
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
