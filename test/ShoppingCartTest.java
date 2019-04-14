import org.junit.Before;
import org.junit.Test;
import src.Category;
import src.Product;
import src.ShoppingCart;
import src.campaign.AmountCampaign;
import src.campaign.Campaign;
import src.campaign.RateCampaign;
import src.coupon.AmountCoupon;
import src.coupon.Coupon;
import src.coupon.RateCoupon;
import src.delivery.FixedDeliveryCostCalculator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ShoppingCartTest {

    private ShoppingCart shoppingCart;

    @Before
    public void setUp() {
        shoppingCart = new ShoppingCart(new FixedDeliveryCostCalculator(1.0, 1.0));
    }

    @Test
    public void givenShoppingCartWithProductAndCouponsWhenGetTotalAmountAfterDiscountsCalledThenItShouldReturnTotalAmount() {
        Category category = new Category("category");
        Product product = new Product("product", 50.0, category);
        shoppingCart.addProduct(2, product);

        Coupon rateCoupon = new RateCoupon(100.0, 10.0);
        Coupon amountCoupon = new AmountCoupon(80.0, 25.0);

        shoppingCart.addCoupon(rateCoupon);
        shoppingCart.addCoupon(amountCoupon);

        double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();

        assertEquals(65.0, totalAmount, 0.001);
    }

    // TODO: rate amount ayri ayri

    @Test
    public void givenShoppingCartWithProductsAndDiscountsWhenGetTotalAmountAfterDiscountsCalledThenItShouldReturnTotalAmount() {

        Campaign rateCampaign = new RateCampaign(25.0, 3);
        Campaign amountCampaign = new AmountCampaign(5.0, 2);

        Category category1 = new Category("category1");
        Category subcategory1 = new Category("subcategory1", category1);
        Category subcategory2 = new Category("subcategory2", category1);
        Category category2 = new Category("category2");

        category1.addCampaign(rateCampaign);
        subcategory1.addCampaign(amountCampaign);

        Product product1 = new Product("product1", 5.0, subcategory1);
        Product product2 = new Product("product2", 3.0, subcategory2);
        Product product3 = new Product("product3", 20.0, category2);

        shoppingCart.addProduct(3, product1);
        shoppingCart.addProduct(2, product2);
        shoppingCart.addProduct(5, product3);

        double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();

        assertEquals(110.75, totalAmount, 0.001);
    }

    @Test
    public void givenProductsForSubcategoriesWithRateCampaignAppliedToParentCategoryWhenGetTotalAmountAfterDiscountsCalledOnShoppingCartThenItShouldReturnTotalAmountWithDiscountApplied() {
        Campaign rateCampaign = new RateCampaign(20.0, 5);
        Category category = new Category("category");
        Category subcategory = new Category("subcategory", category);
        category.addCampaign(rateCampaign);
        Product product = new Product("product", 10.0, category);
        Product subproduct = new Product("subproduct", 20.0, subcategory);
        shoppingCart.addProduct(2, product);
        shoppingCart.addProduct(3, subproduct);

        double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();

        assertEquals(64.0, totalAmount, 0.001);
    }

    @Test
    public void givenProductsForSubcategoriesWithRateCampaignAppliedToSubcategoryWhenGetTotalAmountAfterDiscountsCalledOnShoppingCartThenItShouldReturnTotalAmountWithoutDiscountApplied() {
        Campaign rateCampaign = new RateCampaign(20.0, 6);
        Category category = new Category("category");
        Category subcategory = new Category("subcategory", category);
        category.addCampaign(rateCampaign);
        Product product = new Product("product", 10.0, category);
        Product subproduct = new Product("subproduct", 20.0, subcategory);
        shoppingCart.addProduct(2, product);
        shoppingCart.addProduct(3, subproduct);

        double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();

        assertEquals(80.0, totalAmount, 0.001);
    }
    @Test
    public void givenAmountCampaignWhenGetTotalAmountAfterDiscountsCalledOnShoppingCartThenItShouldReturnTotalAmountWithDiscountApplied() {
        Campaign amountCampaign = new AmountCampaign(5.0, 5);
        Category category = new Category("category");
        category.addCampaign(amountCampaign);
        Product product = new Product("product", 10.0, category);
        shoppingCart.addProduct(5, product);

        double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();

        assertEquals(45.0, totalAmount, 0.001);
    }

    @Test
    public void given2DifferentAmountCampaignsWhenGetTotalAmountAfterDiscountsCalledOnShoppingCartThenItShouldReturnTotalAmountWithDiscountApplied() {
        Campaign amountCampaign1 = new AmountCampaign(5.0, 5);
        Campaign amountCampaign2 = new AmountCampaign(15.0, 5);
        Category category = new Category("category");
        category.addCampaign(amountCampaign1);
        category.addCampaign(amountCampaign2);
        Product product = new Product("product", 10.0, category);
        shoppingCart.addProduct(5, product);

        double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();

        assertEquals(30.0, totalAmount, 0.001);
    }

    @Test
    public void givenProductWhichIsLessThenMinimumProductQuantityForCategoryWithAmountCampaignAppliedWhenGetTotalAmountAfterDiscountsCalledOnShoppingCartThenItShouldReturnTotalAmountWithoutDiscountApplied() {
        Campaign amountCampaign = new AmountCampaign(5.0, 6);
        Category category = new Category("category");
        category.addCampaign(amountCampaign);
        Product product = new Product("product", 10.0, category);
        shoppingCart.addProduct(5, product);

        double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();

        assertEquals(50.0, totalAmount, 0.001);
    }

    @Test
    public void givenRateCampaignWhenGetTotalAmountAfterDiscountsCalledOnShoppingCartThenItShouldReturnTotalAmountWithDiscountApplied() {
        Campaign rateCampaign = new RateCampaign(10.0, 5);
        Category category = new Category("category");
        category.addCampaign(rateCampaign);
        Product product = new Product("product", 10.0, category);
        shoppingCart.addProduct(5, product);

        double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();

        assertEquals(45.0, totalAmount, 0.001);
    }

    @Test
    public void given2DifferentRateCampaignsWhenGetTotalAmountAfterDiscountsCalledOnShoppingCartThenItShouldReturnTotalAmountWithDiscountApplied() {
        Campaign rateCampaign1 = new RateCampaign(10.0, 5);
        Campaign rateCampaign2 = new RateCampaign(30.0, 5);
        Category category = new Category("category");
        category.addCampaign(rateCampaign1);
        category.addCampaign(rateCampaign2);
        Product product = new Product("product", 10.0, category);
        shoppingCart.addProduct(5, product);

        double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();

        assertEquals(30.0, totalAmount, 0.001);
    }

    @Test
    public void givenProductWhichIsLessThenMinimumProductQuantityForCategoryWithRateCampaignAppliedWhenGetTotalAmountAfterDiscountsCalledOnShoppingCartThenItShouldReturnTotalAmountWithoutDiscountApplied() {
        Campaign rateCampaign = new RateCampaign(10.0, 6);
        Category category = new Category("category");
        category.addCampaign(rateCampaign);
        Product product = new Product("product", 10.0, category);
        shoppingCart.addProduct(5, product);

        double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();

        assertEquals(50.0, totalAmount, 0.001);
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

    @Test
    public void givenShoppingCartWhenGetDeliveryCostCalledThenItShouldReturnDeliveryCost() {

        Category category1 = new Category("category1");
        Category category2 = new Category("category2");

        Product product1 = new Product("product1", 5.0, category1);
        Product product2 = new Product("product2", 3.0, category1);
        Product product3 = new Product("product3", 20.0, category2);

        ShoppingCart shoppingCart = new ShoppingCart(new FixedDeliveryCostCalculator(2.0, 3.0));
        shoppingCart.addProduct(2, product1);
        shoppingCart.addProduct(3, product2);
        shoppingCart.addProduct(5, product3);

        double deliveryCost = shoppingCart.getDeliveryCost();

        assertEquals(15.99, deliveryCost, 0.001);
    }

    @Test
    public void givenEmptyShoppingCartWhenGetDeliveryCostCalledThenItShouldReturnFixedCost() {
        assertEquals(2.99, shoppingCart.getDeliveryCost(), 0.001);
    }

    private Product createProduct(String productTitle, double productPrice, String categoryTitle) {
        return new Product(productTitle, productPrice, new Category(categoryTitle));
    }

}
