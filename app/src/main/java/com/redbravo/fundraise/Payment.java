package com.redbravo.fundraise;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.redbravo.fundraise.Mpesa.APIClient;
import com.redbravo.fundraise.Mpesa.Utils;
import com.redbravo.fundraise.Mpesa.model.AccessToken;
import com.redbravo.fundraise.Mpesa.model.STKPush;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.redbravo.fundraise.AppConstants.BUSINESS_SHORT_CODE;
import static com.redbravo.fundraise.AppConstants.CALLBACKURL;
import static com.redbravo.fundraise.AppConstants.PARTYB;
import static com.redbravo.fundraise.AppConstants.PASSKEY;
import static com.redbravo.fundraise.AppConstants.TRANSACTION_TYPE;

public class Payment extends AppCompatActivity implements View.OnClickListener {

    private APIClient mApiClient;
    private ProgressDialog mProgressDialog;



    EditText mAmount;

    EditText mPhone;

    Button mPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        mAmount=findViewById(R.id.etAmount);
        mPhone=findViewById(R.id.etPhone);
        mPay=findViewById(R.id.btnPay);
mPay.setOnClickListener(this);

        mProgressDialog = new ProgressDialog(this);
        mApiClient = new APIClient();
        mApiClient.setIsDebug(true); //Set True to enable logging, false to disable.



        getAccessToken(); // Request for an access token from the MPESA API.

    }

    public void getAccessToken() {
        mApiClient.setGetAccessToken(true);
        mApiClient.mpesaService().getAccessToken().enqueue(new Callback<AccessToken>() {
            @Override
            public void onResponse(@NonNull Call<AccessToken> call, @NonNull Response<AccessToken> response) {

                if (response.isSuccessful()) {
                    mApiClient.setAuthToken(response.body().accessToken);
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccessToken> call, @NonNull Throwable t) {

            }
        });
    }

    // Initiate payment after a user has entered their name and the amount they wish to pay.
    // The amount can also be passed in from the price of a product for example, if you were integrating an online shop.
    @Override
    public void onClick(View view) {
        if (view== mPay){
            String phone_number = mPhone.getText().toString();
            String amount = mAmount.getText().toString();
            performSTKPush(phone_number,amount);
        }
    }

    //These are the same details that we were setting when we introduced ourselves to the MPesa Daraja API.
    public void performSTKPush(String phone_number,String amount) {
        mProgressDialog.setMessage("Processing your request");
        mProgressDialog.setTitle("Please Wait...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();
        String timestamp = Utils.getTimestamp();
        STKPush stkPush = new STKPush(
                BUSINESS_SHORT_CODE,
                Utils.getPassword(BUSINESS_SHORT_CODE, PASSKEY, timestamp),
                timestamp,
                TRANSACTION_TYPE,
                String.valueOf(amount),
                Utils.sanitizePhoneNumber(phone_number),
                PARTYB,
                Utils.sanitizePhoneNumber(phone_number),
                CALLBACKURL,
                "test", //The account reference
                "test"  //The transaction description
        );

        mApiClient.setGetAccessToken(false);

        //Sending the data to the Mpesa API, remember to remove the logging when in production.
        mApiClient.mpesaService().sendPush(stkPush).enqueue(new Callback<STKPush>() {
            @Override
            public void onResponse(@NonNull Call<STKPush> call, @NonNull Response<STKPush> response) {
                mProgressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        Timber.d("post submitted to API. %s", response.body());
                    } else {
                        Timber.e("Response %s", response.errorBody().toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<STKPush> call, @NonNull Throwable t) {
                mProgressDialog.dismiss();
                Timber.e(t);
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}


