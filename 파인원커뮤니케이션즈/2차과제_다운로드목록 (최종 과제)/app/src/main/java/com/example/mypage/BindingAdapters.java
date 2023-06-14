package com.example.mypage;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

public class BindingAdapters {
    // region DownloadLayoutView
    @BindingAdapter("recyclerViewVisible")
    public static void setRecyclerView(RecyclerView recyclerView, boolean existData) {
        int visibility = existData ? View.VISIBLE : View.INVISIBLE;
        recyclerView.setVisibility(visibility);
    }

    @BindingAdapter("dataDeleteButtonVisible")
    public static void setDeleteButtonView(Button button, boolean existData) {
        int visibility = existData ? View.VISIBLE : View.INVISIBLE;
        button.setVisibility(visibility);
    }

    @BindingAdapter("imageVisible")
    public static void setImageView(ImageView imageView, boolean existData) {
        int visibility = existData ? View.INVISIBLE : View.VISIBLE;
        imageView.setVisibility(visibility);
    }

    @BindingAdapter("textVisible")
    public static void setTextView(TextView textView, boolean existData) {
        int visibility = existData ? View.INVISIBLE : View.VISIBLE;
        textView.setVisibility(visibility);
    }
    // endregion

    // region ItemView
    @BindingAdapter("itemAdultVisible")
    public static void setItemAdultView(ImageView imageView, DownloadDto item) {
        int visibility = item.getIsAdultCont() ? View.VISIBLE : View.INVISIBLE;
        imageView.setVisibility(visibility);
    }

    @BindingAdapter("itemTypeSetting")
    public static void setItemTypeView(ImageView imageView, DownloadDto item) {
        imageView.setVisibility(View.VISIBLE);
        switch (item.getTy1Code()) {
            case "AR": imageView.setImageResource(R.drawable.flag_ar); break;
            case "VR": imageView.setImageResource(R.drawable.flag_vr); break;
            case "LB": imageView.setImageResource(R.drawable.flag_live); break;
            case "ETC": imageView.setVisibility(View.INVISIBLE); break;
        }
    }

    @BindingAdapter("itemRemoveBtnVisible")
    public static void setItemBtnView(ImageView imageView, DownloadDto item) {
        switch (item.getStatus()) {
            case DownloadManager.PROGRESS: case DownloadManager.ADDED: case DownloadManager.PAUSED:
                imageView.setVisibility(View.VISIBLE);
                break;

            case DownloadManager.COMPLETED: case DownloadManager.DELETED:
                imageView.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @BindingAdapter("itemStartAndPauseCheckbox")
    public static void setItemStartAndPauseCheckboxView(CheckBox checkBox, DownloadDto item) {
        checkBox.setVisibility(View.VISIBLE);
        switch (item.getStatus()) {
            case DownloadManager.PROGRESS: case DownloadManager.ADDED:
                checkBox.setChecked(true);
                break;

            case DownloadManager.PAUSED:
                checkBox.setChecked(false);
                break;

            case DownloadManager.COMPLETED: case DownloadManager.DELETED:
                checkBox.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @BindingAdapter("itemDeleteCheckbox")
    public static void setItemDeleteCheckboxView(CheckBox checkBox, DownloadDto item) {
        checkBox.setChecked(item.getCheckState());
    }

    @BindingAdapter("itemThumbnailVisible")
    public static void setItemThumbnailView(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .dontAnimate()
                .into(imageView);
    }
    // endregion
}