package com.example.pavneetjauhal.smartwaiter;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AccountCreationActivity extends AppCompatActivity {

    User newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Create Account");
        setContentView(R.layout.activity_account_creation);
        setTypeFace();
    }

    public void setTypeFace() {
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/bebas.otf");
        TextView curr = (TextView) findViewById(R.id.passwordLabel);
        curr.setTypeface(tf);

        curr = (TextView) findViewById(R.id.personalDetailsLabel);
        curr.setTypeface(tf);

        curr = (TextView) findViewById(R.id.firstNameLabel);
        curr.setTypeface(tf);

        curr = (TextView) findViewById(R.id.lastNameLabel);
        curr.setTypeface(tf);

        curr = (TextView) findViewById(R.id.addressLabel);
        curr.setTypeface(tf);

        curr = (TextView) findViewById(R.id.postalCodeLabel);
        curr.setTypeface(tf);

        curr = (TextView) findViewById(R.id.phoneNumberLabel);
        curr.setTypeface(tf);
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
        if (address == null || address.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Address Invalid", Toast.LENGTH_LONG).show();
            return;
        }

        getFields = (EditText) findViewById(R.id.postalCodeEditText);
        String postCode = getFields.getText().toString();
        if (postCode == null || postCode.length() != 6) {
            Toast.makeText(getApplicationContext(), "Postal Code Invalid", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        getFields = (EditText) findViewById(R.id.phoneNumberEditText);
        String phoneNum = getFields.getText().toString();
        if (phoneNum == null || phoneNum.length() != 10) {
            Toast.makeText(getApplicationContext(), "Phone Number Invalid", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        /* Store Password */
        LocalAuth auth = LocalAuth.computeSaltedHash(password);
        newUser =
                new User(auth.salt, auth.password, firstName, lastName, address, postCode, phoneNum);
        LoginActivity.user = newUser;
        try {
            MainActivity.local_database.storeUserData(newUser);
            Intent intent = new Intent(this, GetPaymentInformationActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finish();
    }
}
