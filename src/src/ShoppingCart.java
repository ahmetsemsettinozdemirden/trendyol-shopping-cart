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
        for (Product product: productQuantities.keySet()) {
            categories.addAll(product.getCategory().getAllParents());
        }
        return categories;
    }

    private List<Product> findProducts(Category category) {
        return productQuantities.keySet().stream()
                .filter(product -> product.getCategory().inCategory(category))
                .collect(Collectors.toList());
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

    public double getCampaignDiscount() {
        double totalDiscount = 0;
        for (Category category: findAllCategories()) {

            List<Product> products = findProducts(category);
            int productQuantity = products.stream().mapToInt(product -> productQuantities.get(product)).sum();
            List<Campaign> campaigns = category.getAppliedCampaigns(productQuantity);

            double totalPrice = products.stream().mapToDouble(product -> productQuantities.get(product) * product.getPrice()).sum();
            for (Campaign campaign: campaigns) {
                totalDiscount += campaign.getDiscount(totalPrice);
            }
        }
        return totalDiscount;
    }

    public double getCouponDiscount() {
        return coupons.stream().mapToDouble(coupon -> coupon.getDiscount(getTotalAmountAfterCampaignDiscount())).sum();
    }

    private double getTotalAmountAfterCampaignDiscount() {
        return getTotalAmount() - getCampaignDiscount();
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
        for (Category category: categories) {
            productsStr += "- " + category + "\n";
            List<Product> products = findProducts(category);
            for (Product product: products) {
                productsStr += "  * " + product +
                        ", quantity: " + productQuantities.get(product) +
                        ", totalPrice: " + String.format("%.2f", (productQuantities.get(product) * product.getPrice())) + "TL\n";
            }
        }
        return productsStr;
    }

    private String getCampaignsAsText() {
        String campaignsStr = "";
        List<Category> categories = new ArrayList<>(findAllCategories());
        categories.sort(null);
        for (Category category: categories) {

            List<Product> products = findProducts(category);
            int productQuantity = products.stream().mapToInt(product -> productQuantities.get(product)).sum();
            List<Campaign> campaigns = category.getAppliedCampaigns(productQuantity);

            if (!campaigns.isEmpty()) {
                campaignsStr += "- " + category + "\n";
                for (Campaign campaign : campaigns) {
                    campaignsStr += "  # " + campaign + "\n";
                }
            }
        }
        return campaignsStr;
    }

    private String getCouponsAsText() {
        String couponsStr = "";
        for (Coupon coupon: coupons) {
            couponsStr += "- " + coupon.toString() + "\n";
        }
        return couponsStr;
    }

}
