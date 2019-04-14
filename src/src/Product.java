package src;

public class Product {

    private String title;
    private double price;
    private Category category;

    public Product(String title, double price, Category category) {
        this.title = title;
        this.price = price;
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return title + ": unitPrice: " + String.format("%.2f", price) + "TL";
    }
}
