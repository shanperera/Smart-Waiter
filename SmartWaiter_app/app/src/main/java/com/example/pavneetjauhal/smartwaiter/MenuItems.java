package com.example.pavneetjauhal.smartwaiter;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by meraj0 on 2015-11-22.
 */
public class MenuItems implements Serializable {
    private String itemName;
    private String itemPrice;
    private String itemDetail;
    private ArrayList<String> itemToppings = new ArrayList<String>();
    private ArrayList<String> itemSides = new ArrayList<String>();

    public MenuItems(String itemName, String itemPrice, String itemDetail, ArrayList<String> itemToppings, ArrayList<String> itemSides){
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemDetail = itemDetail;
        this.itemToppings = itemToppings;
        this.itemSides = itemSides;
    }

    public void setItemName(String itemName){
        this.itemName = itemName;
    }
    public void setItemPrice(String itemPrice){
        this.itemPrice = itemPrice;
    }
    public void setItemDetail(String itemDetail){
        this.itemDetail = itemDetail;
    }
    public void setItemToppings(ArrayList<String> itemToppings){
        this.itemToppings = itemToppings;
    }
    public void setItemSides(ArrayList<String> itemSides){
        this.itemSides = itemSides;
    }
    public ArrayList<String> getItemToppings(){return itemToppings;}
    public ArrayList<String> getItemSides(){return itemSides;}
    public String getItemName(){
        return itemName;
    }
    public String getItemDetail(){
        return itemDetail;
    }
    public String getItemPrice(){
        return itemPrice;
    }
}
