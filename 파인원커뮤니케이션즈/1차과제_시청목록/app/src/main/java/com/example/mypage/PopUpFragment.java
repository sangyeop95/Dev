package com.example.mypage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mypage.databinding.FragmentPopupBinding;

public class PopUpFragment extends DialogFragment {
    private final String message;
    private final OnDialogClickListener listener;
    private FragmentPopupBinding binding;

    public PopUpFragment(String message, OnDialogClickListener listener) {
        this.message = message;
        this.listener = listener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPopupBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.popupText.setText(message);

        binding.yesBtn.setOnClickListener(new View.OnClickListener() { // !네 버튼 //
            @Override
            public void onClick(View view) {
                listener.onClickConfirmButton();
                dismiss();
            }
        });

        binding.noBtn.setOnClickListener(new View.OnClickListener() { // !아니오 버튼 //
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
