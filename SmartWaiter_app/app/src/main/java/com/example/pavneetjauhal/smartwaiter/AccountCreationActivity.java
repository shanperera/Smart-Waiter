package com.example.pavneetjauhal.smartwaiter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AccountCreationActivity extends AppCompatActivity {

    User newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_creation);
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

        getFields = (EditText) findViewById(R.id.postalCodeEditText);
        String postCode = getFields.getText().toString();

        getFields = (EditText) findViewById(R.id.phoneNumberEditText);
        String phoneNum = getFields.getText().toString();

        newUser = new User(username, password, firstName, lastName, address, postCode, phoneNum);
    }
}
