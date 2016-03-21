package com.example.pavneetjauhal.smartwaiter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Document;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static CouchBaseLite local_database;
    public static String qrCode;
    static List<MenuCategories> menuCategoryList = new ArrayList<MenuCategories>();
    static List<MenuItems> menuItemList = new ArrayList<MenuItems>();
    static User user = new User();
    static String restarauntName = "";
    private Button scanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean checkExists = true;
        //ActionBar actionBar = getActionBar();
        //actionBar.setHomeButtonEnabled(true);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //this.setTitle(getResources().getString(R.string.title_activity_scan));
        //Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
       // setSupportActionBar(myToolbar);
        try {
            local_database = new CouchBaseLite(this);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        if (local_database != null) {
            try {
                local_database.startReplications();
            } catch (CouchbaseLiteException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        try {
            local_database.populateUserData();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
            checkExists = false;
        } catch (NullPointerException e) {
            checkExists = false;
            e.printStackTrace();
        }
        //setContentView(R.layout.categorylist);


        Intent intent = new Intent("com.example.pavneetjauhal.smartwaiter.LoginActivity");
        startActivity(intent);

        // Created an onClickListener for the "Scan QR code / Barcode" button
        // When clicked, this activity initialises an IntentIntegrator, a class
        // defined by the ZXing embedded scanner library to initialise the
        // barcode scanner, initiateScan() brings up the local camera app
        // and prompts the user to take a picture of the QR/barcode
        String password = "";


        if (!checkExists) {
            intent = new Intent("com.example.pavneetjauhal.smartwaiter.AccountCreationActivity");
            startActivity(intent);
        }

        scanButton = (Button) findViewById(R.id.scanCodeButton);

        //onPopulateMenu("couchbaseevents-777");
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.initiateScan();
            }
        });
        //onPopulateMenu("456");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_login, menu);
        MenuInflater mif = getMenuInflater();
        mif.inflate(R.menu.main_actionbar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void onPopulateMenu(String qrCode){


        /* variables to test parsing */
        ArrayList category = new ArrayList();
        ArrayList items = new ArrayList();
        List<MenuCategories> menuList = new ArrayList<MenuCategories>();
        ArrayList<String> categoryNameList = new ArrayList();

        /* Added to test functionality so far */
        local_database.queryAllRestautant();
        try {
            local_database.restaurant_Address = qrCode.substring(0, qrCode.indexOf('-'));
            Document testRestaurantMenu = local_database.getRestaurantByBarcode(qrCode.substring(qrCode.indexOf('-') + 1, qrCode.length()));
            local_database.outputContent(testRestaurantMenu);
            restarauntName = local_database.getRestaurantName(testRestaurantMenu);

            //Populate Category Names
            category = local_database.getCategoriesItems(testRestaurantMenu);
            menuCategoryList = local_database.getCategoryNames(category);

            //Popualte Menu items
            for(int x=0; x < menuCategoryList.size(); x++) {
                items = local_database.getMenuItems(testRestaurantMenu, menuCategoryList.get(x).getCategory());
                menuCategoryList.get(x).categoryItems = local_database.getItemNames(items);
            }

            Intent intent = new Intent("com.example.pavneetjauhal.smartwaiter.DisplayCategoriesActivity");
            startActivity(intent);
        }
        catch (Exception e){
            Log.d("code", String.valueOf(e));
            Toast.makeText(getApplicationContext(), "Error - Invalid QRcode",
                    Toast.LENGTH_LONG).show();
        }
        /* Truncate barcode to extract the order server address */
        //local_database.restaurant_Address = qrCode.substring(0, qrCode.indexOf('-'));

        /*
        for (int i =0; i < categoryNameList.size(); i++) {
            local_database.getCategoryItems2(testRestaurantMenu, (String) categoryNameList.get(i));
        }
        */
    }



    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            qrCode = scanResult.getContents();

            //Log.d("code", qrCode);
            if(qrCode != null) {
                //onPopulateMenu(re);
                onPopulateMenu(qrCode);
            }else
                return;

        }


    }


    /*public void onDestroy() {
        Log.d("couchbaseevents", "###### DESTROY CALLED #######");
        super.onDestroy();
        try {
            local_database.database.delete();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        System.exit(0);

    }*/

}
