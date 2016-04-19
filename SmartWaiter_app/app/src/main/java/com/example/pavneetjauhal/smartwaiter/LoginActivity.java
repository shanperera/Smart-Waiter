package com.example.pavneetjauhal.smartwaiter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.couchbase.lite.CouchbaseLiteException;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    static User user = new User();
    Button signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: add check to make signup disappear
        try {
            CouchBaseLite local_database = CouchBaseLite.getInstance(this, user);
            local_database.startReplications();
            local_database.populateUserData();
        } catch (IOException | CouchbaseLiteException | NullPointerException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_login);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        Utils.makeStatusBarTranslucent(this, true);
    }

    public void login(View view) throws Exception {
        EditText getFields = (EditText) findViewById(R.id.pwEditText);
        String password = getFields.getText().toString();
        if (user != null && user.getPassword() != null && user.getSalt() != null) {
            if (!LocalAuth.checkPassword(password, user.getPassword(), user.getSalt())) {
                Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please create user account", Toast.LENGTH_LONG).show();
        }

    }

    public void onResume() {
        super.onResume();  // Always call the superclass method first
        signIn = (Button) findViewById(R.id.signup);
        if (user != null && user.getPassword() != null && user.getSalt() != null){
            signIn.setVisibility(View.GONE);
        }
        else{
            signIn.setVisibility(View.VISIBLE);
        }

    }

    public void signup(View view) {
            Intent intent = new Intent(this, AccountCreationActivity.class);
            startActivity(intent);
    }
}
