package com.example.mypage;

import android.graphics.drawable.ClipDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypage.databinding.ItemViewDeleteListBinding;
import com.example.mypage.databinding.ItemViewDownloadListBinding;

import java.util.ArrayList;

public class UDiveRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private int itemViewType;
    public static final int VIEW_TYPE_WATCH = 0, VIEW_TYPE_DELETE = 1;
    private ArrayList<DownloadDto> list;
    private OnItemClickListener itemClickListener;
    private OnItemOrderListener itemOrderListener;

    public ArrayList<DownloadDto> getAdapterList() { return list; } // 어댑터 콘텐츠 리스트 Getter, Setter
    public void setAdapterList(ArrayList<DownloadDto> list) { this.list = list; }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) { this.itemClickListener = itemClickListener; }

    public void setOnItemOrderListener(OnItemOrderListener itemOrderListener) { this.itemOrderListener = itemOrderListener; }

    // region Inner Class : DownloadListViewHolder Class
    private static class DownloadListViewHolder extends RecyclerView.ViewHolder { // 다운로드 목록 클래스
        ItemViewDownloadListBinding binding;
        OnItemOrderListener orderListener;
        DownloadDto dto;
        ImageView imageView;
        ClipDrawable clipDrawable;

        public DownloadListViewHolder(@NonNull ItemViewDownloadListBinding binding, OnItemOrderListener orderListener) { // 다운로드 목록 생성자
            super(binding.getRoot());
            this.binding = binding;
            this.orderListener = orderListener;
            imageView = itemView.findViewById(R.id.progress_circular);
            clipDrawable = (ClipDrawable) imageView.getBackground();

            binding.startAndPauseBtn.setOnClickListener(view -> { // 재생/일시정지 버튼
                if (binding.startAndPauseBtn.isChecked() && !dto.getStatus().equals(DownloadManager.PROGRESS)) {
                    binding.startAndPauseBtn.setChecked(true);
                    orderListener.onItemBeginOrder(itemView, dto, "start");
                } else {
                    binding.startAndPauseBtn.setChecked(false);
                    orderListener.onItemBeginOrder(itemView, dto, "pause");
                }
            });

            binding.downloadDeleteBtn.setOnClickListener(view -> orderListener.onItemBeginOrder(itemView, dto, "remove")); // 다운로드 삭제 버튼
        }

        public void bind(DownloadDto item) {
            dto = item;
            binding.setItem(item); // 타이틀, 성인콘텐츠, 콘텐츠타입, 썸네일, 아이템버튼, 프로그레스써클
            clipDrawable.setLevel(item.getProgress() * 100); // 프로그레스
        }
    }
    // endregion

    // region Inner Class : DeleteListViewHolder Class
    private static class DeleteListViewHolder extends RecyclerView.ViewHolder { // 삭제목록 클래스
        ItemViewDeleteListBinding binding;
        OnItemClickListener clickListener;
        DownloadDto dto;

        public DeleteListViewHolder(@NonNull ItemViewDeleteListBinding binding, OnItemClickListener clickListener) { // 삭제목록 생성자
            super(binding.getRoot());
            this.binding = binding;
            this.clickListener = clickListener;

            binding.getRoot().setOnClickListener(view -> {
                dto.setCheckState(!binding.checkbox.isChecked());
                clickListener.onItemClick(itemView, !binding.checkbox.isChecked());
                binding.checkbox.setChecked(!binding.checkbox.isChecked());
            });

            binding.checkbox.setOnClickListener(view -> {
                dto.setCheckState(binding.checkbox.isChecked());
                clickListener.onItemClick(itemView, binding.checkbox.isChecked());
            });
        }

        public void bind(DownloadDto item) {
            dto = item;
            binding.setItem(item); // 타이틀, 성인콘텐츠, 콘텐츠타입, 썸네일, 아이템 체크박스
        }
    }
    // endregion

    // region RecyclerView Abstract Method
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (itemViewType == VIEW_TYPE_WATCH) {
            ItemViewDownloadListBinding downloadListBinding = ItemViewDownloadListBinding.inflate(layoutInflater, parent, false);
            return new DownloadListViewHolder(downloadListBinding, itemOrderListener);
        } else {
            ItemViewDeleteListBinding deleteListBinding = ItemViewDeleteListBinding.inflate(layoutInflater, parent, false);
            return new DeleteListViewHolder(deleteListBinding, itemClickListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DownloadListViewHolder) { ((DownloadListViewHolder) holder).bind(list.get(position)); }
        else ((DeleteListViewHolder) holder).bind(list.get(position));
    }

    @Override
    public int getItemCount() { return list.size(); }
    // endregion

    // region RecyclerView Method Override
    @Override
    public int getItemViewType(int position) { return itemViewType; }

    @Override
    public long getItemId(int position) { return list.get(position).getContId().hashCode(); } // 각 아이템에게 고유한 유니크 ID를 부여함
    // endregion

    // region RecyclerView Custom Method
    public void setItemViewType(int itemViewType) { this.itemViewType = itemViewType; } // 뷰 타입 변경
    // endregion
}