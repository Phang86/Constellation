package com.yyzy.constellation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yyzy.constellation.R;
import com.yyzy.constellation.entity.LocalMusicEntity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LocalMusicAdapter extends RecyclerView.Adapter<LocalMusicAdapter.ViewHolder> {

    private Context context;
    private List<LocalMusicEntity> mData;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    public LocalMusicAdapter(Context context, List<LocalMusicEntity> mData) {
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_local_musice,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        LocalMusicEntity entity = mData.get(position);
        holder.tvNum.setText(entity.getId());
        holder.tvDuration.setText(entity.getDuration());
        holder.tvGeShou.setText(entity.getGeShou());
        holder.tvGeMing.setText(entity.getGeMing());
        holder.tvDvd.setText(entity.getDvd());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvGeShou,tvGeMing,tvDuration,tvNum,tvDvd;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvGeShou = itemView.findViewById(R.id.item_localMusic_Ashley);
            tvGeMing = itemView.findViewById(R.id.item_localMusic_name);
            tvDuration = itemView.findViewById(R.id.item_localMusic_duration);
            tvNum = itemView.findViewById(R.id.item_localMusic_num);
            tvDvd = itemView.findViewById(R.id.item_localMusic_dvd);
        }
    }
}
