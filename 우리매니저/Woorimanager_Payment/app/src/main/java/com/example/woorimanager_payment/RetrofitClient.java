package com.example.woorimanager_payment;

import static com.example.woorimanager_payment.MainActivity.PACKAGE_NAME;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String USER_CODE = "0100";
    private static final String USER_AGENT = PACKAGE_NAME.substring(PACKAGE_NAME.lastIndexOf(".") + 1) + "_" + USER_CODE;
    private static final String BASE_URL = "http://payment.dandihelper.com/";
    private static Retrofit retrofit = null;

    private RetrofitClient() { }

    public static Retrofit getInstance() {
        return RetrofitSingletonHolder.getClient();
    }

    private static OkHttpClient getHttpClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder();
        httpClient.addInterceptor(chain -> {
            Request requestWithUserAgent = chain.request().newBuilder().header("User-Agent", USER_AGENT).build();
            return chain.proceed(requestWithUserAgent);
        });
        return httpClient.build();
    }

    private static class RetrofitSingletonHolder {
        public static Retrofit getClient() {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                                        .baseUrl(BASE_URL)
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .client(getHttpClient())
                                        .build();
            }
            return retrofit;
        }
    }
}