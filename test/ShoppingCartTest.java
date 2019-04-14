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

    private Category category;
    private Category subcategory;
    private Product product;
    private Product subproduct;

    private Category category1;
    private Category subcategory1;
    private Category subcategory2;
    private Category category2;
    private Product product1;
    private Product product2;
    private Product product3;

    @Before
    public void setUp() {
        shoppingCart = new ShoppingCart(new FixedDeliveryCostCalculator(2.0, 3.0));

        // for singular product tests
        category = new Category("category");
        subcategory = new Category("subcategory", category);
        product = new Product("product", 1.0, category);
        subproduct = new Product("subproduct", 2.0, subcategory);

        // for multiple products tests
        category1 = new Category("category1");
        subcategory1 = new Category("subcategory1", category1);
        subcategory2 = new Category("subcategory2", category1);
        category2 = new Category("category2");

        product1 = new Product("product1", 5.0, subcategory1);
        product2 = new Product("product2", 3.0, subcategory2);
        product3 = new Product("product3", 20.0, category2);
    }

    @Test
    public void givenProductsForDifferentCategoriesWithoutAnyDiscountsWhenGetTotalAmountAfterDiscountsCalledOnShoppingCartThenItShouldReturnTotalAmount() {
        shoppingCart.addProduct(2, product1);
        shoppingCart.addProduct(3, product2);
        shoppingCart.addProduct(5, product3);

        double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();

        assertEquals(119.0, totalAmount, 0.001);
    }

    @Test
    public void givenOneProductWithSingleCategoryWhenGetTotalAmountAfterDiscountCalledOnShoppingCartThenItShouldReturnProductPrice() {
        shoppingCart.addProduct(2, product);

        double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();

        assertEquals(2.0, totalAmount, 0.001);
    }

    @Test
    public void givenShoppingCartWith1ProductWhenAddProductCalledWithSameProductAndQuantity1ThenAddProductReturns2() {
        shoppingCart.addProduct(1, product);

        int productQuantity = shoppingCart.addProduct(1, product);

        assertEquals(2, productQuantity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenShoppingCartWith1ProductWhenAddProductCalledWithInvalidQuantityThenItShouldThrowInvalidArgumentException() {
        shoppingCart.addProduct(1, product);

        shoppingCart.addProduct(-1, product);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenEmptyShoppingCartWhenAddProductCalledWithNullProductThenItShouldThrowInvalidArgumentException() {
        shoppingCart.addProduct(1, null);
    }

    @Test
    public void givenShoppingCartWithProductsAndDiscountsWhenGetTotalAmountAfterDiscountsCalledThenItShouldReturnTotalAmount() {
        Campaign rateCampaign = new RateCampaign(25.0, 3);
        Campaign amountCampaign = new AmountCampaign(5.0, 2);
        category1.addCampaign(rateCampaign);
        subcategory1.addCampaign(amountCampaign);

        shoppingCart.addProduct(3, product1);
        shoppingCart.addProduct(2, product2);
        shoppingCart.addProduct(5, product3);

        double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();

        assertEquals(110.75, totalAmount, 0.001);
    }

    @Test
    public void givenProductsForSubcategoriesWithRateCampaignAppliedToParentCategoryWhenGetTotalAmountAfterDiscountsCalledOnShoppingCartThenItShouldReturnTotalAmountWithDiscountApplied() {
        Campaign rateCampaign = new RateCampaign(20.0, 5);
        category.addCampaign(rateCampaign);
        shoppingCart.addProduct(2, product);
        shoppingCart.addProduct(3, subproduct);

        double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();

        assertEquals(6.4, totalAmount, 0.001);
    }

    @Test
    public void givenProductsForSubcategoriesWithRateCampaignAppliedToSubcategoryWhenGetTotalAmountAfterDiscountsCalledOnShoppingCartThenItShouldReturnTotalAmountWithoutDiscountApplied() {
        Campaign rateCampaign = new RateCampaign(20.0, 6);
        category.addCampaign(rateCampaign);
        shoppingCart.addProduct(2, product);
        shoppingCart.addProduct(3, subproduct);

        double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();

        assertEquals(8.0, totalAmount, 0.001);
    }

    @Test
    public void givenAmountCampaignWhenGetTotalAmountAfterDiscountsCalledOnShoppingCartThenItShouldReturnTotalAmountWithDiscountApplied() {
        Campaign amountCampaign = new AmountCampaign(1.0, 5);
        category.addCampaign(amountCampaign);
        shoppingCart.addProduct(5, product);

        double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();

        assertEquals(4.0, totalAmount, 0.001);
    }

    @Test
    public void given2DifferentAmountCampaignsWhenGetTotalAmountAfterDiscountsCalledOnShoppingCartThenItShouldReturnTotalAmountWithDiscountApplied() {
        Campaign amountCampaign1 = new AmountCampaign(1.0, 5);
        Campaign amountCampaign2 = new AmountCampaign(1.5, 5);
        category.addCampaign(amountCampaign1);
        category.addCampaign(amountCampaign2);
        shoppingCart.addProduct(5, product);

        double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();

        assertEquals(2.5, totalAmount, 0.001);
    }

    @Test
    public void givenProductWhichIsLessThenMinimumProductQuantityForCategoryWithAmountCampaignAppliedWhenGetTotalAmountAfterDiscountsCalledOnShoppingCartThenItShouldReturnTotalAmountWithoutDiscountApplied() {
        Campaign amountCampaign = new AmountCampaign(5.0, 6);
        category.addCampaign(amountCampaign);
        shoppingCart.addProduct(5, product);

        double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();

        assertEquals(5.0, totalAmount, 0.001);
    }

    @Test
    public void givenRateCampaignWhenGetTotalAmountAfterDiscountsCalledOnShoppingCartThenItShouldReturnTotalAmountWithDiscountApplied() {
        Campaign rateCampaign = new RateCampaign(10.0, 5);
        category.addCampaign(rateCampaign);
        shoppingCart.addProduct(5, product);

        double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();

        assertEquals(4.5, totalAmount, 0.001);
    }

    @Test
    public void given2DifferentRateCampaignsWhenGetTotalAmountAfterDiscountsCalledOnShoppingCartThenItShouldReturnTotalAmountWithDiscountApplied() {
        Campaign rateCampaign1 = new RateCampaign(10.0, 5);
        Campaign rateCampaign2 = new RateCampaign(30.0, 5);
        category.addCampaign(rateCampaign1);
        category.addCampaign(rateCampaign2);
        shoppingCart.addProduct(5, product);

        double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();

        assertEquals(3.0, totalAmount, 0.001);
    }

    @Test
    public void givenProductWhichIsLessThenMinimumProductQuantityForCategoryWithRateCampaignAppliedWhenGetTotalAmountAfterDiscountsCalledOnShoppingCartThenItShouldReturnTotalAmountWithoutDiscountApplied() {
        Campaign rateCampaign = new RateCampaign(10.0, 6);
        category.addCampaign(rateCampaign);
        shoppingCart.addProduct(5, product);

        double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();

        assertEquals(5.0, totalAmount, 0.001);
    }

    @Test
    public void givenRateCampaignWhenAddCampaignCalledThenItReturnsTrue() {
        Campaign rateCampaign = new RateCampaign(20.0, 5);

        assertTrue(category.addCampaign(rateCampaign));
    }

    @Test
    public void givenRateCampaignWithCategoryAlreadyAddedWhenAddCampaignCalledThenItReturnsFalse() {
        Campaign rateCampaign = new RateCampaign(20.0, 5);
        category.addCampaign(rateCampaign);

        assertFalse(category.addCampaign(rateCampaign));
    }

    @Test
    public void givenAmountCampaignWhenAddCampaignCalledThenItReturnsTrue() {
        Campaign amountCampaign = new AmountCampaign(10.0, 5);

        assertTrue(category.addCampaign(amountCampaign));
    }

    @Test
    public void givenAmountCampaignWithCategoryAlreadyAddedWhenAddCampaignCalledThenItReturnsFalse() {
        Campaign amountCampaign = new AmountCampaign(10.0, 5);
        category.addCampaign(amountCampaign);

        assertFalse(category.addCampaign(amountCampaign));
    }

    @Test
    public void givenShoppingCartWithProductAndCouponsWhenGetTotalAmountAfterDiscountsCalledThenItShouldReturnTotalAmount() {
        shoppingCart.addProduct(10, product);

        Coupon rateCoupon = new RateCoupon(10.0, 10.0);
        Coupon amountCoupon = new AmountCoupon(8.0, 2.5);

        shoppingCart.addCoupon(rateCoupon);
        shoppingCart.addCoupon(amountCoupon);

        double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();

        assertEquals(6.5, totalAmount, 0.001);
    }

    @Test
    public void givenShoppingCartWithProductAndRateCouponWhenGetTotalAmountAfterDiscountsCalledThenItShouldReturnTotalAmountWithDiscount() {
        shoppingCart.addProduct(10, product);

        Coupon rateCoupon = new RateCoupon(10.0, 10.0);
        shoppingCart.addCoupon(rateCoupon);

        double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();

        assertEquals(9.0, totalAmount, 0.001);
    }

    @Test
    public void givenShoppingCartWithProductAndAmountCouponWhenGetTotalAmountAfterDiscountsCalledThenItShouldReturnTotalAmountWithDiscount() {
        shoppingCart.addProduct(10, product);

        Coupon amountCoupon = new AmountCoupon(10.0, 2.0);
        shoppingCart.addCoupon(amountCoupon);

        double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();

        assertEquals(8.0, totalAmount, 0.001);
    }

    @Test
    public void givenShoppingCartWithCampaignsAndCouponsWhenGetTotalAmountAfterDiscountsCalledThenItShouldReturnTotalAmountWithDiscounts() {
        Campaign rateCampaign = new RateCampaign(25.0, 3);
        Campaign amountCampaign = new AmountCampaign(5.0, 2);
        category1.addCampaign(rateCampaign);
        subcategory1.addCampaign(amountCampaign);

        shoppingCart.addProduct(3, product1);
        shoppingCart.addProduct(2, product2);
        shoppingCart.addProduct(5, product3);

        Coupon rateCoupon = new RateCoupon(10.0, 10.0);
        Coupon amountCoupon = new AmountCoupon(8.0, 2.5);

        shoppingCart.addCoupon(rateCoupon);
        shoppingCart.addCoupon(amountCoupon);

        double totalAmount = shoppingCart.getTotalAmountAfterDiscounts();

        assertEquals(97.175, totalAmount, 0.001);
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

    @Test
    public void givenShoppingCartWhenPrintCalledItReturnsShoppingCartOutput() {
        Campaign rateCampaign = new RateCampaign(25.0, 3);
        Campaign amountCampaign = new AmountCampaign(5.0, 2);
        category1.addCampaign(rateCampaign);
        subcategory1.addCampaign(amountCampaign);

        shoppingCart.addProduct(3, product1);
        shoppingCart.addProduct(2, product2);
        shoppingCart.addProduct(5, product3);

        Coupon rateCoupon = new RateCoupon(10.0, 10.0);
        Coupon amountCoupon = new AmountCoupon(8.0, 2.5);

        shoppingCart.addCoupon(rateCoupon);
        shoppingCart.addCoupon(amountCoupon);

        assertEquals("" +
                "=== PRODUCTS ===\n" +
                "- category1\n" +
                "  - subcategory1\n" +
                "    * product1: quantity: 3, unitPrice: 20.0TL, totalPrice: 100.0TL\n" +
                "  - subcategory2\n" +
                "    * product2: quantity: 2, unitPrice: 20.0TL, totalPrice: 100.0TL\n" +
                "- category2\n" +
                "  * product3: quantity: 5, unitPrice: 20.0TL, totalPrice: 100.0TL\n" +
                "=== CAMPAIGNS ===\n" +
                "- category1\n" +
                "  # rateCampaign: minProductQuantity: 3, rate: %25.0\n" +
                "  - subcategory1\n" +
                "    # amountCampaign: minProductQuantity: 2, amount: 5.0TL\n" +
                "=== COUPONS ===\n" +
                "- rateCoupon: minPurchaseAmount: 10.0TL, rate: %10.0\n" +
                "- amountCoupon: minPurchaseAmount: 8.0TL, amount: 2.5TL\n" +
                "=== SUMMARY ===\n" +
                "totalAmount: 97.17TL\n" +
                "deliveryCost: 15.99TL\n", shoppingCart.toString());
    }

}
