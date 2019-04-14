import org.junit.Before;
import org.junit.Test;
import src.Category;
import src.Product;
import src.ShoppingCart;
import src.campaign.AmountCampaign;
import src.campaign.Campaign;
import src.campaign.RateCampaign;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
    public void givenRateCampaignWhenAddCampaignCalledThenItReturnsTrue() {
        Campaign rateCampaign = new RateCampaign(20.0, 5);
        Category category = new Category("category");

        assertTrue(category.addCampaign(rateCampaign));
    }

    @Test
    public void givenRateCampaignWithCategoryAlreadyAddedWhenAddCampaignCalledThenItReturnsFalse() {
        Campaign rateCampaign = new RateCampaign(20.0, 5);
        Category category = new Category("category");
        category.addCampaign(rateCampaign);

        assertFalse(category.addCampaign(rateCampaign));
    }

    @Test
    public void givenAmountCampaignWhenAddCampaignCalledThenItReturnsTrue() {
        Campaign amountCampaign = new AmountCampaign(10.0, 5);
        Category category = new Category("category");

        assertTrue(category.addCampaign(amountCampaign));
    }

    @Test
    public void givenAmountCampaignWithCategoryAlreadyAddedWhenAddCampaignCalledThenItReturnsFalse() {
        Campaign amountCampaign = new AmountCampaign(10.0, 5);
        Category category = new Category("category");
        category.addCampaign(amountCampaign);

        assertFalse(category.addCampaign(amountCampaign));
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
        Product product = createProduct("product", 1.0, "category");
        shoppingCart.addProduct(2, product);

        double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();

        assertEquals(2.0, totalAmount, 0.001);
    }

    @Test
    public void givenShoppingCartWith1ProductWhenAddProductCalledWithSameProductAndQuantity1ThenAddProductReturns2() {
        Product product = createProduct("product", 1.0, "category");
        shoppingCart.addProduct(1, product);

        int productQuantity = shoppingCart.addProduct(1, product);

        assertEquals(2, productQuantity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenShoppingCartWith1ProductWhenAddProductCalledWithInvalidQuantityThenItShouldThrowInvalidArgumentException() {
        Product product = createProduct("product", 1.0, "category");
        shoppingCart.addProduct(1, product);

        shoppingCart.addProduct(-1, product);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenEmptyShoppingCartWhenAddProductCalledWithNullProductThenItShouldThrowInvalidArgumentException() {
        shoppingCart.addProduct(1, null);
    }

    private Product createProduct(String productTitle, double productPrice, String categoryTitle) {
        return new Product(productTitle, productPrice, new Category(categoryTitle));
    }

}
