package com.example.pavneetjauhal.smartwaiter;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.WindowManager;

import java.util.HashMap;

public class Utils {
    public static HashMap<String, Integer> categoryImage;
    //set images for menu
    static {
        categoryImage = new HashMap<>();
        categoryImage.put("appetizers", R.drawable.appetizers);
        categoryImage.put("main course", R.drawable.main_course);
        categoryImage.put("alcoholic drinks", R.drawable.alcohol);
        categoryImage.put("non alcoholic drinks", R.drawable.drinks);
        categoryImage.put("dessert", R.drawable.dessert);
        categoryImage.put("espresso", R.drawable.espresso);
        categoryImage.put("brewed coffee", R.drawable.coffee);
        categoryImage.put("teavana brewed teas", R.drawable.tevana);
        categoryImage.put("smoothies", R.drawable.smothie);
        categoryImage.put("frappuccinos", R.drawable.fraps);
        categoryImage.put("food", R.drawable.main_course_1);

    }

    public static void makeStatusBarTranslucent(Activity activity, boolean makeTranslucent) {
        if (makeTranslucent) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public static String formatCurrency(String value) {
        return "$" + value;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
