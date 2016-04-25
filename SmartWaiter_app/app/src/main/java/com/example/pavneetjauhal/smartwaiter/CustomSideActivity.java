package com.example.pavneetjauhal.smartwaiter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomSideActivity extends AppCompatActivity {

    MenuItems selectedItem;//current selected item
    ArrayList<String> sideOrders = new ArrayList<String>();//side orders availble for item
    ArrayList<String> itemToppingsToAdd = new ArrayList<String>();//toppings availble for item
    String sideOrdersToAdd = "";//name of side order to add
    UserItems modifyItem;//set when called from CartActivity
    int index;//index for current item

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_custom_side);

        Intent intent = getIntent();
        selectedItem = (MenuItems) intent.getSerializableExtra("selectedItem");//get selected item
        itemToppingsToAdd = (ArrayList<String>) intent.getSerializableExtra("itemToppings");//get item toppings
        modifyItem = (UserItems) intent.getSerializableExtra("modifyOrder");//get item to modify
        Bundle b = getIntent().getExtras();//get arguments
        index = b.getInt("index");//get index of item to modify

        if (modifyItem != null) {//check if item to modify from CartActivity
            selectedItem = modifyItem.getMenuItem();//get item info
        }
        setTitle(selectedItem.getItemName());//set item name
        TextView itemDescriptionText = (TextView) findViewById(R.id.txtItemDes);
        itemDescriptionText.setText(selectedItem.getItemDetail());//set item description

        TextView itemPriceText = (TextView) findViewById(R.id.txtitemTopping);
        itemPriceText.setText(Utils.formatCurrency(selectedItem.getItemPrice()));//set item price

        sideOrders = selectedItem.getItemSides();//get item sides
        //Log.d("GET ITEM SIDES", selectedItem.getItemSides().toString());
        MyAdapter adapter = new MyAdapter(this, sideOrders);//create adapter for side order list
        ListView sideOrderList = (ListView) findViewById(R.id.sideOrderList);
        sideOrderList.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//called on cart button press
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {//check if cart button press
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);//call CartActivity
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {//call on back button press
        if (selectedItem.getItemToppings() == null) {//check if item toppings are not availble for item
            finish();//call DisplayItemActivity
        } else {
            Intent intent = new Intent(this, CustomToppingsActivity.class);
            intent.putExtra("selectedItem", selectedItem);//pass selected item
            intent.putExtra("itemToppings", itemToppingsToAdd);//pass item toppings avaible to add
            intent.putExtra("modifyOrder", modifyItem);//pass item being modified
            Bundle b = new Bundle();
            b.putInt("index", index); //pass index of current item selected
            intent.putExtras(b);
            sideOrdersToAdd = null;
            startActivity(intent);//call CustomToppingsActivity
            finish();
        }
    }

    public class MyAdapter extends ArrayAdapter<String> {

        private final Context context;
        private final List<String> itemsArrayList;

        public MyAdapter(CustomSideActivity context, List<String> itemsArrayList) {

            super(context, R.layout.sideorder_view, itemsArrayList);

            this.context = context;
            this.itemsArrayList = itemsArrayList;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            // 1. Create inflater
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // 2. Get rowView from inflater
            View rowView = inflater.inflate(R.layout.sideorder_view, parent, false);

            // 3. Get the two text view from the rowView
            TextView labelView = (TextView) rowView.findViewById(R.id.txtSideName);

            // 4. Set the text for textView
            labelView.setText(itemsArrayList.get(position));
            ListView list = (ListView) findViewById(R.id.sideOrderList);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                    sideOrdersToAdd = itemsArrayList.get(position);
                    Intent intent = new Intent(CustomSideActivity.this, SpecialInstrunctionsActivity.class);
                    intent.putExtra("selectedItem", selectedItem);//pass selected item
                    intent.putExtra("itemToppings", itemToppingsToAdd);//pass item toppings avaible to add
                    intent.putExtra("sideOrder", sideOrdersToAdd);//pass side order to add
                    intent.putExtra("modifyOrder", modifyItem);//pass item being modified
                    Bundle b = new Bundle();
                    b.putInt("index", index); //Your id
                    intent.putExtras(b);
                    startActivity(intent);//call SpecialInstructions Activity
                    finish();
                    //onDisplayItemList();
                }
            });

            return rowView;
        }
    }
}
