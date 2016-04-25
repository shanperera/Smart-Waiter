package com.example.pavneetjauhal.smartwaiter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DisplayCategoriesActivity extends AppCompatActivity {

    MainActivity mainObject;//object to refer to MainActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view);
        this.setTitle(mainObject.restarauntName);//set restarauntName
        onDisplayCategoryList();//display category names

    }

    public void onDisplayCategoryList() {
        popualteCategoriesListView();

        ListView list = (ListView) findViewById(R.id.menuList);
        //set listener for category name click
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                MenuCategories list = mainObject.menuCategoryList.get(position);//get category items
                mainObject.currentCategory = mainObject.menuCategoryList.get(position).getCategory();
                mainObject.menuItemList = list.categoryItems;//get items in category
                Intent intent = new Intent(DisplayCategoriesActivity.this, DisplayItemsActivity.class);
                startActivity(intent);//call DisplayItemsActivity
            }
        });
    }

    //populate list with Category names
    public void popualteCategoriesListView() {
        CategoryListAdapter adapter = new CategoryListAdapter();
        ListView list = (ListView) findViewById(R.id.menuList);
        list.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //check if cart button pressed
        if (id == R.id.action_cart) {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);//call CartActivity
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {//check if back button pressed
        new AlertDialog.Builder(this)//throw alert box indicating quitting action
                .setMessage("Are you sure you want to exit?")//confirmation message
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {//display yes button
                        finish();
                    }
                }).setNegativeButton("No", null).show();//display no button
    }

    private class CategoryListAdapter extends ArrayAdapter<MenuCategories> {
        public CategoryListAdapter() {
            super(DisplayCategoriesActivity.this, R.layout.category_view, mainObject.menuCategoryList);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            // Make sure we have a view to work with, may have been given null
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.category_view, parent, false);
            }

            //Find the menu item to work with
            MenuCategories currentItem = mainObject.menuCategoryList.get(position);

            //Make text view
            TextView makeText = (TextView) itemView.findViewById(R.id.txtCategory);
            ImageView imgCategory = (ImageView) itemView.findViewById(R.id.imgCategory);
            makeText.setText(currentItem.getCategory());//display category name
            //Log.d("categories", currentItem.getCategory());
            imgCategory.setImageResource(Utils.categoryImage.get(currentItem.getCategory().toLowerCase()));//display category image
            return itemView;
        }
    }
}
