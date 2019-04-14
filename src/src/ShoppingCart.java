package src;

import src.campaign.Campaign;
import src.coupon.Coupon;
import src.delivery.DeliveryCostCalculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ShoppingCart {

    private DeliveryCostCalculator deliveryCostCalculator;
    private Map<Product, Integer> productQuantities;
    private List<Coupon> coupons;

    public ShoppingCart(DeliveryCostCalculator deliveryCostCalculator) {
        this.deliveryCostCalculator = deliveryCostCalculator;
        this.productQuantities = new HashMap<>();
        this.coupons = new ArrayList<>();
    }

    public int addProduct(int quantity, Product product) {
        if (quantity <= 0)
            throw new IllegalArgumentException("invalid quantity: " + quantity);
        if (product == null)
            throw new IllegalArgumentException("product is null");

        if (!productQuantities.containsKey(product))
            productQuantities.put(product, quantity);
        else
            productQuantities.replace(product, productQuantities.get(product) + quantity);

        return productQuantities.get(product);
    }

    public boolean addCoupon(Coupon coupon) {
        if(coupon.isApplicable(getTotalAmountAfterDiscounts()))
            return coupons.add(coupon);
        return false;
    }

    private Set<Category> findAllCategories() {
        Set<Category> categories = new HashSet<>();
        for (Product product: productQuantities.keySet())
            categories.addAll(product.getCategory().getAllParents());
        return categories;
    }

    private List<Product> findProducts(Category category) {
        return productQuantities.keySet().stream()
                .filter(product -> product.getCategory().inCategory(category))
                .collect(Collectors.toList());
    }

    private double getTotalCartAmount() {
        return calculateTotalPrice(new ArrayList<>(productQuantities.keySet()));
    }

    public double getCampaignDiscount() {
        double totalDiscount = 0;
        for (Category category: findAllCategories()) {
            List<Product> products = findProducts(category);
            int productQuantity = calculateProductQuantity(products);
            List<Campaign> campaigns = category.getAppliedCampaigns(productQuantity);
            for (Campaign campaign: campaigns) {
                totalDiscount += campaign.getDiscount(calculateTotalPrice(products));
            }
        }
        return totalDiscount;
    }

    private double getTotalAmountAfterCampaignDiscount() {
        return getTotalCartAmount() - getCampaignDiscount();
    }

    public double getCouponDiscount() {
        return coupons.stream()
                .mapToDouble(coupon -> coupon.getDiscount(getTotalAmountAfterCampaignDiscount()))
                .sum();
    }

    public double getTotalAmountAfterDiscounts() {
        return getTotalAmountAfterCampaignDiscount() - getCouponDiscount();
    }

    private Set<Category> getCategories() {
        Set<Category> categories = new HashSet<>();
        for (Product product: productQuantities.keySet()) {
            categories.add(product.getCategory());
        }
        return categories;
    }

    private int getNumberOfDeliveries() {
        return getCategories().size();
    }

    private int getNumberOfProducts() {
        return productQuantities.entrySet().size();
    }

    public double getDeliveryCost() {
        return deliveryCostCalculator.calculateFor(getNumberOfDeliveries(), getNumberOfProducts());
    }

    private int calculateProductQuantity(List<Product> products) {
        return products.stream()
                .mapToInt(product -> productQuantities.get(product))
                .sum();
    }

    private double calculateTotalPrice(List<Product> products) {
        return products.stream()
                .mapToDouble(product -> productQuantities.get(product) * product.getPrice())
                .sum();
    }

    @Override
    public String toString() {
        String productsOutput = getProductsAsText();
        String campaignsOutput = getCampaignsAsText();
        String couponsOutput = getCouponsAsText();
        return "=== PRODUCTS ===\n" +
                ("".equals(productsOutput) ? "no products.\n" : productsOutput) +
                "=== CAMPAIGNS ===\n" +
                ("".equals(campaignsOutput) ? "no campaigns.\n" : campaignsOutput) +
                "=== COUPONS ===\n" +
                ("".equals(couponsOutput) ? "no coupons.\n" : couponsOutput) +
                "=== SUMMARY ===\n" +
                "totalAmount: " + String.format("%.2f", getTotalAmountAfterDiscounts()) + "TL\n" +
                "deliveryCost: " + String.format("%.2f", getDeliveryCost()) + "TL\n";
    }

    private String getProductsAsText() {
        String productsStr = "";
        List<Category> categories = new ArrayList<>(getCategories());
        categories.sort(null);
        for (Category category: categories)
            productsStr += getProductCategoryText(category);
        return productsStr;
    }

    private String getProductCategoryText(Category category) {
        String text = "- " + category + "\n";
        List<Product> products = findProducts(category);
        for (Product product: products)
            text += "  * " + product +
                    ", quantity: " + productQuantities.get(product) +
                    ", totalPrice: " + String.format("%.2f", (productQuantities.get(product) * product.getPrice())) + "TL\n";
        return text;
    }

    private String getCampaignsAsText() {
        String campaignsStr = "";
        List<Category> categories = new ArrayList<>(findAllCategories());
        categories.sort(null);
        for (Category category: categories) {
            campaignsStr += getCampaignCategoryText(category);
        }
        return campaignsStr;
    }

    private String getCampaignCategoryText(Category category) {
        String text = "";
        List<Product> products = findProducts(category);
        int productQuantity = calculateProductQuantity(products);
        List<Campaign> campaigns = category.getAppliedCampaigns(productQuantity);

        if (!campaigns.isEmpty()) {
            text += "- " + category + "\n";
            for (Campaign campaign : campaigns) {
                text += "  # " + campaign + "\n";
            }
        }
        return text;
    }

    private String getCouponsAsText() {
        String couponsStr = "";
        for (Coupon coupon: coupons)
            couponsStr += "- " + coupon.toString() + "\n";
        return couponsStr;
    }

}
