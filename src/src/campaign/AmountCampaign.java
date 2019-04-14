package src.campaign;

public class AmountCampaign implements Campaign {

    private double amount;
    private int minProductQuantity;

    public AmountCampaign(double amount, int minProductQuantity) {
        this.amount = amount;
        this.minProductQuantity = minProductQuantity;
    }

    @Override
    public boolean isApplicable(int productQuantity) {
        return productQuantity >= minProductQuantity;
    }

    @Override
    public double getDiscount(double totalAmount) {
        return amount;
    }

}
