package src.coupon;

public interface Coupon {

    boolean isApplicable(double cost);

    double getDiscount(double totalAmount);

}
