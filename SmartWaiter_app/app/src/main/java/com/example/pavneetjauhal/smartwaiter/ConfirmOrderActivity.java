package com.example.pavneetjauhal.smartwaiter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ConfirmOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_confirm_order);//set view
        setTitle("Confirm Order");//set title
        popualteCartListView();//populate cart items to order
        displayTotal();//display grand total
    }

    //display items in cart
    public void popualteCartListView() {
        ArrayAdapter<UserItems> adapter = new ConfirmOrderAdapter();
        ListView list = (ListView) findViewById(R.id.confirmOrderList);
        list.setAdapter(adapter);
    }

    //display grand total
    public void displayTotal() {
        TextView t = new TextView(this);
        t = (TextView) findViewById(R.id.totalPrice);
        t.setText(Utils.formatCurrency(LoginActivity.user.getTotalPrice()));//display total price
    }

    //button for confirm payment
    public void proceedPayment(View view) throws Exception {
        Intent intent = new Intent(this, GetPaymentInformationActivity.class);
        startActivity(intent);//go to GetPaymentInformationActivity
    }
    //populate confirm order list with cart items
    private class ConfirmOrderAdapter extends ArrayAdapter<UserItems> {
        public ConfirmOrderAdapter() {
            super(ConfirmOrderActivity.this, R.layout.confirm_order_view,
                    LoginActivity.user.userItems);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with, may have been given null
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.confirm_order_view, parent, false);
            }

            //Find the menu item to work with
            UserItems currentItem = LoginActivity.user.userItems.get(position);
            //create text view for item name
            TextView makeText = (TextView) itemView.findViewById(R.id.txtItemName);
            makeText.setText(currentItem.getItemName());//display item name

            //create text view for item price
            TextView price = (TextView) itemView.findViewById(R.id.txtitemTopping);
            price.setText(Utils.formatCurrency(currentItem.getItemPrice()));//display item price

            return itemView;
        }
    }
}
