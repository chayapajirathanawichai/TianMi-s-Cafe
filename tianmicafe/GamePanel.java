/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tianmicafe;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;

public class GamePanel extends JPanel{
    private Image backgroundImage;
    private MainFrame mainFrame;

    private int timeLeft = 45; 
    private int heartsLeft = 3;  
    private Timer gameTimer;

    private JLabel moneyLabel;
    private JLabel scoreLabel;
    private JLabel timeLabel;
    private JButton actionButton;
    
    private Customer currentCustomer;
    private JLabel customerLabel;
    private JLabel speechBubbleLabel;
    private JLabel[] menuIconLabels = new JLabel[3];
    
    private PlatePanel platePanel;
    private OrderService orderService = new OrderService();
    private JPanel kitchenMenuPanel;
    private boolean isPreparing = false;
    
    private PatienceBar patienceBar;
    
    private ScoreManager scoreManager=new ScoreManager(0);  
    private java.util.Map<String, Integer> itemSalesMap = new java.util.HashMap<>();
    
    //constructor
    public GamePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(null);
        try {
            backgroundImage = new ImageIcon(getClass().getResource("/assets/bg_02.png")).getImage();
        } catch (Exception e) {
            System.out.println("Error loading game background: " + e.getMessage());
        }       
        initGameUI();
        initKitchenMenuHUD();
        if (kitchenMenuPanel != null) {
            setComponentZOrder(kitchenMenuPanel, 0);
        }
        setPreferredSize(new Dimension(1024, 576));
        setLayout(null); // Absolute Layout
        JPanel moneyPanel = createStatPanel("0 $", 40, 20, 210, 75, "/assets/money.png", true);
        add(moneyPanel);
        
