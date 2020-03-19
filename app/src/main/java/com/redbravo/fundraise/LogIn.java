package com.redbravo.fundraise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LogIn extends AppCompatActivity {
    EditText user;
    EditText pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        user=findViewById(R.id.username);
        pass=findViewById(R.id.password);


    }

    public void Login(View view) {
        String username=user.getText().toString();
        String password=pass.getText().toString();

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e==null){
        Intent intent=new Intent(getApplicationContext(),Home.class);
        startActivity(intent);
        finish();
                }else{
                    e.printStackTrace();
                }

            }
        });
    }

    public void register(View view) {
        Intent intent = new Intent(getApplicationContext(),Register.class);
        startActivity(intent);
        finish();



    }
}
