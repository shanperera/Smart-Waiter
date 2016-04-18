package com.example.pavneetjauhal.smartwaiter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomToppingsActivity extends AppCompatActivity {

    static MenuItems selectedItem;
    UserItems modifyItem;
    CheckBox chBox1;
    ArrayList<String> itemToppings = new ArrayList<String>();
    ArrayList<String> itemToppingsToAdd = new ArrayList<String>();
    FloatingActionButton btnDisplay;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_custom_toppings);

        Intent intent = getIntent();
        selectedItem = (MenuItems) intent.getSerializableExtra("selectedItem");
        modifyItem = (UserItems) intent.getSerializableExtra("modifyOrder");
        Bundle b = getIntent().getExtras();
        index = b.getInt("index");
        if (modifyItem != null) {
            selectedItem = modifyItem.getMenuItem();
            itemToppingsToAdd = (ArrayList<String>) modifyItem.getItemToppings();
        }
        setTitle(selectedItem.getItemName());

        TextView itemDescriptionText = (TextView) findViewById(R.id.txtItemDes);
        itemDescriptionText.setText(selectedItem.getItemDetail());

        TextView itemPriceText = (TextView) findViewById(R.id.txtitemTopping);
        itemPriceText.setText(Utils.formatCurrency(selectedItem.getItemPrice()));

        //create user
        btnDisplay = (FloatingActionButton) findViewById(R.id.nextButton);

        itemToppings = selectedItem.getItemToppings();
        MyAdapter adapter = new MyAdapter(this, itemToppings);
        ListView toppingsListView = (ListView) findViewById(R.id.toppingsList);
        toppingsListView.setAdapter(adapter);
        Log.d("TAG", selectedItem.getItemName());

    }

    public void next(View view) {
        Log.d("TAG", itemToppingsToAdd.toString());
        if (selectedItem.getItemSides() != null) {
            Intent intent = new Intent(this, CustomSideActivity.class);
            intent.putExtra("selectedItem", selectedItem);
            intent.putExtra("itemToppings", itemToppingsToAdd);
            intent.putExtra("modifyOrder", modifyItem);
            Bundle b = new Bundle();
            b.putInt("index", index); //Your id
            intent.putExtras(b);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, SpecialInstrunctionsActivity.class);
            intent.putExtra("selectedItem", selectedItem);
            intent.putExtra("itemToppings", itemToppingsToAdd);
            intent.putExtra("modifyOrder", modifyItem);
            Bundle b = new Bundle();
            b.putInt("index", index); //Your id
            intent.putExtras(b);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public class MyAdapter extends ArrayAdapter<String> {

        private final Context context;
        private final List<String> itemsArrayList;

        public MyAdapter(CustomToppingsActivity context, List<String> itemsArrayList) {

            super(context, R.layout.toppings_view, itemsArrayList);

            this.context = context;
            this.itemsArrayList = itemsArrayList;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            // 1. Create inflater
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // 2. Get rowView from inflater
            View rowView = inflater.inflate(R.layout.toppings_view, parent, false);

            // 3. Get the two text view from the rowView
            TextView labelView = (TextView) rowView.findViewById(R.id.txtName);

            // 4. Set the text for textView
            labelView.setText(itemsArrayList.get(position));

            chBox1 = (CheckBox) rowView.findViewById(R.id.checkTopping);
            if (modifyItem != null && modifyItem.getItemToppings().contains(itemsArrayList.get(position))) {
                chBox1.setChecked(true);
            }
            Intent intent = getIntent();
            ArrayList<String> itemAdded = new ArrayList<String>();
            itemAdded = (ArrayList<String>) intent.getSerializableExtra("itemToppings");
            if (itemAdded != null && itemAdded.contains(itemsArrayList.get(position))) {
                chBox1.setChecked(true);
            }
            chBox1.setTag(position);
            chBox1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox checkBox = (CheckBox) v;
                    if (checkBox.isChecked()) {
                        itemToppingsToAdd.add(itemsArrayList.get(position));
                    } else {
                        itemToppingsToAdd.remove(itemsArrayList.get(position));
                    }
                }
            });

            return rowView;
        }
    }

}