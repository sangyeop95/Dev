package com.example.mypage;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.example.mypage.databinding.CustomToastBinding;

public class CustomToast {
    private final Toast toast;
    private final Context context;

    public CustomToast(Context context) {
        this.context = context;
        this.toast = new Toast(context);
    }

    public void showToast(String message) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        CustomToastBinding toastBinding = CustomToastBinding.inflate(layoutInflater);

        toast.setView(toastBinding.getRoot());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 50);
        toastBinding.toastText.setText(message);
    }
}