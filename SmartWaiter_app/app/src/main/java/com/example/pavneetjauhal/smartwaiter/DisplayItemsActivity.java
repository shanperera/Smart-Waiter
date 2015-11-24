package com.example.pavneetjauhal.smartwaiter;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
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

        popualteItemsListView();
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

            TextView priceText = (TextView) itemView.findViewById(R.id.txtItemPrice);
            priceText.setText(currentItem.getItemPrice());

            return  itemView;
            //return super.getView(position, convertView, parent);
        }

    }

}
