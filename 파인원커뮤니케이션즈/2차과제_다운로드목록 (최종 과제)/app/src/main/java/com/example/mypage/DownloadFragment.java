package com.example.mypage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.example.mypage.databinding.FragmentDownloadLayoutBinding;

public class DownloadFragment extends Fragment {
    private FragmentDownloadLayoutBinding binding;
    private DownloadViewModel viewModel;
    private final UDiveRecyclerViewAdapter adapter = new UDiveRecyclerViewAdapter();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDownloadLayoutBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(DownloadViewModel.class);

        handleOnBackPressed();
        RecyclerView.ItemAnimator animator = binding.recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }

        viewModel.addDownloadStatusListener();
        adapter.setAdapterList(viewModel.getList()); // 뷰모델에 남아있는 전체 데이터 가져와서 어댑터 리스트에 세팅
        adapter.setItemViewType(UDiveRecyclerViewAdapter.VIEW_TYPE_WATCH); // 리사이클러뷰 뷰타입 변경
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.setVisibleData(!adapter.getAdapterList().isEmpty());

        binding.prevBtn.setOnClickListener(view -> ((MainActivity) requireActivity()).moveTo(new MainFragment())); // @이전(<) 버튼 //

        binding.deleteBtn.setOnClickListener(view -> ((MainActivity) requireActivity()).moveTo(new DeleteFragment())); // @삭제(휴지통) 버튼 //

        final Observer<DownloadDto> downloadDtoObserver = downloadingDto -> { // !옵저버
            for (DownloadDto adapterDto : adapter.getAdapterList()) {
                if (adapterDto.getContId().equals(downloadingDto.getContId())) {
                    adapterDto.setStatus(downloadingDto.getStatus());
                    adapterDto.setProgress(downloadingDto.getProgress());
                    adapter.notifyItemChanged(adapter.getAdapterList().indexOf(adapterDto));
                }
            }
            binding.setVisibleData(!adapter.getAdapterList().isEmpty());
        };

        viewModel.getCurrentDownloadDtoState().observe(getViewLifecycleOwner(), downloadDtoObserver); // 옵저버 변화감지

        adapter.setOnItemOrderListener((v, downloadDto, order) -> { // !아이템 다운로드(재생/일시정지/삭제) 버튼 리스너 (콜백)
            switch (order) {
                case "start":
                    viewModel.startDownload(downloadDto);
                    break;
                case "pause":
                    viewModel.pauseDownload(downloadDto);
                    break;
                case "remove":
                    if (viewModel.removeDownload(downloadDto)) {
                        binding.recyclerView.getAdapter().notifyItemRemoved(adapter.getAdapterList().indexOf(downloadDto)); // 뷰에 해당 "DownloadDto" 객체가 삭제되었음을 알림
                        adapter.getAdapterList().remove(downloadDto);
                    }
                    break;
            }
        });

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.setAdapterList(viewModel.getList());
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        viewModel.removeDownloadStatusListener();
    }

    // region Custom Method
    public void handleOnBackPressed() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                ((MainActivity) requireActivity()).moveTo(new MainFragment());
            }
        });
    }
    // endregion
}