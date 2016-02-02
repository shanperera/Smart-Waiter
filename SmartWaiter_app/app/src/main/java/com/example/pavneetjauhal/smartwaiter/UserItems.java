package com.example.pavneetjauhal.smartwaiter;
import java.util.ArrayList;
import java.io.Serializable;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by meraj0 on 2016-01-12.
 */
public class UserItems implements Serializable {
    private List<String> userItemToppings = new ArrayList<>();
    private String itemName;
    private String itemPrice;
    private String specialInstrunctions;
    private String sideOrder;
    private MenuItems itemObject;

    public UserItems(String itemName, String itemPrice, ArrayList<String> itemToppings, String sideOrder, MenuItems itemObject, String specialInstructions){
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.userItemToppings = itemToppings;
        this.sideOrder = sideOrder;
        this.itemObject = itemObject;
        this.specialInstrunctions = specialInstructions;
        //Log.d("IN USERITEMS CLASS", this.userItemToppings.toString());
    }
    public void setSideOrder(String sideOrder){
        this.sideOrder = sideOrder;
    }
    public void setMenuItem(MenuItems itemObject){
        this.itemObject = itemObject;
    }

    public void setItemToppings (ArrayList<String> itemToppings){
        this.userItemToppings = itemToppings;
    }

    public void setSpecialInstructions (String specialInstructions) {
        this.specialInstrunctions = specialInstructions;
    }
    public List<String> getItemToppings(){
        //Log.d("RETURNING U CLASS", this.userItemToppings.toString());
        return  this.userItemToppings;
    }
    public String getSpecialInstrucitons() {
        return this.specialInstrunctions;
    }
    public String getSideOrder() {
        return sideOrder;
    }
    public String getItemName(){
        return itemName;
    }
    public String getItemPrice(){
        return itemPrice;
    }
    public MenuItems getMenuItem(){
        return itemObject;
    }
}
