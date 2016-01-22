package com.example.pavneetjauhal.smartwaiter;

import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.couchbase.lite.ReplicationFilter;
import com.couchbase.lite.SavedRevision;
import com.couchbase.lite.android.AndroidContext;
import com.couchbase.lite.replicator.Replication;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by pavneetjauhal on 15-11-16.
 */
public class CouchBaseLite {
    private static final String DB_NAME = "couchbaseevents";
    private static final String DB_ORDER = "couchbaseevents";
    private static final String TAG = "SmartWaiter";
    private static final String HOST = "http://192.168.43.136";
    private static final String PORT = "4984";
    Manager manager = null;
    Database database = null;
    Database database2 = null;
    ArrayList str = new ArrayList();
    Replication push = null;

    /* Key definitions for document */
    private static final String NAME = "Res_Name";
    private static final String CATEGORY = "category";

    public CouchBaseLite(MainActivity mainActivity) throws IOException, CouchbaseLiteException {
        this.manager = new Manager(new AndroidContext(mainActivity), Manager.DEFAULT_OPTIONS);
        this.database = manager.getDatabase(DB_NAME);
        this.database2 = manager.getDatabase(DB_ORDER);
        Log.d(TAG, "################ Create Couch Base Lite database ################");
    }

    public Database getMenuDatabase() throws CouchbaseLiteException {
        if ((this.database == null) & (this.manager != null)) {
            this.database = manager.getDatabase(DB_NAME);
        }
        return database;
    }
    public Database getOrderDatabase() throws CouchbaseLiteException {
        if ((this.database2 == null) & (this.manager != null)) {
            this.database2 = manager.getDatabase(DB_ORDER);
        }
        return database2;
    }

    /* This method is used to extract the restaurant menu associated with specific barcode */
    public Document getRestaurantByBarcode(String barCode){
        Document restaurantMenu = this.database.getDocument(barCode);
        return restaurantMenu;
    }

    public void outputContent(Document restaurantMenu){
        Log.d(TAG, "###### Restaurant Menu Content ######" + restaurantMenu.getProperties());
    }

    public String getRestaurantName(Document restaurantMenu){
        String restaurantName = (String) restaurantMenu.getProperty(this.NAME);
        Log.d(TAG, "###### Restaurant Name = " + restaurantName);
        return restaurantName;
    }

    public ArrayList getCategoriesItems(Document restaurantMenu){
        ArrayList category = new ArrayList();
        category = (ArrayList) restaurantMenu.getProperty(this.CATEGORY);
        Log.d(TAG, "###### Restaurant Category = " + category);
        return category;
    }
    public ArrayList getMenuItems(Document restaurantMenu, String category){
        ArrayList items = new ArrayList();
        items = (ArrayList) restaurantMenu.getProperty(category);
        Log.d(TAG, "###### Restaurant Items = " + items);
        return items;
    }


