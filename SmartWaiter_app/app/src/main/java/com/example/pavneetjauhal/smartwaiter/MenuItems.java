package com.example.pavneetjauhal.smartwaiter;

/**
 * Created by meraj0 on 2015-11-22.
 */
public class MenuItems {
    private String itemName;
    private String itemPrice;
    private String itemDetail;

    public MenuItems(String itemName, String itemPrice, String itemDetail){
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemDetail = itemDetail;
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
