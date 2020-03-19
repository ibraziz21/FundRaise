package com.redbravo.fundraise;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {
private static int timeout=6000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
            if (ParseUser.getCurrentUser() != null) {
                Log.i("User In", ParseUser.getCurrentUser().getUsername());
                Intent intent = new Intent(MainActivity.this, Home.class);
                startActivity(intent);
                finish();
            }else{
                Intent i = new Intent(MainActivity.this,LogIn.class);
                startActivity(i);
                finish();
            }
        }
    },timeout);
    }
}