    public List  getCategoryNames(ArrayList categoryItems){
        List<MenuCategories> menuList = new ArrayList<MenuCategories>();
        String categoryName = null;
        String categoryUrl = null;
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
                    categoryName = (String) me.getValue();
                    categoryNamesList.add(me.getValue());
                }
                if(me.getKey() == "url"){
                    categoryUrl = (String) me.getValue();
                    //categoryNamesList.add(me.getValue());
                }
               // Log.d(TAG, (me.getKey() + ": "));
               // Log.d(TAG, (String) me.getValue());
            }
            menuList.add(new MenuCategories(categoryName, categoryUrl));
            //Log.d(TAG, (String) menuList.get(i).getCategory());
            //Log.d(TAG, (String) categoryNamesList.get(i));
        }
        for (int x = 0; x < menuList.size(); x++ ){
            Log.d(TAG, (String) menuList.get(x).getCategory());
        }
        return menuList;
    }

    public List  getItemNames(ArrayList categoryItems){
        List<MenuItems> itemList = new ArrayList<MenuItems>();
        String itemName = null;
        String itemPrice = null;
        String itemDetail = null;
        //ArrayList categoryNamesList = new ArrayList();
        LinkedHashMap categoryItemsHash = new LinkedHashMap();
        for (int i = 0; i < categoryItems.size(); i++) {
            categoryItemsHash = (LinkedHashMap) categoryItems.get(i);
            Set set = categoryItemsHash.entrySet();
            // Get an iterator
            Iterator j = set.iterator();
            // Display elements
            while (j.hasNext()) {
                Map.Entry me = (Map.Entry) j.next();

                if(me.getKey() == "name"){
                    itemName = (String) me.getValue();
                    //categoryNamesList.add(me.getValue());
                }
                if(me.getKey() == "price"){
                    itemPrice = (String) me.getValue();
                    //categoryNamesList.add(me.getValue());
                }
                if(me.getKey() == "details"){
                    itemDetail = (String) me.getValue();
                    //categoryNamesList.add(me.getValue());
                }
                // Log.d(TAG, (me.getKey() + ": "));
                // Log.d(TAG, (String) me.getValue());
            }
            itemList.add(new MenuItems(itemName, itemPrice, itemDetail));
            //Log.d(TAG, (String) menuList.get(i).getCategory());
            //Log.d(TAG, (String) categoryNamesList.get(i));
        }
        for (int x = 0; x < itemList.size(); x++ ){
            Log.d(TAG, (String) itemList.get(x).getItemName());
        }
        return itemList;
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

    private URL createSyncURL(String host, String port, String db_name) throws MalformedURLException {
        URL syncURL = null;
        syncURL = new URL(host + ":" + port + "/" + db_name);
        return syncURL;
    }

    public void createItem(String text) throws Exception {

        String id = "1234";

        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("id", id);
        properties.put("text", text);
        properties.put("check", false);
        properties.put("owner", "525");
        properties.put("byOwner", "525");
        Document document = this.getOrderDatabase().getDocument("1234");
        document.putProperties(properties);

        Map<String, Object> properties2 = new HashMap<String, Object>();
        properties2.put("id", id);
        properties2.put("text", text);
        properties2.put("check", false);
        properties2.put("owner", "123");
        properties2.put("byOwner", "123");
        Document document2 = this.getOrderDatabase().getDocument("12345");
        document2.putProperties(properties2);
        Log.d(TAG, "Created new grocery item with id: %s" + this.getOrderDatabase().getDocument("12345"));
        str.add("1234");
        Log.d(TAG, "###### Restaurant Menu Content ######" + this.getOrderDatabase().getDocument("1234").getProperties());

    }

    public void setpushfilter() throws CouchbaseLiteException, MalformedURLException {
        // Define a filter that matches only docs with a given "owner" property.
        // The value to match is given as a parameter named "name":

        this.getOrderDatabase().setFilter("byOwner", new ReplicationFilter() {
            @Override
            public boolean filter(SavedRevision revision, Map<String, Object> params) {
                assert revision != null;
                return revision.getProperty("owner") != null && revision.getProperty("owner").equals("525");
            }
        });
        //
        // Set up a filtered push replication using the above filter block,
        // that will push only docs whose "owner" property equals "Waldo":
        Replication push = this.getOrderDatabase().createPushReplication(this.createSyncURL(HOST, PORT, DB_ORDER));
        push.setFilter("byOwner");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "525");
        push.setFilterParams(params);
        push.start();
    }

    public void startReplications() throws CouchbaseLiteException, MalformedURLException {
        final Replication pull = this.getMenuDatabase().createPullReplication(this.createSyncURL(HOST, PORT, DB_NAME));
        //push = this.getOrderDatabase().createPushReplication(this.createSyncURL(HOST, PORT, DB_ORDER));
        //push.start();
        setpushfilter();
        /* For now no need for push replication. Client will not be allowed to change data */
        /* No authentication required for the prototype. Will add back later */
                //Authenticator authenticator = AuthenticatorFactory.createBasicAuthenticator("couchbase_user", "mobile");
                pull.setContinuous(true);
                pull.start();

                pull.addChangeListener(new Replication.ChangeListener()

                                       {
                                           @Override
                                           public void changed(Replication.ChangeEvent event) {
                                               // will be called back when the pull replication status changes
                                               if (pull.getStatus() == Replication.ReplicationStatus.REPLICATION_IDLE) {
                                                   Log.d(TAG, "################ The replication is complete #####################");
                                               } else {
                                                   Log.d(TAG, "################ The replication Failed #####################");
                                               }
                                           }
                                       }

                );
            }

    }