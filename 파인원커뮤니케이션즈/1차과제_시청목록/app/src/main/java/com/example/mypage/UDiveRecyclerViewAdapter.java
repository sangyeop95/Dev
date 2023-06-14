package com.example.mypage;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mypage.databinding.CustomToastBinding;
import com.example.mypage.databinding.ItemViewDeleteListBinding;
import com.example.mypage.databinding.ItemViewWatchListBinding;

import java.util.ArrayList;
import java.util.List;

public class UDiveRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private int ItemViewType;
    public static final int VIEW_TYPE_WATCH = 0, VIEW_TYPE_DELETE = 1;
    private List<WatchDto> list;
    public static List<String> itemSelectList = new ArrayList<>(); // 체크박스에서 선택한 콘텐츠를 담을 리스트 (장바구니)
    private boolean allSelect; // 전체선택 변수
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener { // 아이템 클릭 리스너 콜백 인터페이스
        void onItemClick(View v, boolean isSelect);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {this.itemClickListener = listener;}

    public List<WatchDto> getUDiveList() {return list;} // 어댑터 콘텐츠 리스트 Getter, Setter
    public void setUDiveList(List<WatchDto> list) {this.list = list;}

    private static class WatchListViewHolder extends RecyclerView.ViewHolder { // 시청목록 클래스
        ItemViewWatchListBinding binding;

        public WatchListViewHolder(@NonNull ItemViewWatchListBinding binding) { // 시청목록 생성자
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(WatchDto item) {
            binding.titleText.setText(item.getContNm()); // 타이틀

            if (item.getTy1Code() != null) {binding.contentType.setVisibility(View.VISIBLE); // 콘텐츠 타입
                switch (item.getTy1Code()) {
                    case "AR": binding.contentType.setImageResource(R.drawable.flag_ar); break;
                    case "VR": binding.contentType.setImageResource(R.drawable.flag_vr); break;
                    case "LB": binding.contentType.setImageResource(R.drawable.flag_live); break;}
            } else if (item.getTy1Code() == null) {binding.contentType.setVisibility(View.INVISIBLE);}

            if (item.getPchrgFreeCode().equals("C")) {binding.charge.setVisibility(View.VISIBLE);}  // 유료 코드
            else if (item.getPchrgFreeCode().equals("F")) {binding.charge.setVisibility(View.INVISIBLE);}

            if (item.getIsAdultCont()) {binding.adultType.setVisibility(View.VISIBLE);}     // 성인 콘텐츠
            else {binding.adultType.setVisibility(View.INVISIBLE);}

            Glide.with(binding.getRoot()).load(item.getPosterUrl()).into(binding.thumbnail); // 썸네일

            binding.itemView.setOnClickListener(new View.OnClickListener() { // 아이템뷰 클릭시 토스트 메시지 출력
                @Override
                public void onClick(View view) {
                    LayoutInflater layoutInflater = LayoutInflater.from(binding.getRoot().getContext());
                    CustomToastBinding toastBinding = CustomToastBinding.inflate(layoutInflater);

                    Toast toast = new Toast(layoutInflater.getContext());
                    toast.setView(toastBinding.getRoot());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.show();
                    toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 50);
                    if(item.getPrice() == 0) {toastBinding.toastText.setText("무료 콘텐츠 입니다.");}
                    else toastBinding.toastText.setText(item.getPrice() + "원 입니다.");
                }
            });
        }
    }

    private static class DeleteListViewHolder extends RecyclerView.ViewHolder { // 삭제목록 클래스
        ItemViewDeleteListBinding binding;
        OnItemClickListener clickListener;
        boolean change = false;

        public DeleteListViewHolder(@NonNull ItemViewDeleteListBinding binding, OnItemClickListener clickListener) { // 삭제목록 생성자
            super(binding.getRoot());
            this.binding = binding;
            this.clickListener = clickListener;
        }

        public void bind(WatchDto item) {
            binding.titleText.setText(item.getContNm()); // 타이틀

            if (item.getTy1Code() != null) {binding.contentType.setVisibility(View.VISIBLE); // 콘텐츠 타입
                switch (item.getTy1Code()) {
                    case "AR": binding.contentType.setImageResource(R.drawable.flag_ar); break;
                    case "VR": binding.contentType.setImageResource(R.drawable.flag_vr); break;
                    case "LB": binding.contentType.setImageResource(R.drawable.flag_live); break;}
            } else if (item.getTy1Code() == null) {binding.contentType.setVisibility(View.INVISIBLE);}

            if (item.getPchrgFreeCode().equals("C")) {binding.charge.setVisibility(View.VISIBLE);}  // 유료 코드
            else if (item.getPchrgFreeCode().equals("F")) {binding.charge.setVisibility(View.INVISIBLE);}

            if (item.getIsAdultCont()) {binding.adultType.setVisibility(View.VISIBLE);}     // 성인 콘텐츠
            else {binding.adultType.setVisibility(View.INVISIBLE);}

            Glide.with(binding.getRoot()).load(item.getPosterUrl()).into(binding.thumbnail); // 썸네일
            binding.thumbnail.setOnClickListener(new View.OnClickListener() { // 썸네일 클릭시 체크박스 이벤트 발생
                @Override
                public void onClick(View view) {
                    if (!change) {
                        binding.checkbox.setChecked(true);
                        change = true;
                    } else {
                        binding.checkbox.setChecked(false);
                        change = false;
                    }
                }
            });

            binding.checkbox.setOnCheckedChangeListener(null);  // !스크롤시 체크박스 상태값유지하기 위함
            if (item.getCheckState()) {binding.checkbox.setChecked(true);}
            else if (!item.getCheckState() || item.getCheckState() == null) {binding.checkbox.setChecked(false);}
            binding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // 체크박스 클릭시 WatchDto 'checkState' 변수값 변경
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        item.setCheckState(true);
                        itemSelectList.add(item.getContNm());
                    } else {
                        item.setCheckState(false);
                        itemSelectList.remove(item.getContNm());
                    }

                    clickListener.onItemClick(itemView, isChecked);
                }
            });
        }
    }



    //  ↓ RecyclerView 필수구현 추상 메소드 ↓
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (ItemViewType == VIEW_TYPE_WATCH) {
            ItemViewWatchListBinding watchListBinding = ItemViewWatchListBinding.inflate(layoutInflater, parent, false);
            return new WatchListViewHolder(watchListBinding);
        } else {
            ItemViewDeleteListBinding deleteListBinding = ItemViewDeleteListBinding.inflate(layoutInflater, parent, false);
            return new DeleteListViewHolder(deleteListBinding, itemClickListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof WatchListViewHolder) {((WatchListViewHolder) holder).bind(list.get(position));}
        else ((DeleteListViewHolder) holder).bind(list.get(position));
    }

    @Override
    public int getItemCount() {return list.size();}



    // ↓ RecyclerView 메소드 오버로딩 ↓
    @Override
    public int getItemViewType(int position) {return ItemViewType;}



    // ↓ RecyclerView 제어를 위한 커스텀 메소드 ↓
    public void setItemViewType(int itemViewType) { // 뷰 타입 변경
        ItemViewType = itemViewType;
        notifyDataSetChanged();
    }

    public void allSelectCheckbox() { // 아이템 체크박스 전체선택 On/Off 메소드
        if (!allSelect) {
            for(WatchDto watchDto : list) { watchDto.setCheckState(true); }
            allSelect = true;
        } else {
            for(WatchDto watchDto : list) { watchDto.setCheckState(false); }
            allSelect = false;
        } notifyDataSetChanged();
    }

}
