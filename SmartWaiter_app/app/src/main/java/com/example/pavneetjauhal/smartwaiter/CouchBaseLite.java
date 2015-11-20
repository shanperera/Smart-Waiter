package com.example.pavneetjauhal.smartwaiter;

import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.couchbase.lite.android.AndroidContext;
import com.couchbase.lite.replicator.Replication;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by pavneetjauhal on 15-11-16.
 */
public class CouchBaseLite {
    private static final String DB_NAME = "couchbaseevents";
    private static final String TAG = "couchbaseevents";
    private static final String HOST = "http://192.168.2.12";
    private static final String PORT = "4984";
    Manager manager = null;
    Database database = null;

    /* Key definitions for document */
    private static final String NAME = "Res_Name";
    private static final String CATEGORY = "category";

    public CouchBaseLite(MainActivity mainActivity) throws IOException, CouchbaseLiteException {
        this.manager = new Manager(new AndroidContext(mainActivity), Manager.DEFAULT_OPTIONS);
        this.database = manager.getDatabase(DB_NAME);
        Log.d(TAG, "################ Create Couch Base Lite database ################");
    }

    public Database getDatabaseInstance() throws CouchbaseLiteException {
        if ((this.database == null) & (this.manager != null)) {
            this.database = manager.getDatabase(DB_NAME);
        }
        return database;
    }

    /* This method is used to extract the restaurant menu associated with specific barcode */
    public Document getRestaurantByBarcode(String barCode){
        Document restaurantMenu = this.database.getDocument(barCode);
        return restaurantMenu;
    }

    public void outputContent(Document restaurantMenu){
        Log.d(TAG, "###### Restaurant Menu Content ######" + restaurantMenu.getProperties());
    }

    public void getRestaurantName(Document restaurantMenu){
        String restaurantName = (String) restaurantMenu.getProperty(this.NAME);
        Log.d(TAG, "###### Restaurant Name = " + restaurantName);
    }

    public ArrayList getCategoriesItems(Document restaurantMenu){
        ArrayList category = new ArrayList();
        category = (ArrayList) restaurantMenu.getProperty(this.CATEGORY);
        Log.d(TAG, "###### Restaurant Category = " + category);
        return category;
    }

    public ArrayList getCategoryNames(ArrayList categoryItems){
        ArrayList categoryNamesList = new ArrayList();
        LinkedHashMap categoryNames = new LinkedHashMap();
        for (int i = 0; i < categoryItems.size(); i++) {
            categoryNames = (LinkedHashMap) categoryItems.get(i);
            Set set = categoryNames.entrySet();
            // Get an iterator
            Iterator j = set.iterator();
            // Display elements
            while (j.hasNext()) {
                Map.Entry me = (Map.Entry) j.next();
                if(me.getKey() == "type"){
                    categoryNamesList.add(me.getValue());
                }
               // Log.d(TAG, (me.getKey() + ": "));
               // Log.d(TAG, (String) me.getValue());
            }
            Log.d(TAG, (String) categoryNamesList.get(i));
        }
        return categoryNamesList;
    }

    public void getCategoryItems2(Document restaurantMenu, String categoryName){
        ArrayList listOfItems = new ArrayList();
        listOfItems = (ArrayList) restaurantMenu.getProperty(categoryName);
        Log.d(TAG, String.format("###### Section = %s = %s", categoryName , listOfItems));
    }

    public void getValuesFromList(ArrayList arrayItems) {
        LinkedHashMap values_hash = new LinkedHashMap();
        for (int i = 0; i < arrayItems.size(); i++) {
            values_hash = (LinkedHashMap) arrayItems.get(i);
            Set set = values_hash.entrySet();
            // Get an iterator
            Iterator j = set.iterator();
            // Display elements
            while (j.hasNext()) {
                Map.Entry me = (Map.Entry) j.next();
                //Log.d(TAG, (me.getKey() + ": "));
                Log.d(TAG, (String) me.getValue());
            }
        }
    }


    /* Method used for testing for now. Can be used later to query all docs */
    public void queryAllRestautant(){
        Query query = database.createAllDocumentsQuery();
        query.setAllDocsMode(Query.AllDocsMode.ALL_DOCS);
        QueryEnumerator result = null;
        try {
            result = query.run();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        for (Iterator<QueryRow> it = result; it.hasNext(); ) {
            QueryRow row = it.next();
            Log.d(TAG, "#### All #### " + row.getDocumentId());
        }
    }

    private URL createSyncURL() throws MalformedURLException {
        URL syncURL = null;
        syncURL = new URL(this.HOST + ":" + this.PORT + "/" + this.DB_NAME);
        return syncURL;
    }

    public void startReplications() throws CouchbaseLiteException, MalformedURLException {
        final Replication pull = this.getDatabaseInstance().createPullReplication(this.createSyncURL());
        /* For now no need for push replication. Client will not be allowed to change data */
        //Replication push = this.getDatabaseInstance().createPushReplication(this.createSyncURL(false));
        //push.setContinuous(true);
        //push.start();

        /* No authentication required for the prototype. Will add back later */
        //Authenticator authenticator = AuthenticatorFactory.createBasicAuthenticator("couchbase_user", "mobile");

        pull.setContinuous(true);
        pull.start();

        pull.addChangeListener(new Replication.ChangeListener() {
            @Override
            public void changed(Replication.ChangeEvent event) {
                // will be called back when the pull replication status changes
                if (pull.getStatus() == Replication.ReplicationStatus.REPLICATION_IDLE) {
                    Log.d(TAG, "################ The replication is complete #####################");
                } else {
                    Log.d(TAG, "################ The replication Failed #####################");
                }
            }
        });
    }

}