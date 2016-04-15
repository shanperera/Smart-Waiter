package com.example.pavneetjauhal.smartwaiter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class DisplayCategoriesActivity extends AppCompatActivity {

    MainActivity mainObject;
    Toolbar mActionBarToolbar;

    private String[] SettingsMenu;
    public DrawerLayout mDrawerLayout;
    public ListView mDrawerList;
    public ActionBarDrawerToggle mDrawerToggle;
    public CharSequence mTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_categories);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCategories);
        //setSupportActionBar(toolbar);
        sideMenuSetup();
        //mActionBarToolbar = (Toolbar) findViewById(R.id.toolbarCategories);
        //setSupportActionBar(mActionBarToolbar);
        //getSupportActionBar().setTitle(mainObject.restarauntName + " Menu Categories");
        onDisplayCategoryList();


    }

    public void sideMenuSetup(){
        //ActionBar setup

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(mainObject.restarauntName + " Menu Categories");
        //this.setTitle(getResources().getString(R.string.title_activity_scan));
        //Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        // setSupportActionBar(myToolbar);

        SettingsMenu = getResources().getStringArray(R.array.menu_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, SettingsMenu));
        // Set the list's click listener
        //mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mTitle = "test";

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, SettingsMenu));
        // Set the list's click listener
        //mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                //getActionBar().setTitle(mTitle);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                //getActionBar().setTitle(mTitle);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);

        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header, mDrawerList,
                false);
        ViewGroup footer = (ViewGroup) inflater.inflate(R.layout.footer, mDrawerList,
                false);
        mDrawerList.addHeaderView(header, null, false);
        mDrawerList.addFooterView(footer, null, false);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.user.userItems.clear();
                        Intent intent = new Intent(DisplayCategoriesActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position) {
        Toast.makeText(this, R.string.app_name, Toast.LENGTH_SHORT).show();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(SettingsMenu[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }
}
