package com.example.pavneetjauhal.couchbaseevents;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Manager;
import com.couchbase.lite.android.AndroidContext;
import com.couchbase.lite.auth.Authenticator;
import com.couchbase.lite.auth.AuthenticatorFactory;
import com.couchbase.lite.replicator.Replication;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String DB_NAME = "couchbaseevents";
    public static final String TAG = "couchbaseevents";
    Manager manager = null;
    Database database = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "Begin Couchbase Events App");
        helloCBL();
        Log.d(TAG, "End Couchbase Events App");
        createSyncURL(true);
        try {
            startReplications();
        } catch(CouchbaseLiteException e) {
            Log.d(TAG, "Connection to couchbase failed");
        }

        }
    private void helloCBL() {
        try {
            manager = new Manager(new AndroidContext(this), Manager.DEFAULT_OPTIONS);
            database = manager.getDatabase(DB_NAME);
        } catch (Exception e) {
            Log.e(TAG, "Error getting database", e);
            return;
        }
        // Create the document
        String documentId = createDocument(database);
        // retrieve the document from the database
        outputContent(database, documentId);
        /* Update the document and add an attachment */
        updateDoc(database, documentId);
        // Add an attachment
        outputContent(database, documentId);
        //addAttachment(database, documentId);
        /* Get and output the contents with the attachment */
        //outputContentsWithAttachment(database, documentId);
    }
    private void outputContent(Database database, String documentId){
        Document retrievedDocument = database.getDocument(documentId);
        // display the retrieved document
        Log.d(TAG, "retrievedDocument=" + String.valueOf(retrievedDocument.getProperties()));

    }
    private String createDocument(Database database) {
        // Create a new document and add data
        Document document = database.createDocument();
        String documentId = document.getId();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "Big Party");
        map.put("location", "My House");
        try {
            // Save the properties to the document
            document.putProperties(map);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }
        return documentId;
    }


    public Database getDatabaseInstance() throws CouchbaseLiteException {
        if ((this.database == null) & (this.manager != null)) {
            this.database = manager.getDatabase(DB_NAME);
        }
        return database;
    }
    public Manager getManagerInstance() throws IOException {
        if (manager == null) {
            manager = new Manager(new AndroidContext(this), Manager.DEFAULT_OPTIONS);
        }
        return manager;
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
    private void updateDoc(Database database, String documentId) {
        Document document = database.getDocument(documentId);
        try {
            // Update the document with more data
            Map<String, Object> updatedProperties = new HashMap<String, Object>();
            updatedProperties.putAll(document.getProperties());
            updatedProperties.put("eventDescription", "Everyone is invited!");
            updatedProperties.put("address", "123 Elm St.");
            updatedProperties.put("This is a test app", "lets see if works");

            // Save to the Couchbase local Couchbase Lite DB
            document.putProperties(updatedProperties);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }
    }

    /* Sync Code */
    private URL createSyncURL(boolean isEncrypted){
        URL syncURL = null;
        String host = "http://192.168.2.12";
        String port = "4984";
        String dbName = "couchbaseevents";
        try {
            syncURL = new URL(host + ":" + port + "/" + dbName);
        } catch (MalformedURLException me) {
            me.printStackTrace();
        }
        return syncURL;
    }

    private void startReplications() throws CouchbaseLiteException {
        Replication pull = this.getDatabaseInstance().createPullReplication(this.createSyncURL(false));
        Replication push = this.getDatabaseInstance().createPushReplication(this.createSyncURL(false));
        //Authenticator authenticator = AuthenticatorFactory.createBasicAuthenticator("couchbase_user", "mobile");
        pull.setContinuous(true);
        push.setContinuous(true);
        pull.start();
        push.start();
    }
}


