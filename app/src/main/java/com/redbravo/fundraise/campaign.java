package com.redbravo.fundraise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

public class campaign extends AppCompatActivity {
    EditText commitamt;
TextView text1;
TextView text2;
    TextView amount;
    TextView amountc;
    TextView longdesc;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign);
        text1=findViewById(R.id.title1);
        text2=findViewById(R.id.shortDescript);
        amount=findViewById(R.id.AmountReq);
        amountc = findViewById(R.id.AmountCont);
        longdesc = findViewById(R.id.Longdesc);
        commitamt= findViewById(R.id.commit);
        button=findViewById(R.id.commitamt);
        final Intent i = getIntent();
        final String title = i.getStringExtra("title");

        text1.setText(title);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("campaigndata");
        query.whereEqualTo("campaignName",title);

      query.findInBackground(new FindCallback<ParseObject>() {
          @Override
          public void done(List<ParseObject> objects, ParseException e) {
              if (e==null  && objects.size()>0){
                  for(ParseObject object: objects) {
                      String shortD = object.getString("shortDescription");

                      String amt = String.valueOf(object.getInt("amountRequired"));
                      String amtc = String.valueOf(object.getInt("amountRaised"));
                      String longd = object.getString("longDescription");
                      text2.setText(shortD);
                      amount.setText("Amount Required: "+amt);
                      amountc.setText("Amount Raised: "+amtc);
                      longdesc.setText(longd);
                  }
              }else
              {
                  e.printStackTrace();
              }
          }
      });

button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String commited =commitamt.getText().toString();
        final int amtcommitted = Integer.parseInt(commited);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("campaigndata");
        query.whereEqualTo("campaignName",title);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e==null  && objects.size()>0){
                    for(ParseObject object: objects) {
                        int currentcommitted = Integer.parseInt(object.get("amountRaised").toString());
                        int newint = currentcommitted+amtcommitted;
                        object.put("amountRaised",newint);
                        object.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e==null){
                                    Toast.makeText(campaign.this, "Successfully Commited amount", Toast.LENGTH_SHORT).show();

                                }else{
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }else
                {
                    e.printStackTrace();
                }
            }
        });

        Intent intent = new Intent(getApplicationContext(),Payment.class);
        startActivity(intent);
    }
});

    }
/*
    public void commit(View view) {
        String commited =commitamt.getText().toString();
        final int amtcommitted = Integer.parseInt(commited);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("campaigndata");
        query.whereEqualTo("campaignName",title);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e==null  && objects.size()>0){
                    for(ParseObject object: objects) {
                      int currentcommitted = Integer.parseInt(object.get("amountRaised").toString());
                      int newint = currentcommitted+amtcommitted;
                      object.put("amountRaised",newint);
                      object.saveInBackground(new SaveCallback() {
                          @Override
                          public void done(ParseException e) {
                              if(e==null){
                                  Toast.makeText(campaign.this, "Successfully Commited amount", Toast.LENGTH_SHORT).show();
                              }else{
                                  e.printStackTrace();
                              }
                          }
                      });
                    }
                }else
                {
                    e.printStackTrace();
                }
            }
        });

*/


}
