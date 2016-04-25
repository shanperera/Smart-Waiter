package com.example.pavneetjauhal.smartwaiter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by meraj0 on 2015-11-22.
 */
public class MenuItems implements Serializable {
    private String itemName;//store item name
    private String itemPrice;//store item price
    private String itemDetail;//store item information
    private ArrayList<String> itemToppings = new ArrayList<String>();//store item toppings avaible
    private ArrayList<String> itemSides = new ArrayList<String>();//store item sides avaible

    //constructor
    public MenuItems(String itemName, String itemPrice, String itemDetail, ArrayList<String> itemToppings, ArrayList<String> itemSides) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemDetail = itemDetail;
        this.itemToppings = itemToppings;
        this.itemSides = itemSides;
    }

    //get item toppings
    public ArrayList<String> getItemToppings() {
        return itemToppings;
    }
    //get item sides
    public ArrayList<String> getItemSides() {
        return itemSides;
    }
    //get item name
    public String getItemName() {
        return itemName;
    }
    //get item description
    public String getItemDetail() {
        return itemDetail;
    }
    //get item price
    public String getItemPrice() {
        return itemPrice;
    }

}
