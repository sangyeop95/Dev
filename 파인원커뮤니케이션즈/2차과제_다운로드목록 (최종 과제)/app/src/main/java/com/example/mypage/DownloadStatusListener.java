package com.example.mypage;

public interface DownloadStatusListener {

    void onAdded(DownloadDto downloadDto);
    void onPaused(DownloadDto downloadDto);
    void onProgress(DownloadDto downloadDto);
    void onCompleted(DownloadDto downloadDto);
    void onDeleted(DownloadDto downloadDto);
}
