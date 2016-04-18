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
    MainActivity mainObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        setTitle(mainObject.currentCategory);
        onDisplayItemList();
    }

    public void onDisplayItemList() {
        popualteItemsListView();
        ListView list = (ListView) findViewById(R.id.menuList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                MenuItems object = mainObject.menuItemList.get(position);
                if (object.getItemToppings() != null) {
                    Intent intent = new Intent(DisplayItemsActivity.this, CustomToppingsActivity.class);
                    intent.putExtra("selectedItem", object);
                    startActivity(intent);

                } else if (object.getItemSides() != null) {
                    Intent intent = new Intent(DisplayItemsActivity.this, CustomSideActivity.class);
                    intent.putExtra("selectedItem", object);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(DisplayItemsActivity.this, SpecialInstrunctionsActivity.class);
                    intent.putExtra("selectedItem", object);
                    startActivity(intent);
                }
            }
        });
    }

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
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
            nameText.setText(currentItem.getItemName());

            TextView descriptionText = (TextView) itemView.findViewById(R.id.txtItemDescription);
            descriptionText.setText(currentItem.getItemDetail());

            TextView priceText = (TextView) itemView.findViewById(R.id.txtitemTopping);
            priceText.setText(Utils.formatCurrency(currentItem.getItemPrice()));
            return itemView;
        }

    }

}
