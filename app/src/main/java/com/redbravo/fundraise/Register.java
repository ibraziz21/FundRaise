package com.redbravo.fundraise;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class Register extends AppCompatActivity {

    EditText email;
    EditText username;
    EditText password;
    EditText confirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ParseUser.getCurrentUser() != null) {
            Log.i("User In", ParseUser.getCurrentUser().getUsername());
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
            finish();
        }

        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        confirm = findViewById(R.id.confirm);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());


    }

    public void SignUp (View view){
        String Email = email.getText().toString();
        String Username = username.getText().toString();
        String Password = password.getText().toString();
        String Confirm = confirm.getText().toString();

        if (Password.equals(Confirm)) {
            ParseUser user = new ParseUser();
            user.setEmail(Email);
            user.setUsername(Username);
            user.setPassword(Password);
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {

                    if (e == null) {

                        Intent intent =new Intent(getApplicationContext(), Home.class);
                        startActivity(intent);
                        finish();
                    } else {
                        e.getMessage();
                    }
                }
            });
            Log.i("Match", "Passwords Match");
        } else {
            Log.i("No match", "NO MATCH");
        }

    }

    public void login (View view){
        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
        finish();
    }
/*
    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if(i==KeyEvent.KEYCODE_ENTER && keyEvent.getAction()==KeyEvent.ACTION_DOWN){
            signup(view);
        }
        return false;
    }

    public void changetoLogin(View view) {

    }

}
  */
}