package com.example.mypage;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class DownloadViewModel extends ViewModel {
    private MutableLiveData<DownloadDto> currentDownloadDtoState = new MutableLiveData<DownloadDto>();

    public LiveData<DownloadDto> getCurrentDownloadDtoState() {
        return currentDownloadDtoState;
    }

    private DownloadStatusListener downloadStatusListener = new DownloadStatusListener() {
        @Override
        public void onAdded(DownloadDto downloadDto) {
            Log.d("====onAdded | ", downloadDto.getContId() + " | " + downloadDto.getStatus() + " | " + downloadDto.getProgress());
            currentDownloadDtoState.postValue(downloadDto);
        }

        @Override
        public void onPaused(DownloadDto downloadDto) {
            Log.d("====onPaused | ", downloadDto.getContId() + " | " + downloadDto.getStatus() + " | " + downloadDto.getProgress());
            currentDownloadDtoState.postValue(downloadDto);
        }

        @Override
        public void onProgress(DownloadDto downloadDto) {
            Log.d("====onProgress | ", downloadDto.getContId() + " | " + downloadDto.getStatus() + " | " + downloadDto.getProgress());
            currentDownloadDtoState.postValue(downloadDto);
        }

        @Override
        public void onCompleted(DownloadDto downloadDto) {
            Log.d("====onCompleted | ", downloadDto.getContId() + " | " + downloadDto.getStatus() + " | " + downloadDto.getProgress());
            currentDownloadDtoState.postValue(downloadDto);
        }

        @Override
        public void onDeleted(DownloadDto downloadDto) {
            Log.d("====onDeleted | ", downloadDto.getContId() + " | " + downloadDto.getStatus() + " | " + downloadDto.getProgress());
            currentDownloadDtoState.postValue(downloadDto);
        }
    };

    public void addDownloadStatusListener() {
        DownloadManager.getInstance().addDownloadStatusListener(downloadStatusListener);
    }

    public void removeDownloadStatusListener() {
        DownloadManager.getInstance().removeDownloadStatusListener();
    }

    /**
     * 다운로드 목록 전체 리스트 조회
     *
     * @return 전체 리스트
     */
    public ArrayList<DownloadDto> getList() {
        ArrayList<DownloadDto> list = DownloadManager.getInstance().getList();
        Log.d("==== getList | ", list.toString());
        return list;
    }

    /**
     * 콘텐츠 삭제
     *
     * @param downloadDto
     * @return 삭제 여부
     */
    public boolean removeDownload(DownloadDto downloadDto) {
        if (downloadDto == null || downloadDto.getContId() == null)
            return false;

        return DownloadManager.getInstance().removeDownload(downloadDto);
    }

    /**
     * 다운로드 목록 전부 삭제
     *
     * @return 삭제 여부
     */
    public boolean removeAll() {
        return DownloadManager.getInstance().removeAll();
    }

    /**
     * 다운로드 중지
     *
     * @param downloadDto
     */
    public void pauseDownload(DownloadDto downloadDto) {
        DownloadManager.getInstance().pauseDownload(downloadDto);
    }

    /**
     * 다운로드 재시작 요청 (다운로드 대기 큐에 추가됨)
     *
     * @param downloadDto
     */
    public void startDownload(DownloadDto downloadDto) {
        DownloadManager.getInstance().startDownload(downloadDto);
    }
}