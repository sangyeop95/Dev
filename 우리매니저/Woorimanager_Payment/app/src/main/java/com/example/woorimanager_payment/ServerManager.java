package com.example.woorimanager_payment;

import android.util.Log;

import androidx.annotation.NonNull;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ServerManager {
    private final String TAG = this.getClass().getSimpleName();
    private final Retrofit retrofit = RetrofitClient.getInstance();
    private final RetrofitService service = retrofit.create(RetrofitService.class);
    private Call<ResponseBody> call;

    public void registerDataToServer(String token) {
        tokenToServer(token);
    }
    public void registerDataToServer(String token, String phone) throws IllegalArgumentException {
        if (!isNumber(phone)) {
            tokenToServer(token);
            throw new IllegalArgumentException("registerDataToServer(String token, String phone) => Parameter String 'phone' is not number format.");
        }
        tokenAndPhoneToServer(token, phone);
    }

    public void messageToServer(String message, String phone) throws NullPointerException, IllegalArgumentException {
        if (phone == null) {
            throw new NullPointerException("messageToServer(String message, String phone) => Parameter String 'phone' is null.");
        } else if (!isNumber(phone)) {
            throw new IllegalArgumentException("messageToServer(String message, String phone) => Parameter String 'phone' is not number format.");
        }
        call = service.sendMessage(message, phone);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Message sent to server succeeded and response received good!");
                } else {
                    Log.e(TAG, "Message sent to server succeeded and response received fail!");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e(TAG, "Failed to send message to server.");
            }
        });
    }

    // region Private Method
    private boolean isNumber(String stringNumber) {
        try {
            Long.parseLong(stringNumber);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void tokenToServer(String token) {
        call = service.sendToken(token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Token sent to server succeeded and response received good!");
                } else {
                    Log.e(TAG, "Token sent to server succeeded and response received fail!");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e(TAG, "Failed to send token to server.");
            }
        });
    }

    private void tokenAndPhoneToServer(String token, String phone) {
        call = service.sendTokenAndPhone(token, phone);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && phone != null) {
                    Log.d(TAG, "Token and PhoneNumber sent to server succeeded and response received good!");
                } else if (response.isSuccessful()) {
                    Log.d(TAG, "Token sent to server succeeded and response received good! (Parameter String 'phone' is null)");
                } else {
                    Log.e(TAG, "Token sent to server succeeded and response received fail! (Parameter String 'phone' is null)");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e(TAG, "Failed to send token and phonenumber to server.");
            }
        });
    }
    // endregion
}