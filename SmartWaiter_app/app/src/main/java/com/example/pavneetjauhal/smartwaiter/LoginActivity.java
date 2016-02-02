package com.example.pavneetjauhal.smartwaiter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void login(View view) throws Exception {
        EditText getFields = (EditText) findViewById(R.id.passwordEditText);
        String password = getFields.getText().toString();
        if (password != null || !password.isEmpty()){
            if(!LocalAuth.checkPassword(password, MainActivity.user.getPassword(), MainActivity.user.getSalt())){
                Toast.makeText(getApplicationContext(), "Invalid Password",
                        Toast.LENGTH_LONG).show();
                return;
            }
        }
        finish();
    }
}
