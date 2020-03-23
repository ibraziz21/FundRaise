package com.redbravo.fundraise;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import timber.log.Timber;

public class campaign extends AppCompatActivity {
   ImageView image;
    EditText commitamt;
TextView text1;

    TextView amount;
    TextView amountc;
    TextView longdesc;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign);
        text1=findViewById(R.id.title1);

        amount=findViewById(R.id.AmountReq);
        amountc = findViewById(R.id.AmountCont);
        longdesc = findViewById(R.id.Longdesc);
        commitamt= findViewById(R.id.commit);
        button=findViewById(R.id.commitamt);
        image = findViewById(R.id.image);
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
                      ParseFile file= (ParseFile) object.get("Image");
                      file.getDataInBackground(new GetDataCallback() {
                                                   @Override
                                                   public void done(byte[] data, ParseException e) {
                                                       if (e == null && data != null) {
                                                           Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);


                                                           image.setImageBitmap(bitmap);
                                                       }
                                                   }
                                               });
                      String amt = String.valueOf(object.getInt("amountRequired"));
                      String amtc = String.valueOf(object.getInt("amountRaised"));
                      String longd = object.getString("longDescription");

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

        ParseObject data =new ParseObject("CommitedAmt");
        data.put("User", ParseUser.getCurrentUser());
        data.put("campaignName",title);
        data.put("Amount",amtcommitted);
        data.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null){
                    Timber.i("saved");
                }else{
                    e.printStackTrace();
                }

            }
        });

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
