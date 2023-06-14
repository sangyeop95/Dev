package com.example.mypage;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mypage.databinding.FragmentWatchLayoutBinding;

import java.util.ArrayList;
import java.util.List;

public class WatchFragment extends Fragment {
    private FragmentWatchLayoutBinding binding;
    private WatchViewModel viewModel;
    private final UDiveRecyclerViewAdapter adapter = new UDiveRecyclerViewAdapter();
    private final List<WatchDto> data = new ArrayList<>(); // 뷰모델에서 가져온 데이터를 DB에 옮기기위한 WatchDto 데이터리스트
    private List<WatchDto> filteringList = new ArrayList<>(); // 필터링한 데이터를 담은 리스트
    public static final int WATCH_TYPE_NORMAL = 0, WATCH_TYPE_TEENAGER = 1;
    private static int watchType;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWatchLayoutBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(WatchViewModel.class);

        WatchDB database = WatchDB.getInstance(getContext()); // 데이터베이스 선언 및 초기화
        adapterSettingByWatchType(); // 시청모드에 따른 어댑터리스트 세팅
        viewSetting(); // 화면 세팅 메소드
        binding.allContentBtn.setChecked(true);
        if (!UDiveRecyclerViewAdapter.itemSelectList.isEmpty()) { UDiveRecyclerViewAdapter.itemSelectList.clear(); }

        binding.prevBtn.setOnClickListener(new View.OnClickListener() { // !(<) 이전 버튼 //
            @Override
            public void onClick(View view) {
                ((MainActivity) requireActivity()).moveTo(new MainFragment());
            }
        });

        binding.deleteBtn.setOnClickListener(new View.OnClickListener() { // !(휴지통) 삭제화면 버튼 //
            @Override
            public void onClick(View view) {
                ((MainActivity) requireActivity()).moveTo(new DeleteFragment());
            }
        });

        binding.dataAddBtn.setOnClickListener(new View.OnClickListener() { // !데이터 추가 버튼 //
            @Override
            public void onClick(View view) {
                viewModel.addData(); // 뷰모델에 있는 데이터를 가져와서 DB에 삽입
                data.addAll(viewModel.getList());
                if (getWatchType() == WATCH_TYPE_TEENAGER) { data.removeIf(WatchDto::getIsAdultCont); } // watchType이 15세일경우 'data'리스트에서 'AdultCont'변수가 true인 WatchDto객체는 제거
                for (int i=0; i<data.size(); i++) {
                    database.watchDao().insert(data.get(i));
                }
                adapter.setUDiveList(data); // 어댑터에 DB에 있는 데이터 세팅
                binding.allContentBtn.setChecked(true);
                viewSetting();
            }
        });

        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // !라디오 버튼 //
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int isSelect) {
                switch (isSelect) {
                    case R.id.allContentBtn:
                        if (getWatchType() == WATCH_TYPE_TEENAGER) {filteringList = database.watchDao().searchByIsAdult("0");}
                        else {filteringList = database.watchDao().getAll();}
                        break;
                    case R.id.ARContentBtn:
                        if (getWatchType() == WATCH_TYPE_TEENAGER) {filteringList = database.watchDao().searchByContentTypeIsNotAdult("AR");}
                        else {filteringList = (database.watchDao().searchByContentType("AR"));}
                        break;
                    case R.id.VRContentBtn:
                        if (getWatchType() == WATCH_TYPE_TEENAGER) {filteringList = database.watchDao().searchByContentTypeIsNotAdult("VR");}
                        else {filteringList = (database.watchDao().searchByContentType("VR"));}
                        break;
                    case R.id.liveContentBtn:
                        if (getWatchType() == WATCH_TYPE_TEENAGER) {filteringList = database.watchDao().searchByContentTypeIsNotAdult("LB");}
                        else {filteringList = (database.watchDao().searchByContentType("LB"));}
                        break;
                    case R.id.adultContentBtn:
                        filteringList = database.watchDao().searchByIsAdult("1");
                        break;
                } adapter.setUDiveList(filteringList);
                viewSetting();
            }
        });

        binding.watchMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getWatchType() == WATCH_TYPE_NORMAL) {
                    watchType = WATCH_TYPE_TEENAGER; // @15세이용가모드
                    adapter.setUDiveList(database.watchDao().searchByIsAdult("0"));
                } else if (getWatchType() == WATCH_TYPE_TEENAGER) {
                    watchType = WATCH_TYPE_NORMAL; // @일반모드
                    adapter.setUDiveList(database.watchDao().getAll());
                } viewSetting();
                binding.allContentBtn.setChecked(true);
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
    public void viewSetting() { // 화면 세팅
        if (getWatchType() == WATCH_TYPE_TEENAGER) {
            binding.watchMode.setBackgroundResource(R.drawable.round_rectangle_green);
            binding.watchMode.setText(getString(R.string.watch_type_teenager_text));
            isAdultContentBtn(View.GONE);
            if (!adapter.getUDiveList().isEmpty()) { yesDataView(); }
            else { noDataView(); }
        } else if (getWatchType() == WATCH_TYPE_NORMAL) {
            binding.watchMode.setBackgroundResource(R.drawable.round_rectangle_blue);
            binding.watchMode.setText(getString(R.string.watch_type_normal_text));
            isAdultContentBtn(View.VISIBLE);
            if (!WatchDB.getInstance(getContext()).watchDao().getAll().isEmpty()) { yesDataView(); }
            else { noDataView(); }
        }
    }

    public void yesDataView() { // 데이터가 있을경우의 화면 세팅
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setVisibility(View.VISIBLE);
        binding.deleteBtn.setVisibility(View.VISIBLE);
        binding.subTitle.setVisibility(View.INVISIBLE);
    }

    public void noDataView() { // 데이터가 없을경우의 화면 세팅
        binding.recyclerView.setVisibility(View.INVISIBLE);
        binding.deleteBtn.setVisibility(View.INVISIBLE);
        binding.subTitle.setVisibility(View.VISIBLE);
    }

    public void isAdultContentBtn(int visibleType) { // 성인콘텐츠 라디오버튼 UI
        binding.adultSpace.setVisibility(visibleType);
        binding.adultContentBtn.setVisibility(visibleType);
        binding.adultContentText.setVisibility(visibleType);
    }

    public void adapterSettingByWatchType() {
        if (WatchFragment.getWatchType() == WatchFragment.WATCH_TYPE_TEENAGER) { // 시청타입이 15세이용가일경우 DB에서 isAdultCont변수가 false(==0)인 WatchDto객체만 어댑터에 세팅
            adapter.setUDiveList(WatchDB.getInstance(getContext()).watchDao().searchByIsAdult("0"));
        } else if (WatchFragment.getWatchType() == WatchFragment.WATCH_TYPE_NORMAL) { // 시청타입이 일반모드일경우 DB에서 모든 데이터 가져와서 어댑터에 세팅
            adapter.setUDiveList(WatchDB.getInstance(getContext()).watchDao().getAll());
        } binding.recyclerView.setAdapter(adapter);
        adapter.setItemViewType(UDiveRecyclerViewAdapter.VIEW_TYPE_WATCH); // 리사이클러뷰 뷰타입 변경
    }

    public static int getWatchType() { return watchType; } // 시청모드 Getter
}
