package src.coupon;

public interface Coupon {

    /**
     * checks if the coupon can be applied for given cost
     * @param cost cost
     * @return if applicable or not
     */
    boolean isApplicable(double cost);

    /**
     * calculates discount for given total amount
     * @param totalAmount total price
     * @return calculated discount
     */
    double getDiscount(double totalAmount);

}
