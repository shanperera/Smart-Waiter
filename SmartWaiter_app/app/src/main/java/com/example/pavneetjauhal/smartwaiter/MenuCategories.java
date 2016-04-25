package com.example.pavneetjauhal.smartwaiter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meraj0 on 2015-11-22.
 */
public class MenuCategories {

    List<MenuItems> categoryItems = new ArrayList<MenuItems>();
    private String categoryName;//store category name
    private String picUrl;//store pic url

    //constructor
    public MenuCategories(String categoryName, String picUrl) {
        this.categoryName = categoryName;
        this.picUrl = picUrl;
    }

    public String getCategory() {
        return categoryName;
    }//get category name

    public String getId() {
        return picUrl;
    }//get picture url
}
