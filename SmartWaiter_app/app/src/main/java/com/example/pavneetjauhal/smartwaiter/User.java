package com.example.pavneetjauhal.smartwaiter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shan on 2016-01-12.
 *
 * TO-DO:
 * Implement a more secure method for storing a user's password, cannot go to production and still
 * store user's sensitive information in plain text
 *
 * CHANGELOG
 * JAN 12, 2015:
 * Created User.java
 *
 */


public class User {
    private String username;
    private String password; //storing the password in plain text for now
    private String firstName;
    private String lastName;
    private String billingAddress; // Example "125 Royal Ave"
    private String postalCode;
    private String phoneNumber; // Stored as "9055554213" no dashes for coding simplicity


    //methods for adding menu items to cart
    List<UserItems> userItems = new ArrayList<UserItems>();;
    public void createUserItem(String itemName, String itemPrice) {
        this.userItems.add(new UserItems(itemName, itemPrice));
    }
    public void removeUserItem(int index){
        userItems.remove(index);
    }

    public String getTotalPrice(){
        double price = 0;
        for (int x = 0; x < userItems.size(); x++){
            price = price +  Double.parseDouble(userItems.get(x).getItemPrice());
        }
        return String.valueOf(price);
    }

    public User(){}

    //All credentials given
    public User(String username, String password, String firstName, String lastName, String billingAddress, String postalCode, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.billingAddress = billingAddress;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
