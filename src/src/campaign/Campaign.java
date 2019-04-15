package src.campaign;

public interface Campaign {

    /**
     * checks if the campaign can be applied for given number of products
     * @param productQuantity number of products
     * @return if applicable or not
     */
    boolean isApplicable(int productQuantity);

    /**
     * calculates discount for given price
     * @param totalPrice total price
     * @return calculated discount
     */
    double getDiscount(double totalPrice);

}
