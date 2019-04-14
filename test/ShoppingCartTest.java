import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ShoppingCartTest {

    @Test
    public void givenShoppingCartWithProductsAndDiscountsWhenGetTotalAmountAfterDiscountsCalledThenItShouldReturnTotalAmount() {

        Campaign rateCampaign = new RateCampaign(20.0, 3);
        Campaign amountCampaign = new AmountCampaign(5.0, 5);

        Category category1 = new Category("category1");
        Category subcategory1 = new Category("subcategory1", category1);
        Category subcategory2 = new Category("subcategory2", category1);
        Category category2 = new Category("category2");

        category1.addCampaign(rateCampaign);
        subcategory1.addCampaign(amountCampaign);

        Product product1 = new Product("product1", 5.0, subcategory1);
        Product product2 = new Product("product2", 3.0, subcategory2);
        Product product3 = new Product("product3", 20.0, category2);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addProduct(product1, 2);
        shoppingCart.addProduct(product2, 3);
        shoppingCart.addProduct(product3, 5);

        double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();

        assertEquals(106.90, totalAmount, 0.001);
    }

    interface Campaign {

    }

    class RateCampaign implements Campaign {
        public RateCampaign(double rate, int quantity) {

        }
    }

    class AmountCampaign implements Campaign {
        public AmountCampaign(double amount, int quantity) {

        }
    }

    class Category {

        public Category(String title) {
            this(title, null);
        }

        public Category(String title, Category parentCategory) {

        }

        public boolean addCampaign(Campaign campaign) {
            return true;
        }

    }

    class Product {

        public Product(String title, double price, Category category) {

        }

    }

    class ShoppingCart {

        public int addProduct(Product product, int quantity) {
            return 0;
        }

        public double getTotalAmountAfterDiscounts() {
            return 0;
        }

    }

}