        JPanel timePanel = createStatPanel("45 s", 770, 20, 210, 75, "/assets/clock01.png", false);
        add(timePanel);
        initPatienceBar();      
        actionButton = createActionButton("Prepare", 392, 465, 240, 80);
        actionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleActionButtonClick();
            }
        });
        add(actionButton);
        initCustomerUI();
        initGameTimer();
        // startGame() is called by MainFrame.showScreen when START is pressed
    }
    
    public void startGame() {
        timeLeft = 45;
        heartsLeft = 3;      
        if (scoreManager != null) {
            scoreManager.resetScore();
        }
        updateGameUI();
        if (gameTimer != null) {
            gameTimer.start();
        }
        spawnNewCustomer();
        itemSalesMap.clear();
    }

    private void initGameTimer() {
        gameTimer = new javax.swing.Timer(1000, e -> {
            if (timeLeft > 0) {
                timeLeft--;
                if (timeLabel != null) {
                    timeLabel.setText(timeLeft + " s");
                }
                updateCustomerPatience();
            } else {
                gameTimer.stop();
                checkWinLoseCondition();
            }
        });
    }
    public void showFloatingText(String text, Color color, int startX, int startY) {
        JLabel floatLabel = new JLabel(text);
        floatLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        floatLabel.setForeground(color);
        floatLabel.setBounds(startX, startY, 200, 40);
        add(floatLabel);
        setComponentZOrder(floatLabel, 0); 

        Timer floatTimer = new Timer(30, null);
        floatTimer.addActionListener(new ActionListener() {
            int y = startY;
            int opacity = 255;
            int frameCount = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                y -= 1; 
                frameCount++;
                floatLabel.setLocation(startX, y);
                if (frameCount >= 30) {
                    floatTimer.stop();
                    remove(floatLabel);
                    repaint();
                }
            }
        });
        floatTimer.start();
    }
    private void handleActionButtonClick() {
        if (currentCustomer == null) return;
        if (actionButton.getText().equals("Prepare")) {          
            System.out.println("Preparing Order...");
            actionButton.setText("Serve");
            if (kitchenMenuPanel != null) {
                kitchenMenuPanel.setVisible(true); 
            }
        } else {
            handleServeAction();
        }
    }
    
    public void handleServeAction() {
        if (currentCustomer == null) return;
        int earnedMoney = orderService.processServe(currentCustomer, platePanel.getPlateItems());
        if (earnedMoney != -1) {
            SoundManager.playSound("bonus");
            int patience=currentCustomer.getCurrentPatience();
            int finalEarnedMoney =scoreManager.processServeSuccess(patience, earnedMoney);
            for (Order order : currentCustomer.getOrderedMenuList()) {
                itemSalesMap.put(order.getName(), itemSalesMap.getOrDefault(order.getName(), 0) + order.getPrice());
            }
            showFloatingText("+" + finalEarnedMoney + "$ ", new Color(46, 204, 113), 200, 30);

            actionButton.setText("Prepare");
            if (kitchenMenuPanel != null) {
                kitchenMenuPanel.setVisible(false);
            }

            spawnNewCustomer();
            updateGameUI();
        } else {
            SoundManager.playSound("ohno");
            scoreManager.processWrongServePenalty(5);
            updateGameUI();
            showFloatingText("-5$ Wrong!", new Color(231, 76, 60), 200, 30);
            JOptionPane.showMessageDialog(this, "Not Match!", "Serve Error", JOptionPane.WARNING_MESSAGE);
            platePanel.clearPlate();
        }
    }
    
    private void triggerGameOver(String reason){
        if(gameTimer!=null) gameTimer.stop();
        JOptionPane.showMessageDialog(this, "Game Over! "+reason,"Game Over",JOptionPane.ERROR_MESSAGE);
        checkWinLoseCondition();
    }
    
    private void checkWinLoseCondition() {
        int currentMoney = (scoreManager != null) ? scoreManager.getScoreMoney() : 0;
    
        boolean isWin = currentMoney >= 300;

        ResultPanel resultPanel = mainFrame.getResultPanel();
        if (resultPanel != null) {
            resultPanel.setResult(isWin, itemSalesMap, currentMoney);
            mainFrame.showScreen("ResultPanel");
        }
    }

    private void updateGameUI() {
        if(scoreManager!=null){
            int currentMoney=scoreManager.getScoreMoney();
            if(scoreLabel!=null){
                scoreLabel.setText(currentMoney+"$");
            }
            if(moneyLabel!=null){
                moneyLabel.setText(currentMoney+"$");
            }
        }
        if(timeLabel!=null){
            timeLabel.setText(timeLeft+"s");
        }
    }
    public void initGameUI() {
        if (platePanel == null) {
            platePanel = new PlatePanel();
        }
        platePanel.setBounds(120, 390, 160, 90);
        add(platePanel);   
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }  
    private JPanel createStatPanel(String initialText, int x, int y, int width, int height, String iconPath, boolean isMoney) {
        
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    ImageIcon boardIcon = new ImageIcon(getClass().getResource("/assets/buttonwood01.png"));
                    g.drawImage(boardIcon.getImage(), 0, 0, getWidth(), getHeight(), this);
                } catch (Exception e) {
                    
                }
            }
        };
        panel.setBounds(x, y, width, height);
        panel.setOpaque(false);
        panel.setLayout(null); 

        
        JLabel iconLabel = new JLabel();
        if (isMoney) {
    
            iconLabel.setBounds(8, (height - 62) / 2, 52, 62);
            try {
                ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
                Image img = icon.getImage().getScaledInstance(52, 62, Image.SCALE_SMOOTH);
                iconLabel.setIcon(new ImageIcon(img));
            } catch (Exception e) {}
        }
        //clock
        else {

            iconLabel.setBounds(10, (height - 55) / 2, 55, 55);
            try {
                ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
                Image img = icon.getImage().getScaledInstance(55, 55, Image.SCALE_SMOOTH);
                iconLabel.setIcon(new ImageIcon(img));
            } catch (Exception e) {}
        }

        panel.add(iconLabel);

        
        JLabel textLabel = new JLabel(initialText, SwingConstants.CENTER);
        textLabel.setBounds(75, 0, width - 85, height); 
        textLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        textLabel.setForeground(new Color(90, 50, 20)); 
        panel.add(textLabel);

        if (isMoney) {
            this.moneyLabel = textLabel;
        } else {
            this.timeLabel = textLabel;
        }

        return panel;
    }
    
    private JButton createActionButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/assets/buttonwood01.png"));
            Image scaledImg = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImg));
        } catch (Exception e) {
            System.out.println("Error loading action button icon: " + e.getMessage());
        }

        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.CENTER);
        button.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        button.setForeground(new Color(90, 50, 20));

        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setOpaque(false);

        return button;
    }
    public void onSelectMenu(Order selectedOrder) {
        SoundManager.playSound("pop");
        if (platePanel == null) {
            platePanel = new PlatePanel();
            platePanel.setBounds(80, 400, 180, 90);
            add(platePanel);
            revalidate();
            repaint();
        }
        platePanel.addItem(selectedOrder);
    }
    private void initKitchenMenuHUD() {
        kitchenMenuPanel = new JPanel();
        kitchenMenuPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 3, 2));
        kitchenMenuPanel.setBounds(210, 410, 560, 50);
        kitchenMenuPanel.setOpaque(false);

        for (Order order : Customer.getMenuList()) {
            JButton btn = new JButton();
            btn.setPreferredSize(new java.awt.Dimension(48, 48));

            try {
                ImageIcon icon = new ImageIcon(getClass().getResource(order.getImagePath()));
                Image img = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                btn.setIcon(new ImageIcon(img));
            } catch (Exception e) {
                btn.setText(order.getName());
            }

            btn.addActionListener(e -> onSelectMenu(order));
            kitchenMenuPanel.add(btn);
        }
        add(kitchenMenuPanel);
        kitchenMenuPanel.setVisible(false);
    }
    
    //customer
    private void initCustomerUI() {
    
        customerLabel = new JLabel();
        customerLabel.setBounds(400, 180, 220, 280); 
        add(customerLabel);


        speechBubbleLabel = new JLabel();
        speechBubbleLabel.setBounds(535, 110, 130, 130);
        try {
            ImageIcon bubble = new ImageIcon(getClass().getResource("/assets/speak1.png"));
            Image img = bubble.getImage().getScaledInstance(130, 130, Image.SCALE_SMOOTH);
            speechBubbleLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {}

        menuIconLabels[0] = new JLabel();
        menuIconLabels[0].setBounds(562, 125, 32, 32);

        menuIconLabels[1] = new JLabel();
        menuIconLabels[1].setBounds(598, 125, 32, 32);

        menuIconLabels[2] = new JLabel();
        menuIconLabels[2].setBounds(580, 158, 32, 32);

        for (int i = 0; i < 3; i++) {
            add(menuIconLabels[i]);
        }
        add(speechBubbleLabel); 
        speechBubbleLabel.setVisible(false);
        for (JLabel label : menuIconLabels) label.setVisible(false);

    }
    
    public void spawnNewCustomer() {
        currentCustomer = new Customer();
        SoundManager.playSound("bell");
        if(platePanel !=null){
            platePanel.clearPlate();
        }
        if (currentCustomer.getCustomerImage() != null) {
            Image scaledCustomer = currentCustomer.getCustomerImage()
                                                   .getScaledInstance(220, 280, Image.SCALE_SMOOTH);
            customerLabel.setIcon(new ImageIcon(scaledCustomer));
        }
        for (JLabel label : menuIconLabels) {
            label.setIcon(null);
            label.setVisible(false);
        }
        List<Order> orders = currentCustomer.getOrderedMenuList();
        int count = orders.size();

        if (count == 1) {
            
            menuIconLabels[0].setBounds(580, 138, 32, 32);
        } 
        else if (count == 2) {
            
            menuIconLabels[0].setBounds(562, 138, 32, 32);
            menuIconLabels[1].setBounds(598, 138, 32, 32);
        } 
        else if (count == 3) {
            
            menuIconLabels[0].setBounds(562, 125, 32, 32);
            menuIconLabels[1].setBounds(598, 125, 32, 32);
            menuIconLabels[2].setBounds(580, 158, 32, 32);
        }

        for (int i = 0; i < count; i++) {
            try {
                ImageIcon icon = new ImageIcon(getClass().getResource(orders.get(i).getImagePath()));
                Image img = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
                menuIconLabels[i].setIcon(new ImageIcon(img));
                menuIconLabels[i].setVisible(true);
            } catch (Exception e) {
                System.out.println("Error loading menu icon: " + e.getMessage());
            }
        }

        speechBubbleLabel.setVisible(true);
        
    }
    private void initPatienceBar() {
        patienceBar = new PatienceBar();
        patienceBar.setBounds(370, 198, 110, 16); 
        add(patienceBar);
    }
               
    private void updateCustomerPatience() {
        if (currentCustomer != null) {
            currentCustomer.decreasePatience(4); 
            int currentVal = currentCustomer.getCurrentPatience();

            patienceBar.updatePatience(currentVal);             

            if (currentVal <= 30 && !currentCustomer.isAngry()) {
                SoundManager.playSound("scream");
                currentCustomer.setAngry(true); 
                if (currentCustomer.getAngryCustomerImage() != null) {
                    Image scaledAngry = currentCustomer.getAngryCustomerImage()
                                                       .getScaledInstance(220, 280, Image.SCALE_SMOOTH);
                    customerLabel.setIcon(new ImageIcon(scaledAngry));
                }
            }

            if (currentVal <= 0) {
                scoreManager.processCustomerWalkoutPenalty(10);
                heartsLeft--;
                showFloatingText("-10$ Walked Out!", new Color(231, 76, 60), 200, 30);
                JOptionPane.showMessageDialog(this, "Customer walked out! 😡");
                if(platePanel!=null)
                    platePanel.clearPlate();
                if(heartsLeft<=0) triggerGameOver("Customers walked out 3 times!");
                else{
                    spawnNewCustomer();
                    updateGameUI();
                }
            }
        }
    }
    private void handleCustomerLeave() {
        spawnNewCustomer();
    }
}