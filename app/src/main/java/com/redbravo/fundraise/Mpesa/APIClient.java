package com.redbravo.fundraise.Mpesa;

import com.redbravo.fundraise.Mpesa.interceptor.AccessTokenInterceptor;
import com.redbravo.fundraise.Mpesa.interceptor.AuthInterceptor;
import com.redbravo.fundraise.Mpesa.services.STKPushService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.redbravo.fundraise.AppConstants.BASE_URL;
import static com.redbravo.fundraise.AppConstants.CONNECT_TIMEOUT;
import static com.redbravo.fundraise.AppConstants.READ_TIMEOUT;
import static com.redbravo.fundraise.AppConstants.WRITE_TIMEOUT;

public class APIClient {
    private Retrofit retrofit;
    private boolean isDebug;
    private boolean isGetAccessToken;
    private String mAuthToken;
    private HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();

    public APIClient setIsDebug(boolean isDebug) {
        this.isDebug = isDebug;
        return this;
    }

    public APIClient setAuthToken(String authToken) {
        mAuthToken = authToken;
        return this;
    }

    public APIClient setGetAccessToken(boolean getAccessToken) {
        isGetAccessToken = getAccessToken;
        return this;
    }


    private OkHttpClient.Builder okHttpClient() {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor);

        return okHttpClient;
    }


    private Retrofit getRestAdapter() {

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(BASE_URL);
        builder.addConverterFactory(GsonConverterFactory.create());

        if (isDebug) {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }

        OkHttpClient.Builder okhttpBuilder = okHttpClient();

        if (isGetAccessToken) {
            okhttpBuilder.addInterceptor(new AccessTokenInterceptor());
        }

        if (mAuthToken != null && !mAuthToken.isEmpty()) {
            okhttpBuilder.addInterceptor(new AuthInterceptor(mAuthToken));
        }

        builder.client(okhttpBuilder.build());

        retrofit = builder.build();

        return retrofit;
    }

    public STKPushService mpesaService() {
        return getRestAdapter().create(STKPushService.class);
    }
}
