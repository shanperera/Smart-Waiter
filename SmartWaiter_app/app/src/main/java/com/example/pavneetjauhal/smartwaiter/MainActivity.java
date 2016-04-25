package com.example.pavneetjauhal.smartwaiter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 42;//set camera premession
    public static CouchBaseLite local_database;//create local databse
    public static String qrCode;//String to store qrcode scanned
    static List<MenuCategories> menuCategoryList = new ArrayList<MenuCategories>();//list to store menu category names
    static List<MenuItems> menuItemList = new ArrayList<MenuItems>();//list to store menu items
    static String currentCategory = "";//string to store current category
    static String restarauntName = "";//string to store current restaraunt name
    private Button scanButton;//button for intiate scan
    private boolean isGranted;//access granted
    public int failCount = 0;//scan failure count

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        local_database = CouchBaseLite.getInstance(this, LoginActivity.user);
        scanButton = (Button) findViewById(R.id.scanCodeButton);//set scan button

        // check Android 6 permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)//check if camera action is permitted
                == PackageManager.PERMISSION_GRANTED) {
            isGranted = true;

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        }

        //listen for scan barcode button click
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGranted) {
                    IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                    integrator.initiateScan();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);
                }
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
            startActivity(intent);//call camera
        } catch (Exception e) {
            Log.d("code", String.valueOf(e));
            failCount ++;
            if (failCount < 3) {//check if scan failed 3 times
                Toast.makeText(getApplicationContext(), "Error - Please try again", Toast.LENGTH_LONG)//throw common error if less then 3
                        .show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Error - Please contact host", Toast.LENGTH_LONG)//throw error prompting to contact host
                        .show();
            }
        }
    }

    //called when qrcode scanned
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult =
                IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null && isGranted) {//check if result is non null and access granted
            qrCode = scanResult.getContents();//store scan result
            //Log.d("code", qrCode);
            if (qrCode != null) {//check if result is not null
                onPopulateMenu(qrCode);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isGranted = true;
                } else {
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                }
            }
        }
    }
}
