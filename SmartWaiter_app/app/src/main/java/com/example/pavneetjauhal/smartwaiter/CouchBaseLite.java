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
    private static final String DB_NAME = "restaurant_menus";
    private static final String DB_ORDER = "local_orders";
    private static final String DB_USER = "user_data";
    public static String restaurant_Address = null;
    private static final String TAG = "SmartWaiter";
    private static final String HOST = "http://192.168.2.10";
    //private static final String HOST = "http://162.243.20.236";
    private static final String PORT = "4984";
    private static String timestamp = null;
    Manager manager = null;
    Database database = null;
    Database database2 = null;
    Database database3 = null;
    ArrayList str = new ArrayList();

    /* Key definitions for document */
    private static final String NAME = "Res_Name";
    private static final String CATEGORY = "category";

    public CouchBaseLite(MainActivity mainActivity) throws IOException, CouchbaseLiteException {
        this.manager = new Manager(new AndroidContext(mainActivity), Manager.DEFAULT_OPTIONS);
        this.database = manager.getDatabase(DB_NAME);
        this.database2 = manager.getDatabase(DB_ORDER);
        this.database3 = manager.getDatabase(DB_USER);
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
    public Database getUserDatabase() throws CouchbaseLiteException {
        if ((this.database3 == null) & (this.manager != null)) {
            this.database3 = manager.getDatabase(DB_USER);
        }
        return database3;
    }

    /* Method to add all user information to local database */
    public void storeUserData(User userData) throws CouchbaseLiteException, NullPointerException {
        Document userdocument = this.getUserDatabase().getDocument("userData");
        Map<String, Object> properties = new HashMap<String,Object>();
        properties.put("First Name", userData.getFirstName());
        properties.put("Last Name", userData.getLastName());
        properties.put("Phone Number", userData.getPhoneNumber());
        properties.put("Postal Code", userData.getPostalCode());
        properties.put("Home Address", userData.getBillingAddress());
        properties.put("Salt", userData.getSalt());
        properties.put("Password", userData.getPassword());
        userdocument.putProperties(properties);
    }

    public void populateUserData() throws CouchbaseLiteException {
        Document userdocument = this.getUserDatabase().getDocument("userData");
        if (userdocument.getProperties() != null && this.getUserDatabase().getDocument("userData") != null){
            MainActivity.user.setFirstName((String) userdocument.getProperty("First Name"));
            MainActivity.user.setLastName((String) userdocument.getProperty("Last Name"));
            MainActivity.user.setBillingAddress((String) userdocument.getProperty("Address"));
            MainActivity.user.setPostalCode((String) userdocument.getProperty("Postal Code"));
            MainActivity.user.setPhoneNumber((String) userdocument.getProperty("Phone Number"));
            MainActivity.user.setSalt((String)userdocument.getProperty("Salt"));
            MainActivity.user.setPassword((String) userdocument.getProperty("Password"));
        } else {
            return;
        }
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
        ArrayList <String> itemToppings = new ArrayList();
        ArrayList <String> itemSides = new ArrayList();
        itemSides = null;
        itemToppings = null;
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
                if(me.getKey() == "toppings"){
                    ArrayList <Object> temp = new ArrayList();
                    temp.add(me.getValue());
                    Log.d("work mofo", "HELLO MOFO"+ temp.get(0));
                    itemToppings = (ArrayList) temp.get(0);
                    //Log.d("work mofo2", "HELLO MOFO2"+ al1.get(0));
                }
                else{
                    itemToppings = null;
                }
                if(me.getKey() == "sides"){
                    ArrayList <Object> temp2 = new ArrayList();
                    temp2.add(me.getValue());
                    Log.d("work mofo", "HELLO MOFOSIDES"+ temp2.get(0));
                    itemSides = (ArrayList) temp2.get(0);
                    //itemSides.add((String) me.getValue());
                }
                //else if(itemSides==null){
                  //  itemSides = null;
                //}
                // Log.d(TAG, (me.getKey() + ": "));
                // Log.d(TAG, (String) me.getValue());
            }
            itemList.add(new MenuItems(itemName, itemPrice, itemDetail,itemToppings,itemSides));
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
    public List<OrderItems>  populateOderitems(List<UserItems> userItems){
        List<OrderItems> orderItems = new ArrayList<>();
        for (int i =0; i < userItems.size(); i++){
            orderItems.add(new OrderItems(userItems.get(i).getItemName(), userItems.get(i).getItemPrice(),userItems.get(i).getItemToppings(), userItems.get(i).getSideOrder(), userItems.get(i).getSpecialInstrucitons()));
        }
        return orderItems;
    }

    public void createItem(List<UserItems> UserItems) throws Exception {
        /* Truncate barcode to extract the table number */
        String tableNumber = MainActivity.qrCode.substring(MainActivity.qrCode.indexOf('-') + 1, MainActivity.qrCode.length());
        timestamp = new String(String.valueOf(System.currentTimeMillis()));
        Map<String, Object> properties = new HashMap<String,Object>();
        properties.put("Table", tableNumber);
        properties.put("First Name", MainActivity.user.getFirstName());
        properties.put("Last Name",MainActivity.user.getLastName());
        properties.put("UserName", MainActivity.user.getUsername());
        properties.put("Total price",MainActivity.user.getTotalPrice());
        List<OrderItems> orderItems = new ArrayList<OrderItems>();
        orderItems = populateOderitems(UserItems);
        properties.put("Items List", orderItems);
        //properties.put("Token", MainActivity.user.getToken());
        properties.put("Address", MainActivity.user.getBillingAddress());
        properties.put("Phone Number", MainActivity.user.getPhoneNumber());
        properties.put("Postal Code", MainActivity.user.getPostalCode());
        properties.put("Current Time", timestamp);
        Document document = this.getOrderDatabase().getDocument(timestamp);
        document.putProperties(properties);

        /*Map<String, Object> properties2 = new HashMap<String, Object>();
        properties2.put("id", "888");
        properties2.put("text", "text");
        properties2.put("check", false);
        properties2.put("owner", "123");
        properties2.put("byOwner", "123");
        properties.put("Current Time", timestamp);
        //Document document2 = this.getOrderDatabase().getDocument("12345");
        //document2.putProperties(properties2);
        Log.d(TAG, "Created new grocery item with id: %s" + this.getOrderDatabase().getDocument("12345"));
        str.add("1234");
        */
        //Log.d(TAG, "###### Restaurant Menu Content ######" + this.getOrderDatabase().getDocument("1234").getProperties());
        setpushfilter(timestamp);
    }

    public void setpushfilter(final String timestamp) throws CouchbaseLiteException, MalformedURLException {
        // Define a filter that matches only docs with a given "Current Time" property.
        // The value to match is given as a parameter named "Current Time":

        this.getOrderDatabase().setFilter("Current Time", new ReplicationFilter() {
            @Override
            public boolean filter(SavedRevision revision, Map<String, Object> params) {
                assert revision != null;
                return revision.getProperty("Current Time") != null && revision.getProperty("Current Time").equals(timestamp);
            }
        });
        //
        // Set up a filtered push replication using the above filter block,
        // that will push only docs whose "owner" property equals "Waldo":
        Replication push = this.getOrderDatabase().createPushReplication(this.createSyncURL(HOST, PORT, restaurant_Address));
        push.setFilter("Current Time");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("Current Time", timestamp);
        push.setFilterParams(params);
        push.start();
        push.setContinuous(true);

    }

    public void startReplications() throws CouchbaseLiteException, MalformedURLException {
        final Replication pull = this.getMenuDatabase().createPullReplication(this.createSyncURL(HOST, PORT, DB_NAME));
        //push = this.getOrderDatabase().createPushReplication(this.createSyncURL(HOST, PORT, DB_ORDER));
        //push.start();
        //setpushfilter();
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