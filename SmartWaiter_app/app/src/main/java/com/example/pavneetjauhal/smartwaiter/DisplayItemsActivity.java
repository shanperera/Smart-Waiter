package com.example.pavneetjauhal.smartwaiter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DisplayItemsActivity extends AppCompatActivity {
    MainActivity mainObject;//refer to MainActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        setTitle(mainObject.currentCategory);//set category name
        onDisplayItemList();//display items in category
    }

    public void onDisplayItemList() {
        popualteItemsListView();//populat list view with item names
        ListView list = (ListView) findViewById(R.id.menuList);
        //listen for item press
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                MenuItems object = mainObject.menuItemList.get(position);//get selected item
                if (object.getItemToppings() != null) {//check if toppings availble to add
                    Intent intent = new Intent(DisplayItemsActivity.this, CustomToppingsActivity.class);
                    intent.putExtra("selectedItem", object);//pass selected item
                    startActivity(intent);//call CustomToppingsActivity

                } else if (object.getItemSides() != null) {//check if item sides avaible to add
                    Intent intent = new Intent(DisplayItemsActivity.this, CustomSideActivity.class);
                    intent.putExtra("selectedItem", object);//pass selected item
                    startActivity(intent);//call CustomSideActivity
                } else {
                    Intent intent = new Intent(DisplayItemsActivity.this, SpecialInstrunctionsActivity.class);
                    intent.putExtra("selectedItem", object);//pass selected item
                    startActivity(intent);//call SpecialInstructionsActivity
                }
            }
        });
    }
    //populate list view with item names
    public void popualteItemsListView() {
        ArrayAdapter<MenuItems> adapter = new ItemListAdapter();
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
        int id = item.getItemId();

        //check if cart button pressed
        if (id == R.id.action_cart) {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);//call CartActivity
        }

        return super.onOptionsItemSelected(item);
    }

    private class ItemListAdapter extends ArrayAdapter<MenuItems> {
        public ItemListAdapter() {
            super(DisplayItemsActivity.this, R.layout.item_view, mainObject.menuItemList);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with, may have been given null
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);
            }

            //Find the menu item to work with
            MenuItems currentItem = mainObject.menuItemList.get(position);

            //Make text view
            TextView nameText = (TextView) itemView.findViewById(R.id.txtItemName);
            nameText.setText(currentItem.getItemName());//display item name

            TextView descriptionText = (TextView) itemView.findViewById(R.id.txtItemDescription);
            descriptionText.setText(currentItem.getItemDetail());//display item description

            TextView priceText = (TextView) itemView.findViewById(R.id.txtitemTopping);
            priceText.setText(Utils.formatCurrency(currentItem.getItemPrice()));//display item price
            return itemView;
        }

    }

}
