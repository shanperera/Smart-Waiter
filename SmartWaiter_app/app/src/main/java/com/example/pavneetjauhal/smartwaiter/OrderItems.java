package com.example.pavneetjauhal.smartwaiter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pavneetjauhal on 16-02-02.
 */
public class OrderItems {
        public List<String> userItemToppings = new ArrayList<>();
        public String itemName;
        public String itemPrice;
        public String specialInstrunctions;
        public String sideOrder;

        public OrderItems (String itemName, String itemPrice, List<String> itemToppings, String sideOrder, String specialInstructions){
            this.itemName = itemName;
            this.itemPrice = itemPrice;
            this.userItemToppings = itemToppings;
            this.sideOrder = sideOrder;
            this.specialInstrunctions = specialInstructions;
        }

}
