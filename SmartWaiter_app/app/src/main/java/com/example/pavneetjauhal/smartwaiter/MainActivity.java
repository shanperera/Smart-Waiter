package com.example.pavneetjauhal.smartwaiter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {
    CouchBaseLite local_database;
    static List<MenuCategories> menuCategoryList = new ArrayList<MenuCategories>();
    static List<MenuItems> menuItemList = new ArrayList<MenuItems>();
    private Button scanButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //ActionBar actionBar = getActionBar();
        //actionBar.setHomeButtonEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setContentView(R.layout.categorylist);


        // Created an onClickListener for the "Scan QR code / Barcode" button
        // When clicked, this activity initialises an IntentIntegrator, a class
        // defined by the ZXing embedded scanner library to initialise the
        // barcode scanner, initiateScan() brings up the local camera app
        // and prompts the user to take a picture of the QR/barcode


        scanButton = (Button)findViewById(R.id.scanCodeButton);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.initiateScan();
            }
        });

        //onPopulateMenu("456");


    }

    public void onPopulateMenu(String qrCode){

        CouchBaseLite local_database = null;
        try {
            local_database = new CouchBaseLite(this);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        if (local_database != null){
            try {
                local_database.startReplications();
            } catch (CouchbaseLiteException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        /* variables to test parsing */
        ArrayList category = new ArrayList();
        ArrayList items = new ArrayList();
        List<MenuCategories> menuList = new ArrayList<MenuCategories>();
        ArrayList<String> categoryNameList = new ArrayList();

        /* Added to test functionality so far */
        local_database.queryAllRestautant();
        try {
            Document testRestaurantMenu = local_database.getRestaurantByBarcode(qrCode);
            local_database.outputContent(testRestaurantMenu);
            local_database.getRestaurantName(testRestaurantMenu);

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
            Toast.makeText(getApplicationContext(), "Error - Invalid QRcode",
                    Toast.LENGTH_LONG).show();
        }

        /*
        for (int i =0; i < categoryNameList.size(); i++) {
            local_database.getCategoryItems2(testRestaurantMenu, (String) categoryNameList.get(i));
        }
        */
    }



    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            String re = scanResult.getContents();

            Log.d("code", re);
            onPopulateMenu(re);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
