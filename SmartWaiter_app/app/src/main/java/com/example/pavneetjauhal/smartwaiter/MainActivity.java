package com.example.pavneetjauhal.smartwaiter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.couchbase.lite.Document;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static CouchBaseLite local_database;
    public static String qrCode;
    static List<MenuCategories> menuCategoryList = new ArrayList<MenuCategories>();
    static List<MenuItems> menuItemList = new ArrayList<MenuItems>();
    static String restarauntName = "";
    private Button scanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        local_database = CouchBaseLite.getInstance(this, LoginActivity.user);
        scanButton = (Button) findViewById(R.id.scanCodeButton);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.initiateScan();
            }
        });
    }

    public void onPopulateMenu(String qrCode) {
        /* variables to test parsing */
        ArrayList category;
        ArrayList items;

        /* Added to test functionality so far */
        local_database.queryAllRestautant();
        try {
            CouchBaseLite.restaurant_Address = qrCode.substring(0, qrCode.indexOf('-'));
            Document testRestaurantMenu = local_database.getRestaurantByBarcode(
                    qrCode.substring(qrCode.indexOf('-') + 1, qrCode.length()));
            local_database.outputContent(testRestaurantMenu);
            restarauntName = local_database.getRestaurantName(testRestaurantMenu);

            //Populate Category Names
            category = local_database.getCategoriesItems(testRestaurantMenu);
            menuCategoryList = local_database.getCategoryNames(category);

            //Popualte Menu items
            for (int x = 0; x < menuCategoryList.size(); x++) {
                items = local_database.getMenuItems(testRestaurantMenu,
                        menuCategoryList.get(x).getCategory());
                menuCategoryList.get(x).categoryItems = local_database.getItemNames(items);
            }

            Intent intent = new Intent(this, DisplayCategoriesActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            Log.d("code", String.valueOf(e));
            Toast.makeText(getApplicationContext(), "Error - Invalid QRcode", Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult =
                IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            qrCode = scanResult.getContents();
            //Log.d("code", qrCode);
            if (qrCode != null) {
                onPopulateMenu(qrCode);
            }
        }
    }
}
