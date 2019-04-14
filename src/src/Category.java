package src;

import src.campaign.Campaign;

import java.util.ArrayList;
import java.util.List;

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

}
