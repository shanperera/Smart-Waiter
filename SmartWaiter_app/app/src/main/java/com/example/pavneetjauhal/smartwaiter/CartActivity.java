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

    private ListView list;
    private FloatingActionButton confirmOrderfab;
    private CategoryListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_cart);
        setTitle("Cart Menu");
        TextView toppingsName = (TextView) findViewById(R.id.cartEmpty);
        list = (ListView) findViewById(R.id.cartList);
        confirmOrderfab = (FloatingActionButton) findViewById(R.id.confirmOrder);
        if (LoginActivity.user.userItems.size() == 0){
            toppingsName.setVisibility(View.VISIBLE);
            list.setVisibility(View.INVISIBLE);
            confirmOrderfab.setVisibility(View.INVISIBLE);
        } else{
            toppingsName.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
            confirmOrderfab.setVisibility(View.VISIBLE);
        }
        popualteCartListView();
    }

    public void popualteCartListView() {
        adapter = new CategoryListAdapter();
        list.setAdapter(adapter);
    }

    public void confirmOrder(View view) {
        if (LoginActivity.user.userItems.size() != 0) {
            Intent intent = new Intent(this, ConfirmOrderActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Cart Is Empty. Please Add Items Before Checkout.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    private class CategoryListAdapter extends ArrayAdapter<UserItems> {
        public CategoryListAdapter() {
            super(CartActivity.this, R.layout.cart_view, LoginActivity.user.userItems);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with, may have been given null
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.cart_view, parent, false);
            }

            //Find the menu item to work with
            UserItems currentItem = LoginActivity.user.userItems.get(position);

            TextView toppingsName = (TextView) itemView.findViewById(R.id.toppingsName);

            if (currentItem.getItemToppings() == null) {
                toppingsName.setVisibility(View.GONE);
            } else {
                toppingsName.setVisibility(View.VISIBLE);
                String toppings = currentItem.getItemToppings().toString();
                toppings = toppings.replace("[", "");
                toppings = toppings.replace("]", "");
                toppingsName.setText("Toppings: " + toppings);
            }

            //Make text view
            TextView sideName = (TextView) itemView.findViewById(R.id.sideName);

            if (currentItem.getSideOrder() == null) {
                sideName.setVisibility(View.GONE);
            } else {
                sideName.setVisibility(View.VISIBLE);
                sideName.setText("Sides: " + currentItem.getSideOrder());
            }

            TextView instructionName = (TextView) itemView.findViewById(R.id.instructionsName);

            if (currentItem.getSpecialInstrucitons().equals("")) {
                instructionName.setVisibility(View.GONE);
            } else {
                instructionName.setVisibility(View.VISIBLE);
                instructionName.setText(
                        "Special Instructions: " + currentItem.getSpecialInstrucitons());
            }

            TextView txtItemName = (TextView) itemView.findViewById(R.id.txtItemName);
            txtItemName.setText(
                    currentItem.getItemName() + " - " + Utils.formatCurrency(currentItem.getItemPrice()));

            ImageButton btn = (ImageButton) itemView.findViewById(R.id.deleteItem);
            btn.setTag(position);
            btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Integer index = (Integer) v.getTag();
                    LoginActivity.user.removeUserItem(index);
                    notifyDataSetChanged();
                }
            });

            ImageButton btn2 = (ImageButton) itemView.findViewById(R.id.customizeItem);
            btn2.setTag(position);
            btn2.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Integer index = (Integer) v.getTag();
                    UserItems object = LoginActivity.user.userItems.get(index);
                    if (object.getItemToppings() != null) {
                        Intent intent = new Intent(CartActivity.this, CustomToppingsActivity.class);
                        intent.putExtra("modifyOrder", object);
                        Bundle b = new Bundle();
                        b.putInt("index", index); //Your id
                        intent.putExtras(b);
                        startActivity(intent);
                    } else if (object.getSideOrder() != null) {
                        Intent intent = new Intent(CartActivity.this, CustomSideActivity.class);
                        intent.putExtra("modifyOrder", object);
                        Bundle b = new Bundle();
                        b.putInt("index", index); //Your id
                        intent.putExtras(b);
                        startActivity(intent);
                    } else {
                        Intent intent =
                                new Intent(CartActivity.this, SpecialInstrunctionsActivity.class);
                        intent.putExtra("modifyOrder", object);
                        Bundle b = new Bundle();
                        b.putInt("index", index); //Your id
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                }
            });

            return itemView;
        }
    }
}
