package src.campaign;

public class RateCampaign implements Campaign {

    private double rate;
    private int minProductQuantity;

    public RateCampaign(double rate, int minProductQuantity) {
        this.rate = rate;
        this.minProductQuantity = minProductQuantity;
    }

    @Override
    public boolean isApplicable(int productQuantity) {
        return productQuantity >= minProductQuantity;
    }

    @Override
    public double getDiscount(double totalAmount) {
        return totalAmount * (rate * 0.01);
    }

}
