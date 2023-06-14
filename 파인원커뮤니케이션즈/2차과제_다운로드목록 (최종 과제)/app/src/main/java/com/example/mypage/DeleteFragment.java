package com.example.mypage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mypage.databinding.FragmentDeleteLayoutBinding;

public class DeleteFragment extends Fragment {
    private FragmentDeleteLayoutBinding binding;
    private DownloadViewModel viewModel;
    private final UDiveRecyclerViewAdapter adapter = new UDiveRecyclerViewAdapter();
    private CustomToast customToast;
    private PopupFragment popupFragment;
    private int count = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDeleteLayoutBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(DownloadViewModel.class);
        customToast = new CustomToast(binding.getRoot().getContext());

        handleOnBackPressed();
        adapter.setAdapterList(viewModel.getList()); // 뷰모델에 남아있는 전체 데이터 가져와서 어댑터 리스트에 세팅
        adapter.setItemViewType(UDiveRecyclerViewAdapter.VIEW_TYPE_DELETE); // 리사이클러뷰 뷰타입 변경
        if (!adapter.hasObservers()) { // 어댑터에 관찰자가 등록되어 있는 동안 이 어댑터 아이템에 ID가 있는지 여부를 변경할 수 없다
            adapter.setHasStableIds(true); // notify 시 어댑터가 아이템을 추척하여 아이템의 ID가 같을 경우 ViewHolder 를 다시 매칭하지 않고 기존의 ViewHolder 를 사용함 (깜빡거리지 않음)
        }
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.allContentCountText.setText(getString(R.string.all_content_count_number, adapter.getAdapterList().size()));

        binding.closeBtn.setOnClickListener(view -> ((MainActivity) requireActivity()).moveTo(new DownloadFragment())); // @(X) 창닫기 버튼 //

        adapter.setOnItemClickListener((v, isSelect) -> { // !체크박스 개별선택 버튼 (콜백)
            if (isSelect) {
                count++;
                if (count == adapter.getAdapterList().size()) { binding.deleteCheckAllBtn.setChecked(true); } // 선택된 콘텐츠 개수가 어댑터 리스트의 전체 개수와 같을때
            } else {
                count--;
                binding.deleteCheckAllBtn.setChecked(false);
            }
            binding.contentCountText.setText(String.valueOf(count)); // count 변수 값 setText
            binding.itemDeleteBtn.setEnabled(count > 0); // 삭제하기 버튼 활성화/비활성화
        });

        binding.deleteCheckAllBtn.setOnClickListener(view -> { // @체크박스 전체선택 버튼 //
            if (binding.deleteCheckAllBtn.isChecked()) {
                checkAll(true);
                setButtonAndCount(adapter.getAdapterList().size());
            } else {
                checkAll(false);
                setButtonAndCount(0);
            }
        });

        popupFragment = new PopupFragment(() -> { // PopupFragment (DialogFragment 를 상속받은) 객체 생성 -> !삭제팝업창에서 확인버튼을 눌렀을 때 실행 (OnDialogClickListener)
            for (DownloadDto downloadDto : adapter.getAdapterList()) {
                if (downloadDto.getCheckState()) { viewModel.removeDownload(downloadDto); }
            }
            ((MainActivity) requireActivity()).moveTo(new DownloadFragment()); // 다운로드목록으로 이동
            customToast.showToast("삭제가 완료되었습니다.");
        });

        binding.itemDeleteBtn.setOnClickListener(view -> { // @삭제하기 버튼 //
            if (count == adapter.getAdapterList().size()) { showDialog("시청 목록이 모두 삭제됩니다.\n전체 삭제를 진행하시겠습니까?"); }
            else { showDialog(count + "개의 콘텐츠를 삭제하시겠습니까?"); }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // region Custom Method
    public void setButtonAndCount(int contentCount) {
        count = contentCount;
        binding.contentCountText.setText(String.valueOf(count));
        binding.itemDeleteBtn.setEnabled(count > 0);
    }

    public void checkAll(boolean setCheckStateAll) { // "DownloadDto" 객체의 CheckState 변수를 바꿈
        for (DownloadDto downloadDto : adapter.getAdapterList()) {
            if (setCheckStateAll) {
                if (!downloadDto.getCheckState()) { downloadDto.setCheckState(true); }
            } else {
                if (downloadDto.getCheckState()) { downloadDto.setCheckState(false); }
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void showDialog(String message) {
        popupFragment.setMessage(message);
        popupFragment.setCancelable(false);
        popupFragment.show(requireActivity().getSupportFragmentManager(), "");
    }

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