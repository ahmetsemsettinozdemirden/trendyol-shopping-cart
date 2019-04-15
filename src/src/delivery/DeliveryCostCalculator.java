package src.delivery;

public interface DeliveryCostCalculator {

    /**
     * calculates delivery cost for given number of deliveries and products
     * @param numberOfDeliveries number of deliveries (categories)
     * @param numberOfProducts number of products
     * @return delivery cost
     */
    double calculateFor(int numberOfDeliveries, int numberOfProducts);

}
