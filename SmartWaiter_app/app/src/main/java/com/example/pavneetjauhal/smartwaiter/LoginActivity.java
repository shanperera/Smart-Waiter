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
    static User user = new User();//create new user
    Button signIn;//creat sign in button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: add check to make signup disappear
        try {
            CouchBaseLite local_database = CouchBaseLite.getInstance(this, user);
            local_database.startReplications();//start database replications
            local_database.populateUserData();//populate user information
        } catch (IOException | CouchbaseLiteException | NullPointerException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_login);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);//fullscreen
        Utils.makeStatusBarTranslucent(this, true);
    }

    //call if login button pressed
    public void login(View view) throws Exception {
        EditText getFields = (EditText) findViewById(R.id.pwEditText);//get password field
        String password = getFields.getText().toString();//get password
        if (user != null && user.getPassword() != null && user.getSalt() != null) {//check if account exists, password is entered
            if (!LocalAuth.checkPassword(password, user.getPassword(), user.getSalt())) {//check if password matches hash
                Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_LONG).show();//throw error
            } else {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);//call MainActivity
                finish();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please create user account", Toast.LENGTH_LONG).show();//throw message indicating account must be created
        }

    }

    //called when
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        signIn = (Button) findViewById(R.id.signup);//set signup button
        if (user != null && user.getPassword() != null && user.getSalt() != null){//check if user exists
            signIn.setVisibility(View.GONE);//remove sign up
        }
        else{
            signIn.setVisibility(View.VISIBLE);//add sign up button
        }

    }
    //called when sign up button is pressed
    public void signup(View view) {
            Intent intent = new Intent(this, AccountCreationActivity.class);
            startActivity(intent);//call AccountCreationActivity
    }
}
