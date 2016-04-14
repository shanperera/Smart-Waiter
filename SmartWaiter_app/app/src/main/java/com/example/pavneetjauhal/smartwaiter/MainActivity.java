package com.example.pavneetjauhal.smartwaiter;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Document;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    public static CouchBaseLite local_database;
    public static String qrCode;
    static List<MenuCategories> menuCategoryList = new ArrayList<MenuCategories>();
    static List<MenuItems> menuItemList = new ArrayList<MenuItems>();
    static User user = new User();
    static String restarauntName = "";
    private Button scanButton;

    private String[] SettingsMenu;
    public DrawerLayout mDrawerLayout;
    public ListView mDrawerList;
    public ActionBarDrawerToggle mDrawerToggle;
    public CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        boolean checkExists = true;
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        sideMenuSetup();

        //Couchbase Setup
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

    public void sideMenuSetup(){
        //ActionBar setup

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        //this.setTitle(getResources().getString(R.string.title_activity_scan));
        //Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        // setSupportActionBar(myToolbar);

        SettingsMenu = getResources().getStringArray(R.array.menu_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, SettingsMenu));
        // Set the list's click listener
        //mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mTitle = "test";

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, SettingsMenu));
        // Set the list's click listener
        //mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                //getActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                //getActionBar().setTitle(mTitle);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);

        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header, mDrawerList,
                false);
        ViewGroup footer = (ViewGroup) inflater.inflate(R.layout.footer, mDrawerList,
                false);
        mDrawerList.addHeaderView(header, null, false);
        mDrawerList.addFooterView(footer, null, false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_login, menu);
        //MenuInflater mif = getMenuInflater();
        //mif.inflate(R.menu.main_actionbar,menu);
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



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position) {
        Toast.makeText(this, R.string.app_name, Toast.LENGTH_SHORT).show();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(SettingsMenu[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
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
