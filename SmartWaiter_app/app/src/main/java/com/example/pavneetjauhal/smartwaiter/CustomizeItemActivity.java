package com.example.pavneetjauhal.smartwaiter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class CustomizeItemActivity extends AppCompatActivity {
    MenuItems selectedItem;
    MainActivity mainObject;
    Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        selectedItem = (MenuItems) intent.getSerializableExtra("selectedItem");

        Log.d("TAG", selectedItem.getItemName());

    }
    public void addToCart(View view) {
        MainActivity.user.createUserItem(selectedItem.getItemName(), selectedItem.getItemPrice());
        Toast.makeText(getApplicationContext(), "Added Item to cart",
                Toast.LENGTH_LONG).show();
        Intent intent = new Intent("com.example.pavneetjauhal.smartwaiter.DisplayItemsActivity");
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
        }

        return super.onOptionsItemSelected(item);
    }

}
