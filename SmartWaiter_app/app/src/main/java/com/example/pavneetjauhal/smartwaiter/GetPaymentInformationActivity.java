package com.example.pavneetjauhal.smartwaiter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.stripe.android.*;
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
                                postToken(token);
                                MainActivity.user.setToken(token);
                                try {
                                    MainActivity.local_database.createItem(MainActivity.user.userItems);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Log.d("TokenSuccess", "Token Success!");
                            }
                            public void onError(Exception error) {
                                Log.d("TokenError", "Token Failed!");
                            }
                        }
                );
            }
            else{

            }
        }catch (Exception e){

        }
    }

    public void postToken(Token token){
        String url = "https://node-js-charge-card.herokuapp.com/";
        String charset = "UTF-8";
        String name = "stripeToken";
        String pToken = token.toString();
        String chargeParameters;

        try{
            String query = String.format("name=%s&pToken=%s",
                    URLEncoder.encode(name, charset),
                    URLEncoder.encode(pToken, charset));

            URLConnection connection = new URL(url).openConnection();
            connection.setDoOutput(true); // Triggers POST.
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

            try (OutputStream output = connection.getOutputStream()) {
                output.write(query.getBytes(charset));
            }

            InputStream response = connection.getInputStream();
            response.close();
        }catch(Exception e){

        }
    }
}
