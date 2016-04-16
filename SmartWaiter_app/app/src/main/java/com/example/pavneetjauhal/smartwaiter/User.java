package com.example.pavneetjauhal.smartwaiter;

import com.stripe.android.model.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shan on 2016-01-12.
 * <p/>
 * TO-DO:
 * Implement a more secure method for storing a user's password, cannot go to production and still
 * store user's sensitive information in plain text
 * <p/>
 * CHANGELOG
 * JAN 12, 2015:
 * Created User.java
 */


public class User {
    //methods for adding menu items to cart
    List<UserItems> userItems = new ArrayList<UserItems>();
    private String salt;
    private String username;
    private String password; //storing the password in plain text for now
    private String firstName;
    private String lastName;
    private String billingAddress; // Example "125 Royal Ave"
    private String postalCode;
    private String phoneNumber; // Stored as "9055554213" no dashes for coding simplicity
    private Token token;
    private String customerID;

    public User() {
    }

    //All credentials given
    public User(String salt, String password, String firstName, String lastName, String billingAddress, String postalCode, String phoneNumber) {
        this.salt = salt;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.billingAddress = billingAddress;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
    }

    public User(String customerID) {
        this.customerID = customerID;
    }

    public void createUserItem(String itemName, String itemPrice, ArrayList<String> itemToppings, String sideOrder, MenuItems itemObject, String specialInstructions) {
        this.userItems.add(new UserItems(itemName, itemPrice, itemToppings, sideOrder, itemObject, specialInstructions));
    }

    public void removeUserItem(int index) {
        userItems.remove(index);
    }

    public String getTotalPrice() {
        double price = 0;
        for (int x = 0; x < userItems.size(); x++) {
            price = price + Double.parseDouble(userItems.get(x).getItemPrice());
        }
        price = (double) Math.round(price * 100d) / 100d;
        return String.valueOf(price);
    }

    ;

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (firstName == null) {
            return;
        } else {
            this.firstName = firstName;
        }
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (lastName == null) {
            return;
        } else {
            this.lastName = lastName;
        }
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        if (billingAddress == null) {
            return;
        } else {
            this.billingAddress = billingAddress;
        }
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        if (postalCode != null) {
            this.postalCode = postalCode;
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber != null) {
            this.phoneNumber = phoneNumber;
        }
    }
}
