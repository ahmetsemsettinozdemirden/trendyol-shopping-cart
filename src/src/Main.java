package src;

import src.campaign.AmountCampaign;
import src.campaign.Campaign;
import src.campaign.RateCampaign;
import src.coupon.AmountCoupon;
import src.coupon.Coupon;
import src.coupon.RateCoupon;
import src.delivery.FixedDeliveryCostCalculator;

public class Main {

    public static void main(String[] args) {

        ShoppingCart shoppingCart = new ShoppingCart(new FixedDeliveryCostCalculator(2.0, 3.0));

        Category category1 = new Category("category1");
        Category subcategory1 = new Category("subcategory1", category1);
        Category subcategory2 = new Category("subcategory2", category1);
        Category category2 = new Category("category2");

        Campaign rateCampaign = new RateCampaign(25.0, 3);
        Campaign amountCampaign = new AmountCampaign(5.0, 2);
        category1.addCampaign(rateCampaign);
        subcategory1.addCampaign(amountCampaign);

        Product product1 = new Product("product1", 5.0, subcategory1);
        Product product2 = new Product("product2", 3.0, subcategory2);
        Product product3 = new Product("product3", 20.0, category2);
        shoppingCart.addProduct(3, product1);
        shoppingCart.addProduct(2, product2);
        shoppingCart.addProduct(5, product3);

        Coupon rateCoupon = new RateCoupon(10.0, 10.0);
        Coupon amountCoupon = new AmountCoupon(8.0, 2.5);

        shoppingCart.addCoupon(rateCoupon);
        shoppingCart.addCoupon(amountCoupon);

        System.out.println(shoppingCart);
    }

}
