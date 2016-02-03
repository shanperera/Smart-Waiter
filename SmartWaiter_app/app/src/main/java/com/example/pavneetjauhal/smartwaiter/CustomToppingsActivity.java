package com.example.pavneetjauhal.smartwaiter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CustomToppingsActivity extends AppCompatActivity {

    static MenuItems selectedItem;
    UserItems modifyItem;
    Toolbar mActionBarToolbar;
    CheckBox chBox1;
    //List<String> itemToppings = new ArrayList<String>();
    ArrayList<String> itemToppings = new ArrayList<String>();
    ArrayList<String> itemToppingsToAdd = new ArrayList<String>();
    RadioButton orderChoiceButton;
    Button btnDisplay;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_toppings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        selectedItem = (MenuItems) intent.getSerializableExtra("selectedItem");
        modifyItem = (UserItems) intent.getSerializableExtra("modifyOrder");
        Bundle b = getIntent().getExtras();
        index = b.getInt("index");

        if (modifyItem != null){
            selectedItem = modifyItem.getMenuItem();
            itemToppingsToAdd = (ArrayList<String>) modifyItem.getItemToppings();
        }



        TextView itemNameText = (TextView)findViewById(R.id.txtItemName);
        itemNameText.setText(selectedItem.getItemName());


        TextView itemDescriptionText = (TextView)findViewById(R.id.txtItemDes);
        itemDescriptionText.setText(selectedItem.getItemDetail());

        TextView itemPriceText = (TextView)findViewById(R.id.txtitemTopping);
        itemPriceText.setText(selectedItem.getItemPrice());

        //create user
        btnDisplay=(Button)findViewById(R.id.nextButton);

        //Log.d("TOPPINGS TO ADD", itemToppingsToAdd.toString());

        /*
        itemToppings.add("Lettuce");
        itemToppings.add("Tomato");
        itemToppings.add("Onion");
        itemToppings.add("Green Peppers");
        selectedItem.setItemToppings(itemToppings);
        */

        //Log.d("SELECTEDITEMSSSSSSSSSSSSSSSS", selectedItem.getItemToppings().toString());

        if (selectedItem.getItemToppings() == null){
            intent = new Intent("com.example.pavneetjauhal.smartwaiter.CustomSideActivity");
            intent.putExtra("selectedItem", selectedItem);
            intent.putExtra("modifyOrder", modifyItem);
            startActivity(intent);
        }
        else {
            itemToppings = selectedItem.getItemToppings();
            MyAdapter adapter = new MyAdapter(this, itemToppings);
            ListView toppingsListView = (ListView) findViewById(R.id.toppingsList);
            toppingsListView.setAdapter(adapter);
            Log.d("TAG", selectedItem.getItemName());
            //onDisplayItemToppings();
        }

        /*
        itemToppings.add("Lettuce");
        itemToppings.add("Tomato");
        itemToppings.add("Onion");
        itemToppings.add("Green Peppers");

        if (itemToppings.size() == 0){
            intent = new Intent("com.example.pavneetjauhal.smartwaiter.CustomizeItemSideActivity");
            intent.putExtra("selectedItem", selectedItem);
            startActivity(intent);
        }
        else {
            MyAdapter adapter = new MyAdapter(this, itemToppings);
            ListView toppingsListView = (ListView) findViewById(R.id.toppingsList);
            toppingsListView.setAdapter(adapter);
            Log.d("TAG", selectedItem.getItemName());
            //onDisplayItemToppings();
        }
        */

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
            //TextView labelView2 = (TextView) rowView.findViewById(R.id.txtToppingPrice);

            // 4. Set the text for textView
            labelView.setText(itemsArrayList.get(position));
            //labelView2.setText((itemsArrayList.get(position)));


            chBox1 = (CheckBox)rowView.findViewById(R.id.checkTopping);
            if(modifyItem != null && modifyItem.getItemToppings().contains(itemsArrayList.get(position))){
                chBox1.setChecked(true);
                //itemToppingsToAdd.add(itemsArrayList.get(position));
            }
            chBox1.setTag(position);
            chBox1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox checkBox = (CheckBox)v;
                    if(checkBox.isChecked()){
                        itemToppingsToAdd.add(itemsArrayList.get(position));
                    }
                    else{
                        itemToppingsToAdd.remove(itemsArrayList.get(position));
                    }
                }
            });

            // 5. retrn rowView
            return rowView;
        }
    }


    public void next(View view) {
        Log.d("TAG",itemToppingsToAdd.toString());
        //MainActivity.user.createUserItem(selectedItem.getItemName(), selectedItem.getItemPrice());
        //MainActivity.user.userItems.get(MainActivity.user.userItems.size() - 1).setItemToppings(itemToppingsToAdd);

        //Toast.makeText(getApplicationContext(), "Added Item to cart",
        //Toast.LENGTH_LONG).show();
        //itemToppingsToAdd = (ArrayList<String>) itemToppings;
        Intent intent = new Intent("com.example.pavneetjauhal.smartwaiter.CustomSideActivity");
        intent.putExtra("selectedItem", selectedItem);
        intent.putExtra("itemToppings", itemToppingsToAdd);
        intent.putExtra("modifyOrder", modifyItem);
        Bundle b = new Bundle();
        b.putInt("index", index); //Your id
        intent.putExtras(b);
        startActivity(intent);
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
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            //MainActivity.user.userItems.remove(MainActivity.user.userItems.size() - 1);
            Intent intent = new Intent("com.example.pavneetjauhal.smartwaiter.DisplayItemsActivity");
            itemToppingsToAdd.clear();
            startActivity(intent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}