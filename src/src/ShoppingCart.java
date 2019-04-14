package src;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {

    private Map<Product, Integer> productQuantities;

    public ShoppingCart() {
        this.productQuantities = new HashMap<>();
    }

    public int addProduct(int quantity, Product product) {
        if (quantity <= 0)
            throw new IllegalArgumentException("invalid quantity: " + quantity);
        if (product == null)
            throw new IllegalArgumentException("product is null");

        if (!productQuantities.containsKey(product)) {
            productQuantities.put(product, quantity);
        } else {
            productQuantities.replace(product, productQuantities.get(product) + quantity);
        }

        return productQuantities.get(product);
    }

    private double getTotalAmount() {
        double totalAmount = 0.0;
        for (Map.Entry<Product, Integer> entry: productQuantities.entrySet()){
            Product product = entry.getKey();
            int quantity = entry.getValue();
            totalAmount += product.getPrice() * quantity;
        }
        return totalAmount;
    }

    public double getTotalAmountAfterDiscounts() {
        return getTotalAmount();
    }

}
