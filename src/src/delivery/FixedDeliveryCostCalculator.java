package src.delivery;

public class FixedDeliveryCostCalculator implements DeliveryCostCalculator {

    private final double FIXED_COST = 2.99;

    private double costPerDelivery;
    private double costPerProduct;

    public FixedDeliveryCostCalculator(double costPerDelivery, double costPerProduct) {
        this.costPerDelivery = costPerDelivery;
        this.costPerProduct = costPerProduct;
    }

    @Override
    public double calculateFor(int numberOfDeliveries, int numberOfProducts) {
        return numberOfDeliveries * costPerDelivery + numberOfProducts * costPerProduct + FIXED_COST;
    }

}