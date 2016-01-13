package com.example.pavneetjauhal.smartwaiter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CreateAccountActivity extends AppCompatActivity {

public User newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

    }

    public void createAccount(View button){
        EditText getFields = (EditText) findViewById(R.id.usernameEditText);
        String username = getFields.getText().toString();

        getFields = (EditText) findViewById(R.id.passwordEditText);
        String password = getFields.getText().toString();

        getFields = (EditText) findViewById(R.id.firstNameEditText);
        String firstName = getFields.getText().toString();

        getFields = (EditText) findViewById(R.id.lastNameEditText);
        String lastName = getFields.getText().toString();

        getFields = (EditText) findViewById(R.id.addressEditText);
        String address = getFields.getText().toString();

        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setBillingAddress(address);
        newUser.setPostalCode("L8S 4L8");
        newUser.setPhoneNumber("9055259140");
    }
}
