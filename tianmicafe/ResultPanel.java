/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tianmicafe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

public class ResultPanel extends JPanel{
    private MainFrame mainFrame;
    private Image backgroundImage;
    
    private JLabel titleLabel;
    private JLabel summaryHeaderLabel;
    private JPanel listContainerPanel;
    private JScrollPane summaryScrollPane;
    private JLabel totalLabel;
    private JLabel characterLabel;
    private JButton homeBtn;
    private JButton tryAgainBtn;

    public ResultPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setPreferredSize(new Dimension(1024, 576));
        setLayout(null);

        try {
            backgroundImage = new ImageIcon(getClass().getResource("/assets/bg_03.png")).getImage();
        } catch (Exception e) {}

        titleLabel = new JLabel("", SwingConstants.CENTER);
        titleLabel.setBounds(150, 35, 724, 80);
        titleLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 52));
        add(titleLabel);

        summaryHeaderLabel = new JLabel("Summary", SwingConstants.CENTER);
        summaryHeaderLabel.setBounds(250, 145, 520, 35);
        summaryHeaderLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        summaryHeaderLabel.setForeground(new Color(90, 50, 20));
        add(summaryHeaderLabel);

        listContainerPanel = new JPanel();
        listContainerPanel.setLayout(new BoxLayout(listContainerPanel, BoxLayout.Y_AXIS));
        listContainerPanel.setOpaque(false);

        summaryScrollPane = new JScrollPane(listContainerPanel);
        
        summaryScrollPane.setBounds(290, 185, 440, 160);
        summaryScrollPane.setOpaque(false);
        summaryScrollPane.getViewport().setOpaque(false);
        summaryScrollPane.setBorder(null);
        summaryScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        summaryScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(summaryScrollPane);

        totalLabel = new JLabel("Total: 0 $", SwingConstants.RIGHT);
        totalLabel.setBounds(290, 350, 440, 30);
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        totalLabel.setForeground(new Color(62, 39, 35));
        add(totalLabel);

        characterLabel = new JLabel();
        characterLabel.setBounds(740, 210, 250, 320);
        add(characterLabel);

        homeBtn = createStyledButton("HOME", 437, 435, 150, 55);
        homeBtn.addActionListener(e -> mainFrame.showScreen("StartScreen"));
        add(homeBtn);

        tryAgainBtn = createStyledButton("TRY AGAIN", 347, 435, 150, 55);
        tryAgainBtn.addActionListener(e -> {
            mainFrame.showScreen("GameScreen");
        });
        add(tryAgainBtn);
    }

    public void setResult(boolean isWin, Map<String, Integer> itemSales, int totalScore) {
        //SoundManager.stopBGM();
        listContainerPanel.removeAll();
        if (itemSales != null && !itemSales.isEmpty()) {
            for (Map.Entry<String, Integer> entry : itemSales.entrySet()) {
                JPanel rowPanel = new JPanel(new BorderLayout());
                rowPanel.setOpaque(false);
                rowPanel.setMaximumSize(new Dimension(440, 28));
                rowPanel.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));

                JLabel nameLabel = new JLabel("• " + entry.getKey());
                nameLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
                nameLabel.setForeground(new Color(62, 39, 35));

                JLabel priceLabel = new JLabel(entry.getValue() + " $");
                priceLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
                priceLabel.setForeground(new Color(109, 76, 65));
                priceLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20)); 

                rowPanel.add(nameLabel, BorderLayout.WEST);
                rowPanel.add(priceLabel, BorderLayout.EAST);

                listContainerPanel.add(rowPanel);
            }
        } else {
            JLabel noItemLabel = new JLabel("No items served", SwingConstants.CENTER);
            noItemLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            noItemLabel.setForeground(new Color(141, 110, 99));
            listContainerPanel.add(noItemLabel);
        }
        totalLabel.setText("Total: " + totalScore + " $");

        if (isWin) {
            SoundManager.playSound("success");
            titleLabel.setText("WINNER !!");
            titleLabel.setForeground(new Color(90, 50, 20));
            setCharIcon("/assets/happy.png");

            tryAgainBtn.setVisible(false);
            homeBtn.setBounds(437, 435, 150, 55);
        } else {
            SoundManager.playSound("fail");
            titleLabel.setText("GAME OVER !!");
            titleLabel.setForeground(new Color(90, 50, 20));
            setCharIcon("/assets/cry.png");

            tryAgainBtn.setVisible(true);
            tryAgainBtn.setBounds(347, 435, 150, 55);
            homeBtn.setBounds(517, 435, 150, 55);
        }

        listContainerPanel.revalidate();
        listContainerPanel.repaint();
        repaint();
    }

    public void setResult(boolean isWin, int totalScore) {
        
        setResult(isWin, null, totalScore);
    }

    private void setCharIcon(String path) {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(path));
            Image img = icon.getImage().getScaledInstance(250, 320, Image.SCALE_SMOOTH);
            characterLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            characterLabel.setIcon(null);
        }
    }

    private JButton createStyledButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/assets/buttonwood01.png"));
            Image scaledImg = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImg));
        } catch (Exception e) {}

        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.CENTER);
        button.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        button.setForeground(new Color(90, 50, 20));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        int cardX = 250;
        int cardY = 135;
        int cardWidth = 520;
        int cardHeight = 275;

        g2d.setColor(new Color(255, 253, 245, 120));
        g2d.fillRoundRect(cardX, cardY, cardWidth, cardHeight, 24, 24);

        g2d.setColor(new Color(180, 150, 120, 180));
        g2d.drawRoundRect(cardX, cardY, cardWidth, cardHeight, 24, 24);

        g2d.setColor(new Color(161, 136, 127, 180));
        g2d.drawLine(280, 345, 740, 345);

        g2d.dispose();
    }
}
