package com.example.pavneetjauhal.smartwaiter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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

}
