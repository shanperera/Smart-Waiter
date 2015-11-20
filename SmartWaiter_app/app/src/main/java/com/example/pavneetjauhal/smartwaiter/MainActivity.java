package com.example.pavneetjauhal.smartwaiter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity {
    CouchBaseLite local_database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        ArrayList<String> categoryNameList = new ArrayList();
        /* Added to test functionality so far */
        local_database.queryAllRestautant();
        Document testRestaurantMenu = local_database.getRestaurantByBarcode("456");
        local_database.outputContent(testRestaurantMenu);
        local_database.getRestaurantName(testRestaurantMenu);
        category = local_database.getCategoriesItems(testRestaurantMenu);
        categoryNameList = local_database.getCategoryNames(category);
        for (int i =0; i < categoryNameList.size(); i++) {
            local_database.getCategoryItems2(testRestaurantMenu, (String) categoryNameList.get(i));
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
