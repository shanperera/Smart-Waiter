package com.example.pavneetjauhal.smartwaiter;

/**
 * Created by meraj0 on 2016-01-12.
 */
public class UserItems {
    private String itemName;
    private String itemPrice;
    private String itemToppings;
    private String specialInstrucitons;

    public UserItems(String itemName, String itemPrice){
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    public void setItemToppings (String itemToppings){
        this.itemToppings = itemToppings;
    }
    public void setSpecialInstructions (String specialInstructions) {
        this.specialInstrucitons = specialInstructions;
    }
    public String getItemToppings(){return itemToppings;}
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
