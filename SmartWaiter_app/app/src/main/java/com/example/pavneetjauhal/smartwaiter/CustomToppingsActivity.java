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
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomToppingsActivity extends AppCompatActivity {

    static MenuItems selectedItem;//store selected item
    UserItems modifyItem;//set if item to be modified from CartActivity
    CheckBox toppingsCheckBox;//check box for toppings
    ArrayList<String> itemToppings = new ArrayList<String>();//store item toppings availble
    ArrayList<String> itemToppingsToAdd = new ArrayList<String>();//store item toppings to add
    FloatingActionButton btnDisplay;
    int index;//get index of current item

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_custom_toppings);

        Intent intent = getIntent();
        selectedItem = (MenuItems) intent.getSerializableExtra("selectedItem");//get selected item info
        modifyItem = (UserItems) intent.getSerializableExtra("modifyOrder");//get item to modify from CartActivity
        Bundle b = getIntent().getExtras();//get arguments
        index = b.getInt("index");//get current item index
        if (modifyItem != null) {//check if item to be modified
            selectedItem = modifyItem.getMenuItem();//get item info
            itemToppingsToAdd = (ArrayList<String>) modifyItem.getItemToppings();//get current toppings chosen
        }
        setTitle(selectedItem.getItemName());//display item name

        TextView itemDescriptionText = (TextView) findViewById(R.id.txtItemDes);
        itemDescriptionText.setText(selectedItem.getItemDetail());//display item information

        TextView itemPriceText = (TextView) findViewById(R.id.txtitemTopping);
        itemPriceText.setText(Utils.formatCurrency(selectedItem.getItemPrice()));//display item price

        //create user
        btnDisplay = (FloatingActionButton) findViewById(R.id.nextButton);

        itemToppings = selectedItem.getItemToppings();//get toppings availble for item
        MyAdapter adapter = new MyAdapter(this, itemToppings);//create toppings adapter
        ListView toppingsListView = (ListView) findViewById(R.id.toppingsList);
        toppingsListView.setAdapter(adapter);//set toppings list view
        Log.d("TAG", selectedItem.getItemName());

    }

    //call on checkmark button press for next page
    public void next(View view) {
        Log.d("TAG", itemToppingsToAdd.toString());
        if (selectedItem.getItemSides() != null) {//check if side avaiblle for item selected
            Intent intent = new Intent(this, CustomSideActivity.class);
            intent.putExtra("selectedItem", selectedItem);//pass item selected
            intent.putExtra("itemToppings", itemToppingsToAdd);//pass item toppings added
            intent.putExtra("modifyOrder", modifyItem);//pass item modified
            Bundle b = new Bundle();
            b.putInt("index", index); //Your id
            intent.putExtras(b);
            startActivity(intent);//call CustomSideActivity
            finish();
        } else {
            Intent intent = new Intent(this, SpecialInstrunctionsActivity.class);
            intent.putExtra("selectedItem", selectedItem);//pass item selected
            intent.putExtra("itemToppings", itemToppingsToAdd);//pass item toppings added
            intent.putExtra("modifyOrder", modifyItem);//pass item modified
            Bundle b = new Bundle();
            b.putInt("index", index); //Your id
            intent.putExtras(b);
            startActivity(intent);//call SpecialInstructionsActivity
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }//call on back button press

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //call if Cart button pressed on toolbar
        if (id == R.id.action_cart) {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);//call CartActivity
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

            toppingsCheckBox = (CheckBox) rowView.findViewById(R.id.checkTopping);
            if (modifyItem != null && modifyItem.getItemToppings().contains(itemsArrayList.get(position))) {
                toppingsCheckBox.setChecked(true);
            }
            Intent intent = getIntent();
            ArrayList<String> itemAdded = new ArrayList<String>();
            itemAdded = (ArrayList<String>) intent.getSerializableExtra("itemToppings");
            if (itemAdded != null && itemAdded.contains(itemsArrayList.get(position))) {
                toppingsCheckBox.setChecked(true);
            }
            toppingsCheckBox.setTag(position);
            toppingsCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//set listener for toppings checkbox
                    CheckBox checkBox = (CheckBox) v;
                    if (checkBox.isChecked()) {//check if box is marked
                        itemToppingsToAdd.add(itemsArrayList.get(position));//add to itemToppingsToAdd
                    } else {
                        itemToppingsToAdd.remove(itemsArrayList.get(position));//remove from itemToppingsToAdd
                    }
                }
            });

            return rowView;
        }
    }

}