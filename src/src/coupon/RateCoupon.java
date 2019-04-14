package src.coupon;

public class RateCoupon implements Coupon {

    private double minPurchaseAmount;
    private double rate;

    public RateCoupon(double minPurchaseAmount, double rate) {
        this.minPurchaseAmount = minPurchaseAmount;
        this.rate = rate;
    }

    @Override
    public boolean isApplicable(double amount) {
        return amount >= minPurchaseAmount;
    }

    @Override
    public double getDiscount(double totalAmount) {
        return totalAmount * (rate * 0.01);
    }
}