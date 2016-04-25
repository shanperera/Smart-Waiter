package com.example.pavneetjauhal.smartwaiter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pavneetjauhal on 16-02-02.
 */
public class OrderItems {
    public List<String> userItemToppings = new ArrayList<>();//item toppings for item to send to couchbase
    public String itemName;//item name to send to couchbase
    public String itemPrice;//item price to send to couchbase
    public String specialInstrunctions;//speicial instructions of item to send to couchbase
    public String sideOrder;//side order of item to send to couchbase

    //constructor
    public OrderItems(String itemName, String itemPrice, List<String> itemToppings, String sideOrder, String specialInstructions) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.userItemToppings = itemToppings;
        this.sideOrder = sideOrder;
        this.specialInstrunctions = specialInstructions;
    }

}
