package com.example.pavneetjauhal.smartwaiter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Shan on 2016-01-14.
 */

public class GetPaymentInformationActivity extends AppCompatActivity {

    public String restID = "00";
    public String[] spk = new String[3]; //Stripe private keys for different restaurants


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_information);

        populatePrivateKeys();
        populateSpinner();
    }

    //Uses the same private key, this method is to demonstrate that
    //unique stripe accounts can be used for each restaurant
    //so that they get paid directly
    public void populatePrivateKeys(){
        spk[0] = "pk_test_YvxrNPpmntwiq44Rp4HkAYuT";
        spk[1] = "pk_test_YvxrNPpmntwiq44Rp4HkAYuT";
        spk[2] = "pk_test_YvxrNPpmntwiq44Rp4HkAYuT";
    }

    //Method for populating the credit card expiry
    //month and year spinner
    public void populateSpinner() {
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

    //Method for retrieve the credit card information to create a token
    public void getInfo(View button) {
        EditText getFields;
        Spinner spinner1, spinner2;

        getFields = (EditText) findViewById(R.id.cardNumberEditText);
        String cardNumber = getFields.getText().toString();

        getFields = (EditText) findViewById(R.id.cvcEditText);
        String cardCVC = getFields.getText().toString();

        spinner1 = (Spinner) findViewById(R.id.spinner);
        String cardExpMonth = spinner1.getSelectedItem().toString();

        spinner2 = (Spinner) findViewById(R.id.spinner2);
        String cardExpYear = "20" + spinner2.getSelectedItem().toString();
        int cardMonth = 0, cardYear = 0;
        try {
            cardMonth = Integer.parseInt(cardExpMonth);
            cardYear = Integer.parseInt(cardExpYear);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (cardMonth == 0 || cardYear == 0 || cardNumber.isEmpty() || cardCVC.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Invalid card details", Toast.LENGTH_LONG)
                    .show();
        }
        //Checks for a valid internet connection
        else if (!Utils.isOnline(this)) {
            Toast.makeText(getApplicationContext(), "No internet Connection",
                    Toast.LENGTH_LONG).show();
        } else {
            Card card = new Card(cardNumber, cardMonth, cardYear, cardCVC);
            try {
                Stripe stripe;
                switch(restID){
                    case "00":
                        stripe = new Stripe(spk[0]);
                        break;
                    case "01":
                        stripe = new Stripe(spk[1]);
                        break;
                    case "02":
                        stripe = new Stripe(spk[2]);
                        break;
                    default:
                        stripe = new Stripe(spk[0]);
                        break;
                }

                //Stripe method for Luhn check
                if (card.validateCard()) {
                    stripe.createToken(
                            card,
                            new TokenCallback() { //TokenCallback contacts Stripe server for token
                                public void onSuccess(Token token) {
                                    //1-use token with credit card details returned from stripe server
                                    //POST the token to our Heroku web server
                                    postToken(token);
                                    LoginActivity.user.setToken(token);
                                    try {
                                        MainActivity.local_database.createItem(LoginActivity.user.userItems);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    Log.d("TokenSuccess", "Token Success!");
                                    LoginActivity.user.userItems.clear();
                                    Intent intent = new Intent(GetPaymentInformationActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "Order Sent Successfully",
                                            Toast.LENGTH_LONG).show();
                                }

                                public void onError(Exception error) {
                                    Log.d("TokenError", "Token Failed!");
                                }
                            }
                    );
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid Card Details",
                            Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Encode token as string to POST to Heroku webserver
    public void postToken(Token token) {
        String url = "http://charge-card-sw.herokuapp.com/";
        String charset = "UTF-8";
        String name = "stripeToken";
        String pToken = token.toString();
        String sAmount = LoginActivity.user.getTotalPrice();

        if(sAmount == null){
            Toast.makeText(getApplicationContext(), "amount to pay is NULL", Toast.LENGTH_LONG)
                    .show();
            sAmount = "0";
        }
        else{
            double amount = Double.parseDouble(sAmount);
            amount = amount * 100; // Stripe calculates payments in cents, convert amount to equivalent value in cents
            sAmount = String.valueOf(amount);
            Log.i("AMOUNT TAG", sAmount);
        }

        try {
            String query = String.format("name=%s&pToken=%s&amount=%s",
                    URLEncoder.encode(name, charset),
                    URLEncoder.encode(pToken, charset),
                    URLEncoder.encode(sAmount, charset));

            URLConnection connection = new URL(url).openConnection();
            connection.setDoOutput(true); // Triggers POST.
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
            connection.connect();

            try (OutputStream output = connection.getOutputStream()) {
                output.write(query.getBytes(charset));
            }

            InputStream response = connection.getInputStream();
            response.close();
            Log.d("WebSuccess", "Web Success!");
        } catch (Exception e) {
            Log.d("WebFail", "Web Fail!");
        }
    }
}
