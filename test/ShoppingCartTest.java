import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ShoppingCartTest {

    private ShoppingCart shoppingCart;

    @Before
    public void setUp() {
        shoppingCart = new ShoppingCart();
    }

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

        shoppingCart.addProduct(2, product1);
        shoppingCart.addProduct(3, product2);
        shoppingCart.addProduct(5, product3);

        double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();

        assertEquals(106.90, totalAmount, 0.001);
    }

    @Test
    public void givenProductsForDifferentCategoriesWithoutAnyDiscountsWhenGetTotalAmountAfterDiscountsCalledOnShoppingCartThenItShouldReturnTotalAmount() {

        Category category1 = new Category("category1");
        Category subcategory1 = new Category("subcategory1", category1);
        Category subcategory2 = new Category("subcategory2", category1);
        Category category2 = new Category("category2");

        Product product1 = new Product("product1", 5.0, subcategory1);
        Product product2 = new Product("product2", 3.0, subcategory2);
        Product product3 = new Product("product3", 20.0, category2);

        shoppingCart.addProduct(2, product1);
        shoppingCart.addProduct(3, product2);
        shoppingCart.addProduct(5, product3);

        double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();

        assertEquals(119.0, totalAmount, 0.001);
    }

    @Test
    public void givenOneProductWithSingleCategoryWhenGetTotalAmountAfterDiscountCalledOnShoppingCartThenItShouldReturnProductPrice() {
        Product product = new Product("product", 1.0, new Category("category"));
        shoppingCart.addProduct(1, product);

        double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();

        assertEquals(1.0, totalAmount, 0.001);
    }

    interface Campaign { }

    class RateCampaign implements Campaign {
        public RateCampaign(double rate, int quantity) { }
    }

    class AmountCampaign implements Campaign {
        public AmountCampaign(double amount, int quantity) { }
    }

    class Category {

        private String title;
        private Category parentCategory;

        public Category(String title) {
            this(title, null);
        }

        public Category(String title, Category parentCategory) {
            this.title = title;
            this.parentCategory = parentCategory;
        }

        public boolean addCampaign(Campaign campaign) {
            return true;
        }

    }

    class Product {

        private String title;
        private double price;
        private Category category;

        public Product(String title, double price, Category category) {
            this.title = title;
            this.price = price;
            this.category = category;
        }

        public double getPrice() {
            return price;
        }

    }

    class ShoppingCart {

        private Map<Product, Integer> productQuantities;

        public ShoppingCart() {
            this.productQuantities = new HashMap<>();
        }

        public int addProduct(int quantity, Product product) {
            if (!productQuantities.containsKey(product)) {
                productQuantities.put(product, quantity);
            } else {
                productQuantities.replace(product, productQuantities.get(product) + quantity);
            }
            return productQuantities.get(product);
        }

        private double getTotalAmount() {
            double totalAmount = 0.0;
            for (Map.Entry<Product, Integer> entry: productQuantities.entrySet()){
                Product product = entry.getKey();
                int quantity = entry.getValue();
                totalAmount += product.getPrice() * quantity;
            }
            return totalAmount;
        }

        public double getTotalAmountAfterDiscounts() {
            return getTotalAmount();
        }

    }

}
