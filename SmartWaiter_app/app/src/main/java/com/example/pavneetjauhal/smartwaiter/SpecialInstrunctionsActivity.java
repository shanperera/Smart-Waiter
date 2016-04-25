package com.example.pavneetjauhal.smartwaiter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SpecialInstrunctionsActivity extends AppCompatActivity {

    ArrayList<String> itemToppingsToAdd = new ArrayList<String>();//store item toppings to add
    String sideOrder;//store side order
    MenuItems selectedItem;//store selected item info
    UserItems modifyItem;//store item to modify
    int index;//store index of item selected
    FloatingActionButton cartButton;
    EditText et;//text field for special instructions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_special_instrunctions);

        Intent intent = getIntent();
        selectedItem = (MenuItems) intent.getSerializableExtra("selectedItem");//get selected item
        itemToppingsToAdd = (ArrayList<String>) intent.getSerializableExtra("itemToppings");//get item toppings to add
        sideOrder = (String) intent.getSerializableExtra("sideOrder");//get side order to add
        modifyItem = (UserItems) intent.getSerializableExtra("modifyOrder");//get item to modify from CartActivity
        Bundle b = getIntent().getExtras();
        index = b.getInt("index");//get index of selected item
        et = (EditText) findViewById(R.id.specialInstructions);

        cartButton = (FloatingActionButton) findViewById(R.id.addCart);//set add to cart button

        if (modifyItem != null) {//check if item to modify from CartActivity
            selectedItem = modifyItem.getMenuItem();//get modify item info
            et.setText(modifyItem.getSpecialInstrucitons());//store special instructions from modifyItem
        }
        setTitle(selectedItem.getItemName());//set title to item name

        TextView itemDescriptionText = (TextView) findViewById(R.id.txtItemDes);
        itemDescriptionText.setText(selectedItem.getItemDetail());//set item description

        TextView itemPriceText = (TextView) findViewById(R.id.txtitemTopping);
        itemPriceText.setText(Utils.formatCurrency(selectedItem.getItemPrice()));//set item price

        et = (EditText) findViewById(R.id.specialInstructions);

        //listen for activity in special instrucitons text bot
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

    //called when check mark is clicked to add item to cart
    public void addToCart(View view) {
        String specialInstructions = et.getText().toString();//store special instrucitons from text field
        if (modifyItem == null) {//check new item to be added
            //create new item
            LoginActivity.user.createUserItem(selectedItem.getItemName(), selectedItem.getItemPrice(), itemToppingsToAdd, sideOrder, selectedItem, specialInstructions);
            Toast.makeText(getApplicationContext(), "Added Item to cart",
                    Toast.LENGTH_LONG).show();
        } else {//modify already added item
            LoginActivity.user.userItems.remove(index);
            LoginActivity.user.userItems.add(index, modifyItem);
            LoginActivity.user.userItems.get(index).setItemToppings(itemToppingsToAdd);//set new item toppings
            LoginActivity.user.userItems.get(index).setSideOrder(sideOrder);//set new side order
            LoginActivity.user.userItems.get(index).setSpecialInstructions(specialInstructions);//set new special instructions

        }
        Log.d("Item Name", LoginActivity.user.userItems.get(LoginActivity.user.userItems.size() - 1).getItemName());
        finish();
    }

    @Override
    public void onBackPressed() {//called when back button pressed
        if (selectedItem.getItemSides() != null) {//check if item sides avialbe for selected item
            Intent intent = new Intent(this, CustomSideActivity.class);
            intent.putExtra("selectedItem", selectedItem);//pass selected item
            intent.putExtra("itemToppings", itemToppingsToAdd);//pass item toppings to add
            intent.putExtra("modifyOrder", modifyItem);
            Bundle b = new Bundle();
            b.putInt("index", index); //Your id
            intent.putExtras(b);
            startActivity(intent);//call CustomSideActivity
            finish();
        } else if (selectedItem.getItemToppings() != null) {//check if item toppings availble for selected item
            Intent intent = new Intent(this, CustomToppingsActivity.class);
            intent.putExtra("selectedItem", selectedItem);//pass selected item
            intent.putExtra("itemToppings", itemToppingsToAdd);//pass item toppings to add
            intent.putExtra("modifyOrder", modifyItem);
            Bundle b = new Bundle();
            b.putInt("index", index); //Your id
            intent.putExtras(b);
            startActivity(intent);//call CustomToppingsActivity
            intent.putExtra("selectedItem", selectedItem);
            startActivity(intent);
            finish();
        } else {
            finish();
        }
    }

}
