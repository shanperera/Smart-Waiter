package com.example.pavneetjauhal.smartwaiter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by meraj0 on 2016-01-12.
 */
public class UserItems implements Serializable {
    private List<String> userItemToppings = new ArrayList<>();//store toppings for user selected item in cart
    private String itemName;//store item name for selected item in cart
    private String itemPrice;//store item price for selected item in cart
    private String specialInstrunctions;//store special instructions for selected item in cart
    private String sideOrder;//store side order for selected item in cart
    private MenuItems itemObject;//refer to original item information

    //constructor
    public UserItems(String itemName, String itemPrice, ArrayList<String> itemToppings, String sideOrder, MenuItems itemObject, String specialInstructions) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.userItemToppings = itemToppings;
        this.sideOrder = sideOrder;
        this.itemObject = itemObject;
        this.specialInstrunctions = specialInstructions;
        //Log.d("IN USERITEMS CLASS", this.userItemToppings.toString());
    }

    //set special instructions
    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstrunctions = specialInstructions;
    }

    //get item toppings
    public List<String> getItemToppings() {
        //Log.d("RETURNING U CLASS", this.userItemToppings.toString());
        return this.userItemToppings;
    }
    //set item toppings
    public void setItemToppings(ArrayList<String> itemToppings) {
        this.userItemToppings = itemToppings;
    }
    //get special instructions
    public String getSpecialInstrucitons() {
        return this.specialInstrunctions;
    }
    //get side order
    public String getSideOrder() {
        return sideOrder;
    }
    //set side order
    public void setSideOrder(String sideOrder) {
        this.sideOrder = sideOrder;
    }
    //get item name
    public String getItemName() {
        return itemName;
    }
    //get item price
    public String getItemPrice() {
        return itemPrice;
    }
    //get orginal item information
    public MenuItems getMenuItem() {
        return itemObject;
    }

}
