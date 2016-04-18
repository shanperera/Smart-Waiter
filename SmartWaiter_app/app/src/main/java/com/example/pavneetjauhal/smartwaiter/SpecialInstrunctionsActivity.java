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

    ArrayList<String> itemToppingsToAdd = new ArrayList<String>();
    String sideOrder;
    MenuItems selectedItem;
    UserItems modifyItem;
    int index;
    FloatingActionButton cartButton;
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_special_instrunctions);

        Intent intent = getIntent();
        selectedItem = (MenuItems) intent.getSerializableExtra("selectedItem");
        itemToppingsToAdd = (ArrayList<String>) intent.getSerializableExtra("itemToppings");
        sideOrder = (String) intent.getSerializableExtra("sideOrder");
        modifyItem = (UserItems) intent.getSerializableExtra("modifyOrder");
        Bundle b = getIntent().getExtras();
        index = b.getInt("index");
        et = (EditText) findViewById(R.id.specialInstructions);

        cartButton = (FloatingActionButton) findViewById(R.id.addCart);

        if (modifyItem != null) {
            selectedItem = modifyItem.getMenuItem();
            et.setText(modifyItem.getSpecialInstrucitons());
        }
        setTitle(selectedItem.getItemName());

        TextView itemDescriptionText = (TextView) findViewById(R.id.txtItemDes);
        itemDescriptionText.setText(selectedItem.getItemDetail());

        TextView itemPriceText = (TextView) findViewById(R.id.txtitemTopping);
        itemPriceText.setText(Utils.formatCurrency(selectedItem.getItemPrice()));

        et = (EditText) findViewById(R.id.specialInstructions);

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
        String specialInstructions = et.getText().toString();
        if (modifyItem == null) {
            LoginActivity.user.createUserItem(selectedItem.getItemName(), selectedItem.getItemPrice(), itemToppingsToAdd, sideOrder, selectedItem, specialInstructions);
            Toast.makeText(getApplicationContext(), "Added Item to cart",
                    Toast.LENGTH_LONG).show();
        } else {
            LoginActivity.user.userItems.remove(index);
            LoginActivity.user.userItems.add(index, modifyItem);
            LoginActivity.user.userItems.get(index).setItemToppings(itemToppingsToAdd);
            LoginActivity.user.userItems.get(index).setSideOrder(sideOrder);
            LoginActivity.user.userItems.get(index).setSpecialInstructions(specialInstructions);

        }
        Log.d("Item Name", LoginActivity.user.userItems.get(LoginActivity.user.userItems.size() - 1).getItemName());
        finish();
    }

    @Override
    public void onBackPressed() {
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
        } else if (selectedItem.getItemToppings() != null) {
            Intent intent = new Intent(this, CustomToppingsActivity.class);
            intent.putExtra("selectedItem", selectedItem);
            intent.putExtra("itemToppings", itemToppingsToAdd);
            intent.putExtra("modifyOrder", modifyItem);
            Bundle b = new Bundle();
            b.putInt("index", index); //Your id
            intent.putExtras(b);
            startActivity(intent);
            intent.putExtra("selectedItem", selectedItem);
            startActivity(intent);
            finish();
        } else {
            finish();
        }
    }

}
