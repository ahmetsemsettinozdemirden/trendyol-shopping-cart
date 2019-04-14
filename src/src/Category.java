package src;

import src.campaign.Campaign;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Category {

    private String title;
    private Category parentCategory;
    private List<Campaign> campaigns;

    public Category(String title) {
        this(title, null);
    }

    public Category(String title, Category parentCategory) {
        this.title = title;
        this.parentCategory = parentCategory;
        this.campaigns = new ArrayList<>();
    }

    public boolean addCampaign(Campaign campaign) {
        return !campaigns.contains(campaign) && campaigns.add(campaign);
    }

    public List<Campaign> getAppliedCampaigns(int productQuantity) {
        return campaigns.stream()
                .filter(campaign -> campaign.isApplicable(productQuantity))
                .collect(Collectors.toList());
    }

    public boolean inCategory(Category category) {
        return category == this || (parentCategory != null && parentCategory.inCategory(category));
    }

    public List<Category> getAllParents() {
        List<Category> categories = new ArrayList<>();
        Category category = this;
        while (category != null) {
            categories.add(category);
            category = category.parentCategory;
        }
        return categories;
    }

}
