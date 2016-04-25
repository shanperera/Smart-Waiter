package com.example.pavneetjauhal.smartwaiter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CartActivity extends AppCompatActivity {

    private ListView list;//cart list view
    private FloatingActionButton confirmOrderfab;//confirm order button
    private CategoryListAdapter adapter;//list adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_cart);
        setTitle("Cart Menu");//set title
        TextView toppingsName = (TextView) findViewById(R.id.cartEmpty);
        list = (ListView) findViewById(R.id.cartList);
        confirmOrderfab = (FloatingActionButton) findViewById(R.id.confirmOrder);

        //make buttons and list invisilbe is not items are added to cart
        if (LoginActivity.user.userItems.size() == 0){
            toppingsName.setVisibility(View.VISIBLE);
            list.setVisibility(View.INVISIBLE);
            confirmOrderfab.setVisibility(View.INVISIBLE);
        } else{//set visibility if items are added
            toppingsName.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
            confirmOrderfab.setVisibility(View.VISIBLE);
        }
        popualteCartListView();//populat cart list view
    }

    //method used to populat cart view (using list)
    public void popualteCartListView() {
        adapter = new CategoryListAdapter();
        list.setAdapter(adapter);
    }

    //called when confirm order button is pressed
    public void confirmOrder(View view) {
        if (LoginActivity.user.userItems.size() != 0) {//check if cart contains at least one item
            Intent intent = new Intent(this, ConfirmOrderActivity.class);
            startActivity(intent);
        } else {//throw error if not
            Toast.makeText(getApplicationContext(),
                    "Cart Is Empty. Please Add Items Before Checkout.", Toast.LENGTH_LONG).show();
        }
    }

    //method to update cart list on call
    @Override
    protected void onResume() {
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    //list view method
    private class CategoryListAdapter extends ArrayAdapter<UserItems> {
        public CategoryListAdapter() {
            super(CartActivity.this, R.layout.cart_view, LoginActivity.user.userItems);//set cart list view
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with, may have been given null
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.cart_view, parent, false);
            }

            //Find the menu item to work with
            UserItems currentItem = LoginActivity.user.userItems.get(position);

            //Make text view for toppings
            TextView toppingsName = (TextView) itemView.findViewById(R.id.toppingsName);

            //check if toppings selected on item
            if (currentItem.getItemToppings() == null) {
                toppingsName.setVisibility(View.GONE);//remove toppings section if no toppings
            } else {
                toppingsName.setVisibility(View.VISIBLE);//add toppings section
                String toppings = currentItem.getItemToppings().toString();//display toppings
                toppings = toppings.replace("[", "");
                toppings = toppings.replace("]", "");
                toppingsName.setText("Toppings: " + toppings);//display toppings title
            }

            //Make text view for side name
            TextView sideName = (TextView) itemView.findViewById(R.id.sideName);

            //check if item selected contains side
            if (currentItem.getSideOrder() == null) {
                sideName.setVisibility(View.GONE);//remove side order from view
            } else {
                sideName.setVisibility(View.VISIBLE);//display side order in view
                sideName.setText("Sides: " + currentItem.getSideOrder());//display side name
            }

            //Make text view for special instructions
            TextView instructionName = (TextView) itemView.findViewById(R.id.instructionsName);

            //check if special instructions set on item
            if (currentItem.getSpecialInstrucitons().equals("")) {
                instructionName.setVisibility(View.GONE);//remove special instructions in view
            } else {
                instructionName.setVisibility(View.VISIBLE);//display special instructions in view
                instructionName.setText(
                        "Special Instructions: " + currentItem.getSpecialInstrucitons());//display special instructions
            }

            //Make text view for special item name
            TextView txtItemName = (TextView) itemView.findViewById(R.id.txtItemName);
            txtItemName.setText(
                    currentItem.getItemName() + " - " + Utils.formatCurrency(currentItem.getItemPrice()));//display item name and price

            //create button for delete item
            ImageButton btn = (ImageButton) itemView.findViewById(R.id.deleteItem);
            btn.setTag(position);
            //set listener for delete button
            btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Integer index = (Integer) v.getTag();
                    LoginActivity.user.removeUserItem(index);
                    notifyDataSetChanged();
                }
            });
            //create button for customize item
            ImageButton btn2 = (ImageButton) itemView.findViewById(R.id.customizeItem);
            btn2.setTag(position);
            //set listener for customize button
            btn2.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Integer index = (Integer) v.getTag();
                    UserItems object = LoginActivity.user.userItems.get(index);
                    //if item toppings, go to toppings page
                    if (object.getItemToppings() != null) {
                        Intent intent = new Intent(CartActivity.this, CustomToppingsActivity.class);
                        intent.putExtra("modifyOrder", object);
                        Bundle b = new Bundle();
                        b.putInt("index", index); //index for item to customize
                        intent.putExtras(b);//pass arguments
                        startActivity(intent);//go to toppings activity
                    } else if (object.getSideOrder() != null) { //if side order for item, go to order page
                        Intent intent = new Intent(CartActivity.this, CustomSideActivity.class);
                        intent.putExtra("modifyOrder", object);
                        Bundle b = new Bundle();
                        b.putInt("index", index); //index for item to customize
                        intent.putExtras(b);//pass arguments
                        startActivity(intent);//go to side order activity
                    } else {//go to special instructions page
                        Intent intent =
                                new Intent(CartActivity.this, SpecialInstrunctionsActivity.class);
                        intent.putExtra("modifyOrder", object);
                        Bundle b = new Bundle();
                        b.putInt("index", index); //index for item to customize
                        intent.putExtras(b);
                        startActivity(intent);//go to special instructions activity
                    }
                }
            });

            return itemView;
        }
    }
}
