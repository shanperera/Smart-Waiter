package capstone.barcodereader;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private Button scanButton;
    private List<menu> menuList = new ArrayList<menu>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setContentView(R.layout.menu);

        populateMenu();
        popualteListView();


        // Created an onClickListener for the "Scan QR code / Barcode" button
        // When clicked, this activity initialises an IntentIntegrator, a class
        // defined by the ZXing embedded scanner library to initialise the
        // barcode scanner, initiateScan() brings up the local camera app
        // and prompts the user to take a picture of the QR/barcode
        /*
        scanButton = (Button)findViewById(R.id.scanCodeButton);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.initiateScan();
            }
        });
        */

    }

    public void populateMenu(){
        menuList.add(new menu("Appetizers", R.drawable.appet));
        menuList.add(new menu("Sandwiches", R.drawable.dessert));
        menuList.add(new menu("Booze", R.drawable.dessert));
    }

    public void popualteListView(){
        ArrayAdapter<menu> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.menuList);
        list.setAdapter(adapter);
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

    // onActivityResult is the function that is called when the code scanner
    // is given a QR/barcode to read. onActivityResult handles the scan result
    // and stores it in the IntentResult variable. For now the function only
    // prints the information passed through the code to the Log. Further
    // functionality will be added as development progresses
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            String re = scanResult.getContents();
            Log.d("code", re);
        }

    }

    private class MyListAdapter extends ArrayAdapter<menu> {
        public MyListAdapter() {
            super(MainActivity.this, R.layout.item_view, menuList);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            // Make sure we have a view to work with, may have been given null
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);
            }

            //Find the menu item to work with
            menu currentItem = menuList.get(position);

            //Fill the view
            ImageView imageView = (ImageView) itemView.findViewById(R.id.itemIcon);
            imageView.setImageResource(currentItem.getId());

            //Make text view
            TextView makeText = (TextView) itemView.findViewById(R.id.txtCategory);
            makeText.setText(currentItem.getCategory());

            return  itemView;
            //return super.getView(position, convertView, parent);
        }

    }
}
