/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tianmicafe;
import java.util.List;

public interface Servable {
    boolean isOrderCorrect(List<Order> preparedItems, List<Order> requestedItems);
    int processServe(Customer customer, List<Order> preparedItems);
}
