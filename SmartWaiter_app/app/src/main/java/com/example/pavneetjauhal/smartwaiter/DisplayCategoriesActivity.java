package com.example.pavneetjauhal.smartwaiter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class DisplayCategoriesActivity extends AppCompatActivity {

    MainActivity mainObject;
    Toolbar mActionBarToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_categories);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCategories);
        setSupportActionBar(toolbar);
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbarCategories);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("Menu Categories");
        onDisplayCategoryList();


    }

    public void onDisplayCategoryList(){
        //setContentView(R.layout.categorylist);
        popualteCategoriesListView();

        ListView list = (ListView) findViewById(R.id.menuList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {
                MenuCategories list = mainObject.menuCategoryList.get(position);
                String switchClass = list.getCategory();
                Intent appInfo;
                mainObject.menuItemList = list.categoryItems;
                Intent intent = new Intent("com.example.pavneetjauhal.smartwaiter.DisplayItemsActivity");
                startActivity(intent);
                //onDisplayItemList();
            }
        });
    }

    public void popualteCategoriesListView(){
        ArrayAdapter<MenuCategories> adapter = new CategoryListAdapter();
        ListView list = (ListView) findViewById(R.id.menuList);
        list.setAdapter(adapter);
    }


    private class CategoryListAdapter extends ArrayAdapter<MenuCategories> {
        public CategoryListAdapter() {
            super(DisplayCategoriesActivity.this, R.layout.category_view, mainObject.menuCategoryList);
        }

        public View getView(int position, View convertView, ViewGroup parent){
            // Make sure we have a view to work with, may have been given null
            View itemView = convertView;
            if(itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.category_view, parent, false);
            }

            //Find the menu item to work with
            MenuCategories currentItem = mainObject.menuCategoryList.get(position);

            //Fill the view
            //ImageView imageView = (ImageView) itemView.findViewById(R.id.itemIcon);
            //imageView.setImageResource(currentItem.getId());

            //Make text view
            TextView makeText = (TextView) itemView.findViewById(R.id.txtCategory);
            makeText.setText(currentItem.getCategory());

            return  itemView;
            //return super.getView(position, convertView, parent);
        }

    }

}
