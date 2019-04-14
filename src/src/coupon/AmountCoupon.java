package src.coupon;

public class AmountCoupon implements Coupon {

    private double minPurchaseAmount;
    private double amount;

    public AmountCoupon(double minPurchaseAmount, double amount) {
        this.minPurchaseAmount = minPurchaseAmount;
        this.amount = amount;
    }

    @Override
    public boolean isApplicable(double amount) {
        return amount >= minPurchaseAmount;
    }

    @Override
    public double getDiscount(double totalAmount) {
        return amount;
    }

    @Override
    public String toString() {
        return "AmountCoupon: minPurchaseAmount: " + String.format("%.2f", minPurchaseAmount) + "TL, amount: " + String.format("%.2f", amount) + "TL";
    }

}