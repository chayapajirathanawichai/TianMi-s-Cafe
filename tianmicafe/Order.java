/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tianmicafe;

public class Order implements Cloneable{
    private String name;
    private String imagePath;
    private int price;

    public Order(String name, String imagePath, int price) {
        this.name = name;
        this.imagePath = imagePath;
        this.price = price;
    }
    
    @Override
    public Order clone() {
        try {
            return (Order) super.clone();
        } catch (CloneNotSupportedException e) {
            return new Order(this.name, this.imagePath, this.price);
        }
    }
    
    public String getName() { return name; }
    public String getImagePath() { return imagePath; }
    public int getPrice() { return price; }
}
