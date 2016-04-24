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
    /*
* Method used to get stripe token
*
* output - token
*/
    public Token getToken() {
        return token;
    }
    /*
* Method used to set stripe token
*
* input - token
*/
    public void setToken(Token token) {
        this.token = token;
    }

    public String getCustomerID() {
        return customerID;
    }
    /*
* Method used to get username
*
* output - username
*/
    public String getUsername() {
        return username;
    }
    /*
* Method used to set username
*
* input - username
*/
    public void setUsername(String username) {
        this.username = username;
    }
    /*
* Method used to get password
*
* output - password
*/
    public String getPassword() {
        return password;
    }
    /*
* Method used to set password
*
* input - password
*/
    public void setPassword(String password) {
        this.password = password;
    }
    /*
* Method used to get hash salt name
*
* output - salt
*/
    public String getSalt() {
        return salt;
    }
    /*
* Method used to set hash salt name
*
* input - salt
*/
    public void setSalt(String salt) {
        this.salt = salt;
    }
    /*
* Method used to get first name
*
* output - first name
*/
    public String getFirstName() {
        return firstName;
    }
    /*
* Method used to set first name
*
* input - first name
*/
    public void setFirstName(String firstName) {
        if (firstName == null) {
            return;
        } else {
            this.firstName = firstName;
        }
    }
    /*
* Method used to get Last name
*
* output - last name
*/
    public String getLastName() {
        return lastName;
    }
    /*
* Method used to set Last name
*
* input - last name
*/
    public void setLastName(String lastName) {
        if (lastName == null) {
            return;
        } else {
            this.lastName = lastName;
        }
    }
    /*
* Method used to get billing address
*
* output - billing address
*/
    public String getBillingAddress() {
        return billingAddress;
    }
    /*
* Method used to set billing address
*
* input - billing address
*/
    public void setBillingAddress(String billingAddress) {
        if (billingAddress == null) {
            return;
        } else {
            this.billingAddress = billingAddress;
        }
    }
    /*
* Method used to get postal code
*
* output - post code
*/
    public String getPostalCode() {
        return postalCode;
    }

    /*
* Method used to set postal code
*
* input - post code
*/
    public void setPostalCode(String postalCode) {
        if (postalCode != null) {
            this.postalCode = postalCode;
        }
    }
    /*
 * Method used to get user phone number
 *
 * Output - phonenumber
 */
    public String getPhoneNumber() {
        return phoneNumber;
    }
    /*
 * Method used to set user phone number
 *
 * input - phonenumber
 */
    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber != null) {
            this.phoneNumber = phoneNumber;
        }
    }
}
