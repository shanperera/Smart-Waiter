package com.example.pavneetjauhal.smartwaiter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AccountCreationActivity extends AppCompatActivity {

    User newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Create Account");
        setContentView(R.layout.activity_account_creation);
    }

    public void createAccount(View button) throws Exception {
        EditText getFields = (EditText) findViewById(R.id.passwordEditText);
        String password = getFields.getText().toString();
        if (password == null || password.length() < 5) {
            Toast.makeText(getApplicationContext(), "Password Too Short", Toast.LENGTH_LONG).show();
            return;
        }

        getFields = (EditText) findViewById(R.id.firstNameEditText);
        String firstName = getFields.getText().toString();
        if (firstName == null || firstName.isEmpty()) {
            Toast.makeText(getApplicationContext(), "First Name Invalid", Toast.LENGTH_LONG).show();
            return;
        }

        getFields = (EditText) findViewById(R.id.lastNameEditText);
        String lastName = getFields.getText().toString();
        if (lastName == null || lastName.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Last Name Invalid", Toast.LENGTH_LONG).show();
            return;
        }

        getFields = (EditText) findViewById(R.id.addressEditText);
        String address = getFields.getText().toString();

        getFields = (EditText) findViewById(R.id.postalCodeEditText);
        String postCode = getFields.getText().toString();

        getFields = (EditText) findViewById(R.id.phoneNumberEditText);
        String phoneNum = getFields.getText().toString();
        /* Store Password */
        LocalAuth auth = LocalAuth.computeSaltedHash(password);
        newUser = new User(auth.salt, auth.password, firstName, lastName, address, postCode, phoneNum);
        LoginActivity.user = newUser;
        CouchBaseLite local_database = CouchBaseLite.getInstance(this, newUser);
        local_database.storeUserData(newUser);
        finish();
    }
}
