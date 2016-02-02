package com.example.pavneetjauhal.smartwaiter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SpecialInstrunctionsActivity extends AppCompatActivity {

    ArrayList<String> itemToppingsToAdd = new ArrayList<String>();
    String sideOrder;
    MenuItems selectedItem;
    UserItems modifyItem;
    int index;
    Button cartButton;
    EditText et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_instrunctions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        selectedItem = (MenuItems) intent.getSerializableExtra("selectedItem");
        itemToppingsToAdd = (ArrayList<String>) intent.getSerializableExtra("itemToppings");
        sideOrder = (String) intent.getSerializableExtra("sideOrder");
        modifyItem = (UserItems) intent.getSerializableExtra("modifyOrder");
        Bundle b = getIntent().getExtras();
        index = b.getInt("index");
        et = (EditText) findViewById(R.id.specialInstructions);;
        cartButton=(Button)findViewById(R.id.addCart);

        if (modifyItem != null){
            cartButton.setText("Modify Item");
            selectedItem = modifyItem.getMenuItem();
            et.setText(modifyItem.getSpecialInstrucitons());
        }
        else{
            cartButton.setText("Add to Cart");
        }
        //Log.d("TAG", selectedItem.getItemName());
        String itemName = selectedItem.getItemName();

        TextView itemNameText = (TextView)findViewById(R.id.txtItemName);
        itemNameText.setText(itemName);

        TextView itemDescriptionText = (TextView)findViewById(R.id.txtItemDes);
        itemDescriptionText.setText(selectedItem.getItemDetail());

        TextView itemPriceText = (TextView)findViewById(R.id.txtitemTopping);
        itemPriceText.setText(selectedItem.getItemPrice());

        et = (EditText) findViewById(R.id.specialInstructions);;
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            /**
             * This listens for the user to press the enter button on
             * the keyboard and then hides the virtual keyboard
             */
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // do something, e.g. set your TextView here via .setText()
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    return true;
                }
                return false;
            }

        });

    }

    public void addToCart(View view) {
        //et = (EditText) findViewById(R.id.specialInstructions);
        String specialInstructions = et.getText().toString();
        Toast.makeText(getBaseContext(), specialInstructions, Toast.LENGTH_LONG).show();


        if (modifyItem == null) {
            MainActivity.user.createUserItem(selectedItem.getItemName(), selectedItem.getItemPrice(), itemToppingsToAdd, sideOrder, selectedItem, specialInstructions);
            //Log.d("TAG", CustomizeItemActivity.itemToppingsToAdd.toString());
            //MainActivity.user.userItems.get(MainActivity.user.userItems.size() - 1).setItemToppings(CustomizeItemActivity.itemToppingsToAdd);
            //MainActivity.user.userItems.get(MainActivity.user.userItems.size() - 1).setSideOrder(CustomizeItemSideActivity.sideOrdersToAdd);
            Toast.makeText(getApplicationContext(), "Added Item to cart",
                    Toast.LENGTH_LONG).show();
        }
        else{
            MainActivity.user.userItems.remove(index);
            MainActivity.user.userItems.add(index, modifyItem);
            MainActivity.user.userItems.get(index).setItemToppings(itemToppingsToAdd);
            MainActivity.user.userItems.get(index).setSideOrder(sideOrder);
            MainActivity.user.userItems.get(index).setSpecialInstructions(specialInstructions);
            Toast.makeText(getApplicationContext(), "Modified Item",
                    Toast.LENGTH_LONG).show();

        }
        Intent intent = new Intent("com.example.pavneetjauhal.smartwaiter.DisplayItemsActivity");
        Log.d("Item Name", MainActivity.user.userItems.get(MainActivity.user.userItems.size() - 1).getItemName());
        //Log.d("Item Toppings", MainActivity.user.userItems.get(MainActivity.user.userItems.size() - 1).getItemToppings().toString());
        //Log.d("Item Side Order", MainActivity.user.userItems.get(MainActivity.user.userItems.size() - 1).getSideOrder());
        //CustomizeItemActivity.itemToppingsToAdd.clear();
        //CustomizeItemSideActivity.sideOrdersToAdd = null;
        startActivity(intent);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            if((selectedItem.getItemSides() == null) && (selectedItem.getItemToppings() == null)) {
                Intent intent = new Intent("com.example.pavneetjauhal.smartwaiter.DisplayItemsActivity");
                intent.putExtra("selectedItem", selectedItem);
                startActivity(intent);
            }
            else if((selectedItem.getItemSides() == null)){
                Intent intent = new Intent("com.example.pavneetjauhal.smartwaiter.CustomToppingsActivity");
                intent.putExtra("selectedItem", selectedItem);
                startActivity(intent);
            }
            else{
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
