package grocery;

import java.util.ArrayList;
import java.util.List;

public class Bill {
    private List<Item> items;
    private double totalAmount;

    public Bill() {
        items = new ArrayList<>();
        totalAmount = 0.0;
    }

    public void addItem(Item item) {
        items.add(item);
        totalAmount += item.getTotal();
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public List<Item> getItems() {
        return items;
    }
}
