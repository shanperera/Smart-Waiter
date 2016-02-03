package com.example.pavneetjauhal.smartwaiter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        popualteCartListView();
    }

    public void popualteCartListView(){
        ArrayAdapter<UserItems> adapter = new CategoryListAdapter();
        ListView list = (ListView) findViewById(R.id.cartList);
        list.setAdapter(adapter);
    }

    public void confirmOrder(View view) {
        if(MainActivity.user.userItems.size() != 0){
        Intent intent = new Intent("com.example.pavneetjauhal.smartwaiter.ConfirmOrderActivity");
        startActivity(intent);
        }
        else{
            Toast.makeText(getApplicationContext(), "Cart Is Empty. Please Add Items Before Checkout.",
                    Toast.LENGTH_LONG).show();
        }
    }


    private class CategoryListAdapter extends ArrayAdapter<UserItems> {
        public CategoryListAdapter() {
            super(CartActivity.this, R.layout.cart_view, MainActivity.user.userItems);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            // Make sure we have a view to work with, may have been given null
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.cart_view, parent, false);
            }

            //Find the menu item to work with
            UserItems currentItem = MainActivity.user.userItems.get(position);

            //Fill the view
            //ImageView imageView = (ImageView) itemView.findViewById(R.id.itemIcon);
            //imageView.setImageResource(currentItem.getId());

            TextView txtItemTopping = (TextView) itemView.findViewById(R.id.txtitemTopping);
            TextView toppingsName = (TextView) itemView.findViewById(R.id.toppingsName);

            if(currentItem.getItemToppings() == null){
                txtItemTopping.setVisibility(View.GONE);
                toppingsName.setVisibility(View.GONE);
            }
            else{
                txtItemTopping.setVisibility(View.VISIBLE);
                toppingsName.setVisibility(View.VISIBLE);
                String toppings = currentItem.getItemToppings().toString();
                toppings = toppings.replace(", ", "\n");
                toppings = toppings.replace("[","");
                toppings = toppings.replace("]","");
                txtItemTopping.setText(toppings);
            }

            //Make text view
            TextView sideName = (TextView) itemView.findViewById(R.id.sideName);
            TextView txtItemSide = (TextView) itemView.findViewById(R.id.txtItemSide);

            if(currentItem.getSideOrder() == null){
                sideName.setVisibility(View.GONE);
                txtItemSide.setVisibility(View.GONE);
            }
            else{
                sideName.setVisibility(View.VISIBLE);
                txtItemSide.setVisibility(View.VISIBLE);
                txtItemSide.setText(currentItem.getSideOrder());
            }

            TextView instructionName = (TextView) itemView.findViewById(R.id.instructionsName);
            TextView txtSpecialInstructions = (TextView) itemView.findViewById(R.id.txtSpecialInstructions);

            if (currentItem.getSpecialInstrucitons().equals("")) {
                instructionName.setVisibility(View.GONE);
                txtSpecialInstructions.setVisibility(View.GONE);
            }
            else{
                instructionName.setVisibility(View.VISIBLE);
                txtSpecialInstructions.setVisibility(View.VISIBLE);
                txtSpecialInstructions.setText(currentItem.getSpecialInstrucitons());
            }

            TextView txtItemName = (TextView) itemView.findViewById(R.id.txtItemName);
            txtItemName.setText(currentItem.getItemName());

            TextView txtItemPrice = (TextView) itemView.findViewById(R.id.txtItemPrice);
            txtItemPrice.setText("$" + currentItem.getItemPrice());

            Button btn = (Button)itemView.findViewById(R.id.deleteItem);
            btn.setTag(position);
            btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Integer index = (Integer) v.getTag();
                    MainActivity.user.removeUserItem(index);
                    notifyDataSetChanged();
                }
            });

            Button btn2 = (Button)itemView.findViewById(R.id.customizeItem);
            btn2.setTag(position);
            btn2.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Integer index = (Integer) v.getTag();
                    UserItems object = MainActivity.user.userItems.get(index);
                    Intent intent = new Intent("com.example.pavneetjauhal.smartwaiter.CustomToppingsActivity");
                    intent.putExtra("modifyOrder", object);
                    Bundle b = new Bundle();
                    b.putInt("index", index); //Your id
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });

            return  itemView;
            //return super.getView(position, convertView, parent);
        }

    }
}
