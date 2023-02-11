package com.yyzy.constellation.user.feedback.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.yyzy.constellation.R;
import com.yyzy.constellation.tally.util.OnClickSure;
import com.yyzy.constellation.user.feedback.OnLongClick;
import com.yyzy.constellation.user.feedback.bean.FeedBackBean;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.List;

import static java.lang.System.currentTimeMillis;

public class HistoryFeedBackAdapter extends RecyclerView.Adapter<HistoryFeedBackAdapter.ViewHolder> {

    private Context context;
    private List<FeedBackBean> mData;
    private OnLongClick onLongClick;

    public HistoryFeedBackAdapter(Context context, List<FeedBackBean> mData, OnLongClick longClick) {
        this.context = context;
        this.mData = mData;
        this.onLongClick = longClick;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history_feedback,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HistoryFeedBackAdapter.ViewHolder holder, int position) {
        FeedBackBean bean = mData.get(position);
//        String currDate2 = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒 EEEE").format(currentTimeMillis());
//        Log.e("TAG", "onBindViewHolder: "+currDate2);
        holder.tvTime.setText(bean.getFeedBackTime());
        holder.tvContent.setText(bean.getUserContent());
        String repAddress = bean.getUserAddress().replace("·", "").replace(" ","");
        holder.tvAddress.setText(repAddress);
        String userPhone = bean.getUserPhone();
        String subPhone = userPhone.substring(0,3)+"****"+userPhone.substring(7);
        holder.tvPhone.setText(subPhone);
        holder.cv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                FeedBackBean backBean = mData.get(position);
                onLongClick.onLongClick(backBean,v);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTime,tvContent,tvAddress,tvPhone;
        CardView cv;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.item_feedback_time);
            tvContent = itemView.findViewById(R.id.item_feedback_content);
            tvAddress = itemView.findViewById(R.id.item_feedback_address);
            tvPhone = itemView.findViewById(R.id.item_feedback_phone);
            cv = itemView.findViewById(R.id.item_feedback_cv);
        }
    }
}
