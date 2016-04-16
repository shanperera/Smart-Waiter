package com.example.pavneetjauhal.smartwaiter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meraj0 on 2015-11-22.
 */
public class MenuCategories {

    List<MenuItems> categoryItems = new ArrayList<MenuItems>();
    private String categoryName;
    private String picUrl;

    public MenuCategories(String categoryName, String picUrl) {
        this.categoryName = categoryName;
        this.picUrl = picUrl;
    }

    public String getCategory() {
        return categoryName;
    }

    public String getId() {
        return picUrl;
    }
}
