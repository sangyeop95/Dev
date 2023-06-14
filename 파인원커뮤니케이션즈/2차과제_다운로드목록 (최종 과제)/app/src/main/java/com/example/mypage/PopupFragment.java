package com.example.mypage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mypage.databinding.FragmentPopupBinding;

public class PopupFragment extends DialogFragment {
    private FragmentPopupBinding binding;
    private final OnDialogClickListener listener;
    private String message;

    public PopupFragment(OnDialogClickListener listener) { this.listener = listener; } // 생성자

    public void setMessage(String message) { this.message = message; }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPopupBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getDialog() != null) { getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent); }

        binding.popupText.setText(message);

        binding.yesBtn.setOnClickListener(view1 -> { // @네 버튼 //
            listener.onClickConfirmButton();
            dismiss();
        });

        binding.noBtn.setOnClickListener(view12 -> dismiss()); // @아니오 버튼 //
    }
}