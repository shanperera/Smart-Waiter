package com.example.pavneetjauhal.smartwaiter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by pavneetjauhal on 16-02-02.
 */
public class AppAuthentication extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void createAccount(View button){
        EditText getFields = (EditText) findViewById(R.id.passwordEditText);
        /*String password = getFields.getText().toString();
        try {
            LocalAuth.checkPassword(password, MainActivity.user.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("activity_login", "## Crashed in password check");
        }*/
    }
}
