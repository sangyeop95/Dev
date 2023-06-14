package com.example.mypage;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mypage.databinding.CustomToastBinding;
import com.example.mypage.databinding.FragmentDeleteLayoutBinding;

public class DeleteFragment extends Fragment {
    private FragmentDeleteLayoutBinding binding;
    private final UDiveRecyclerViewAdapter adapter = new UDiveRecyclerViewAdapter();
    private int count = 0;
    private Boolean check = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDeleteLayoutBinding.inflate(inflater, container, false);

        WatchDB database = WatchDB.getInstance(getContext()); // 데이터베이스 선언 및 초기화
        adapterSettingByWatchType(); // 시청모드에 따른 어댑터리스트 세팅
        binding.allContentCountText.setText(getString(R.string.all_content_count_number, adapter.getUDiveList().size()));

        binding.closeBtn.setOnClickListener(new View.OnClickListener() { // !(X) 창닫기 버튼 //
            @Override
            public void onClick(View view) {
                ((MainActivity) requireActivity()).moveTo(new WatchFragment());
            }
        });

        adapter.setOnItemClickListener(new UDiveRecyclerViewAdapter.OnItemClickListener() { // !리사이클러뷰 아이템 체크박스 체크했을때 체크한 개수 세아림 (콜백) //
            @Override
            public void onItemClick(View v, boolean isSelect) {
                if (isSelect) {
                    count++;
                    if (count == adapter.getUDiveList().size()) {
                        binding.deleteCheckAllBtn.setChecked(true);
                    }
                } else {
                    count--;
                    if (binding.deleteCheckAllBtn.isChecked() && count != adapter.getUDiveList().size()) { // 전체삭제버튼은 체크되있지만 선택된 콘텐츠 개수가 전체개수가 아닐때
                        binding.deleteCheckAllBtn.setChecked(false);
                    }
                }
                binding.itemDeleteBtn.setEnabled(count > 0);
                binding.contentCountText.setText(String.valueOf(count));
            }
        });

        binding.deleteCheckAllBtn.setOnClickListener(new View.OnClickListener() { // !체크박스 전체선택 버튼 //
            @Override
            public void onClick(View view) {
                adapter.allSelectCheckbox(); // 리사이클러뷰 모든 아이템 체크박스 상태값 On/Off 메소드

                if (!check) {
                    check = true;
                    contentCountTextUIController(adapter.getUDiveList().size());
                    allDeleteListByWatchType();
                } else if (binding.deleteCheckAllBtn.isChecked()){
                    adapter.allSelectCheckbox();

                    check = true;
                    contentCountTextUIController(adapter.getUDiveList().size());
                    allDeleteListByWatchType();
                } else if (check) {
                    check = false;
                    contentCountTextUIController(0);
                    UDiveRecyclerViewAdapter.itemSelectList.clear();
                }
            }
        });

        OnDialogClickListener listener = new OnDialogClickListener() { // !삭제팝업창에서 확인버튼을 눌렀을 때 실행 (콜백) //
            @Override
            public void onClickConfirmButton() {
                for (int i=0; i<UDiveRecyclerViewAdapter.itemSelectList.size(); i++) { // 선택한 콘텐츠 DB에서 삭제
                    database.watchDao().deleteContentByContNm(UDiveRecyclerViewAdapter.itemSelectList.get(i));
                } UDiveRecyclerViewAdapter.itemSelectList.clear();

                ((MainActivity) requireActivity()).moveTo(new WatchFragment()); // 시청목록으로 이동
                LayoutInflater layoutInflater = LayoutInflater.from(binding.getRoot().getContext()); // 커스텀 토스트
                CustomToastBinding toastBinding = CustomToastBinding.inflate(layoutInflater);

                Toast toast = new Toast(layoutInflater.getContext());
                toast.setView(toastBinding.getRoot());
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.show();
                toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 50);
                toastBinding.toastText.setText("삭제가 완료되었습니다.");
            }
        };

        binding.itemDeleteBtn.setOnClickListener(new View.OnClickListener() { // !삭제하기 버튼 //
            @Override
            public void onClick(View view) { // PopUpFragment(다이얼로그 프래그먼트)로 화면에 띄울 메세지 넘김
                if (UDiveRecyclerViewAdapter.itemSelectList.size() == adapter.getUDiveList().size()) {    // 전체삭제 진행(O)
                    new PopUpFragment("시청 목록이 모두 삭제됩니다.\n전체 삭제를 진행하시겠습니까?", listener)
                                                .show(requireActivity().getSupportFragmentManager(), "");
                } else
                    new PopUpFragment(UDiveRecyclerViewAdapter.itemSelectList.size() + "개의 콘텐츠를 삭제하시겠습니까?", listener)
                                                .show(requireActivity().getSupportFragmentManager(), "");
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



    // ↓ 커스텀 메소드 ↓
    public void allDeleteListByWatchType() { // 시청타입에 따른 삭제목록 콘탠츠선택
        if (WatchFragment.getWatchType() == WatchFragment.WATCH_TYPE_TEENAGER) {
            UDiveRecyclerViewAdapter.itemSelectList = WatchDB.getInstance(getContext()).watchDao().searchAdultContentName("0");
        } else {
            UDiveRecyclerViewAdapter.itemSelectList = WatchDB.getInstance(getContext()).watchDao().getAllContentName();
        }
    }

    public void contentCountTextUIController(int contentCountNumber) { // UI 컨트롤
        count = contentCountNumber;
        binding.contentCountText.setText(String.valueOf(count));
        binding.itemDeleteBtn.setEnabled(count > 0);
    }

    public void adapterSettingByWatchType() {
        if (WatchFragment.getWatchType() == WatchFragment.WATCH_TYPE_TEENAGER) { // 시청타입이 15세이용가일경우 DB에서 isAdultCont변수가 false(==0)인 WatchDto객체만 어댑터에 세팅
            adapter.setUDiveList(WatchDB.getInstance(getContext()).watchDao().searchByIsAdult("0"));
        } else if (WatchFragment.getWatchType() == WatchFragment.WATCH_TYPE_NORMAL) { // 시청타입이 일반모드일경우 DB에서 모든 데이터 가져와서 어댑터에 세팅
            adapter.setUDiveList(WatchDB.getInstance(getContext()).watchDao().getAll());
        } binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setItemViewType(UDiveRecyclerViewAdapter.VIEW_TYPE_DELETE); // 리사이클러뷰 뷰타입 변경
    }

}
