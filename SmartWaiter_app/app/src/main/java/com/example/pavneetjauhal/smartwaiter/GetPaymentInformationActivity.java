package com.example.pavneetjauhal.smartwaiter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.stripe.android.*;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.model.Charge;
import com.stripe.model.Customer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shan on 2016-01-14.
 *
 *
 */

public class GetPaymentInformationActivity extends AppCompatActivity{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_information);

        populateSpinner();
    }

    public void populateSpinner(){
        Spinner monthSpinner, yearSpinner;

        //Find XML spinner object
        monthSpinner = (Spinner) findViewById(R.id.spinner);
        yearSpinner = (Spinner) findViewById(R.id.spinner2);
        //Convert XML string-array: month_array to a List<String> so it can be used as a parameter for
        //the ArrayAdapter: aArray
        List<String> sArrayToList = Arrays.asList(getResources().getStringArray(R.array.month_array));
        ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, sArrayToList);
        aAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(aAdapter);

        List<String> yArrayToList = Arrays.asList(getResources().getStringArray(R.array.year_array));
        ArrayAdapter<String> yAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, yArrayToList);
        yAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yAdapter);
    }

    public void getInfo(View button){
        EditText getFields;
        Spinner spinner1, spinner2;

        getFields = (EditText) findViewById(R.id.cardNumberEditText);
        String cardNumber = getFields.getText().toString();

        getFields = (EditText) findViewById(R.id.cvcEditText);
        String cardCVC = getFields.getText().toString();

        spinner1 = (Spinner) findViewById(R.id.spinner);
        String cardExpMonth = spinner1.getSelectedItem().toString();
        int cardMonth = Integer.parseInt(cardExpMonth);

        spinner2 = (Spinner) findViewById(R.id.spinner2);
        String cardExpYear = "20" + spinner2.getSelectedItem().toString();
        int cardYear = Integer.parseInt(cardExpYear);

        Card card = new Card(cardNumber, cardMonth, cardYear, cardCVC);
        try{
            Stripe stripe = new Stripe("pk_test_YvxrNPpmntwiq44Rp4HkAYuT");

            if(card.validateCard()){
                stripe.createToken(
                        card,
                        new TokenCallback() {
                            public void onSuccess(Token token) {
                                createCharge(token);
                                try {
                                    MainActivity.local_database.createItem(MainActivity.user.userItems);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            public void onError(Exception error) {
                            }
                        }
                );
            }
            else{

            }
        }catch (Exception e){

        }
    }

    public void createCharge(Token token){
        final Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("amount", 400);
        chargeParams.put("currency", "usd");
        chargeParams.put("card", token.getId());


        try{
            new AsyncTask<Void, Void, Void>() {

                Charge charge;

                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        com.stripe.Stripe.apiKey = "sk_test_RTfwxOfsg2wTLsPkP0xaY4Lm";
                        charge = Charge.create(chargeParams);
                    } catch (Exception e) {
                    }
                    return null;
                }

                protected void onPostExecute(Void result) {
                    //Toast.makeText(getPaymentInformationActivity.this,
                    //        "Card Charged : " + charge.getCreated() + "\nPaid : " + charge.getPaid(),
                    //        Toast.LENGTH_LONG
                    //).show();
                };

            }.execute();
        }
        catch(Exception e){

        }
    }

    public void storeToken(Token token){
        //Stripe API key
        com.stripe.Stripe.apiKey = "sk_test_RTfwxOfsg2wTLsPkP0xaY4Lm";

        //Customer Parameters HashMap
        Map<String, Object> customerParams = new HashMap<String, Object>();
        customerParams.put("description", "exampleCustomer");
        customerParams.put("card", token.getId()); // Obtained in onSuccess() method of TokenCallback
        // while creating token above

        //Create a Customer
        try {
            Customer cust = Customer.create(customerParams);
        }catch(Exception e){

        }

        /*
            save Customer Id into database for later use.
            Customer ID can be obtained as cust.getId()
        */

        //chargeCustomer(user);
    }

    public void chargeCustomer(User user){
        //Stripe API key
        com.stripe.Stripe.apiKey = "sk_test_RTfwxOfsg2wTLsPkP0xaY4Lm";

        //Retrieve saved customer ID from database
        String cust_id = user.getCustomerID(); //getSavedCustomerId() method should retrieve saved customer Id from db

        //Charge Parameters HashMap
        final Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("amount", 400);
        chargeParams.put("currency", "usd");
        chargeParams.put("customer", cust_id); //Use customer instead of card

        //Charge customer
        try{
            Charge.create(chargeParams);
        }
        catch(Exception e){

        }

    }

}
