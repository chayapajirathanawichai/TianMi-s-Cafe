/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tianmicafe;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderService implements Servable{ 
    @Override
    public boolean isOrderCorrect(List<Order> preparedItems, List<Order> requestedItems) {
        if (preparedItems == null || requestedItems == null) return false;
        if (preparedItems.size() != requestedItems.size()) return false;

        List<String> preparedNames = new ArrayList<>();
        for (Order o : preparedItems) preparedNames.add(o.getName());

        List<String> requestedNames = new ArrayList<>();
        for (Order o : requestedItems) requestedNames.add(o.getName());

        Collections.sort(preparedNames);
        Collections.sort(requestedNames);

        return preparedNames.equals(requestedNames);
    }

    @Override
    public int processServe(Customer customer, List<Order> preparedItems) {
        if (customer == null || preparedItems == null) return -1;

        if (isOrderCorrect(preparedItems, customer.getOrderedMenuList())) {
            int totalPrice = 0;
            for (Order item : preparedItems) {
                totalPrice += item.getPrice();
            }

            double patiencePercent = customer.getPatiencePercentage();
            int tip = 0;
            if (patiencePercent >= 0.75) {
                tip = (int) (totalPrice * 0.30);
            } else if (patiencePercent >= 0.45) {
                tip = (int) (totalPrice * 0.15);
            }

            return totalPrice + tip;
        }

        return -1;
    }
}
