package src.campaign;

public interface Campaign {

    boolean isApplicable(int productQuantity);

    double getDiscount(double totalPrice);

}
