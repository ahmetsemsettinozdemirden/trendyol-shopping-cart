package src;

import src.campaign.Campaign;

public class Category {

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
