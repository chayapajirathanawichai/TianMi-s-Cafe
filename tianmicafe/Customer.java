/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tianmicafe;
import java.awt.*;
import java.util.Random;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Customer {
    private Image customerImage;
    private Image angryCustomerImage;
    public List<Order> orderedMenuList = new ArrayList<>();
    private int patienceTime = 20; 
    private boolean isFinished = false;
    private boolean isAngry = false;
    private int maxPatience = 100;
    private int currentPatience = 100;

    private static final Order[] MENU_LIST = {
        //drink
        new Order("Peach Sparkling Soda", "/assets/peach_sparkling_soda.png", 30),
        new Order("Honey Strawberry Milk", "/assets/honey_strawberry_milk.png", 35),
        new Order("TianMi Cloud Latte", "/assets/tianmi_cloud_latte.png", 45),
        new Order("Matcha Cream Float", "/assets/matcha_cream_float.png", 40),
        new Order("Warm Hot Chocolate", "/assets/warmhot_chocolate.png", 35),
        //bakery
        new Order("Chocolate Layer Cake", "/assets/chocolate_layer.png", 55),
        new Order("Custard Cream Puff", "/assets/custard_cream_puff.png", 25),
        new Order("Honey Bear Pancake", "/assets/honeybear_pancake.png", 45),
        new Order("Cute Cat Paw Donut", "/assets/cutecat_donut.png", 20),
        new Order("Strawberry Short Cake", "/assets/strawberry_shortcake.png", 50)
    };

    public Customer() {
        Random rand = new Random();
        
        int customerIndex = rand.nextInt(2) + 1; 
        String normalPath;
        String angryPath;

        if (customerIndex == 1) {
            normalPath = "/assets/cust_boy1.png";
            angryPath = "/assets/cust_boy2.png";
        } else {
            normalPath = "/assets/cust_girl1.png";
            angryPath = "/assets/cust_girl2.png";
        }
        try {
            customerImage = new ImageIcon(getClass().getResource(normalPath)).getImage();
            angryCustomerImage = new ImageIcon(getClass().getResource(angryPath)).getImage();
        } catch (Exception e) {
            System.out.println("Error loading customer images: " + e.getMessage());
        }

        int itemCount = rand.nextInt(3) + 1; 
        for (int i = 0; i < itemCount; i++) {
            Order randomOrder = MENU_LIST[rand.nextInt(MENU_LIST.length)];
            orderedMenuList.add(randomOrder.clone());
        }
    }
    
    public void decreasePatience(int amount) {
        this.currentPatience -= amount;
        if (this.currentPatience < 0) {
            this.currentPatience = 0;
        }
    }

    public int getCurrentPatience() {
        return currentPatience;
    }

    public void resetPatience() {
        this.currentPatience = maxPatience;
    }

    public double getPatiencePercentage() {
        if (maxPatience == 0) return 0.0;
        return (double) currentPatience / (double) maxPatience;
    }

    public Image getCustomerImage() { return customerImage; }
    public Image getAngryCustomerImage() { return angryCustomerImage; }
    public List<Order> getOrderedMenuList() { return orderedMenuList; }
    public boolean isAngry() { return isAngry; }
    public void setAngry(boolean angry) { this.isAngry = angry; }
    public boolean isFinished() { return isFinished; }
    public void setFinished(boolean finished) { this.isFinished = finished; }

    public static Order[] getMenuList() {
        return MENU_LIST;
    }
}
