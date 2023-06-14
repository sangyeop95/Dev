package com.example.mypage;

import android.view.View;

public interface OnItemOrderListener {

    void onItemBeginOrder(View v, DownloadDto downloadDto, String order);
}
