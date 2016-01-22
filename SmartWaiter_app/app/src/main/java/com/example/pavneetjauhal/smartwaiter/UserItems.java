package com.example.pavneetjauhal.smartwaiter;
import java.util.ArrayList;


/**
 * Created by meraj0 on 2016-01-12.
 */
public class UserItems {
    ArrayList<String> itemToppings = new ArrayList<String>();
    private String itemName;
    private String itemPrice;
    private String specialInstrucitons;

    public UserItems(String itemName, String itemPrice){
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    public void setItemToppings (String itemToppings){
        this.itemToppings.add(itemToppings);
    }
    public void setSpecialInstructions (String specialInstructions) {
        this.specialInstrucitons = specialInstructions;
    }
    public String getItemToppings(){return itemToppings.toString();}
    public String getSpecialInstrucitons() {
        return specialInstrucitons;
    }
    public String getItemName(){
        return itemName;
    }
    public String getItemPrice(){
        return itemPrice;
    }
}
