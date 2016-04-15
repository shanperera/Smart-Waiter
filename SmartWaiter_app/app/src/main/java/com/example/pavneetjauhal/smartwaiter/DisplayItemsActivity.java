package com.example.pavneetjauhal.smartwaiter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DisplayItemsActivity extends AppCompatActivity {
    MainActivity mainObject;
    Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_items);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarItems);
        setSupportActionBar(toolbar);

        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbarItems);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle(" Category Items");

        onDisplayItemList();
    }

    public void onDisplayItemList(){
        //setContentView(R.layout.categorylist);
        popualteItemsListView();

        ListView list = (ListView) findViewById(R.id.itemList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                MenuItems object = mainObject.menuItemList.get(position);
                Intent intent = new Intent("com.example.pavneetjauhal.smartwaiter.CustomToppingsActivity");
                intent.putExtra("selectedItem", object);
                startActivity(intent);
                //onDisplayItemList();
            }
        });
    }

    public void popualteItemsListView(){
        ArrayAdapter<MenuItems> adapter = new ItemListAdapter();
        ListView list = (ListView) findViewById(R.id.itemList);
        list.setAdapter(adapter);
    }

    private class ItemListAdapter extends ArrayAdapter<MenuItems> {
        public ItemListAdapter() {
            super(DisplayItemsActivity.this, R.layout.item_view, mainObject.menuItemList);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            // Make sure we have a view to work with, may have been given null
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);
            }

            //Find the menu item to work with
            MenuItems currentItem = mainObject.menuItemList.get(position);

            //Make text view
            TextView nameText = (TextView) itemView.findViewById(R.id.txtItemName);
            nameText.setText(currentItem.getItemName());

            TextView descriptionText = (TextView) itemView.findViewById(R.id.txtItemDescription);
            descriptionText.setText(currentItem.getItemDetail());

            TextView priceText = (TextView) itemView.findViewById(R.id.txtitemTopping);
            priceText.setText(currentItem.getItemPrice());
            return  itemView;
            //return super.getView(position, convertView, parent);
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
        if (id == R.id.action_cart) {
            Intent intent = new Intent("com.example.pavneetjauhal.smartwaiter.CartActivity");
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            Intent intent = new Intent("com.example.pavneetjauhal.smartwaiter.DisplayCategoriesActivity");
            startActivity(intent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
