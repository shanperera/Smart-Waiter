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

    MenuItems selectedItem;
    ArrayList<String> sideOrders = new ArrayList<String>();
    ArrayList<String> itemToppingsToAdd = new ArrayList<String>();
    String sideOrdersToAdd = "";
    UserItems modifyItem;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_custom_side);

        Intent intent = getIntent();
        selectedItem = (MenuItems) intent.getSerializableExtra("selectedItem");
        itemToppingsToAdd = (ArrayList<String>) intent.getSerializableExtra("itemToppings");
        modifyItem = (UserItems) intent.getSerializableExtra("modifyOrder");
        Bundle b = getIntent().getExtras();
        index = b.getInt("index");

        if (modifyItem != null) {
            selectedItem = modifyItem.getMenuItem();
        }
        setTitle(selectedItem.getItemName());
        TextView itemDescriptionText = (TextView) findViewById(R.id.txtItemDes);
        itemDescriptionText.setText(selectedItem.getItemDetail());

        TextView itemPriceText = (TextView) findViewById(R.id.txtitemTopping);
        itemPriceText.setText(Utils.formatCurrency(selectedItem.getItemPrice()));

        sideOrders = selectedItem.getItemSides();
        Log.d("GET ITEM SIDES", selectedItem.getItemSides().toString());
        MyAdapter adapter = new MyAdapter(this, sideOrders);
        ListView sideOrderList = (ListView) findViewById(R.id.sideOrderList);
        sideOrderList.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (selectedItem.getItemToppings() == null) {
            finish();
        } else {
            Intent intent = new Intent(this, CustomToppingsActivity.class);
            intent.putExtra("selectedItem", selectedItem);
            intent.putExtra("itemToppings", itemToppingsToAdd);
            intent.putExtra("modifyOrder", modifyItem);
            Bundle b = new Bundle();
            b.putInt("index", index); //Your id
            intent.putExtras(b);
            sideOrdersToAdd = null;
            startActivity(intent);
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
                    intent.putExtra("selectedItem", selectedItem);
                    intent.putExtra("itemToppings", itemToppingsToAdd);
                    intent.putExtra("sideOrder", sideOrdersToAdd);
                    intent.putExtra("modifyOrder", modifyItem);
                    Bundle b = new Bundle();
                    b.putInt("index", index); //Your id
                    intent.putExtras(b);
                    startActivity(intent);
                    finish();
                    //onDisplayItemList();
                }
            });

            return rowView;
        }
    }
}
