package com.yyzy.constellation.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.material.transition.Hold;
import com.yyzy.constellation.R;
import com.yyzy.constellation.tally.adapter.ChartLvItemAdapter;
import com.yyzy.constellation.tally.util.OnClickSure;
import com.yyzy.constellation.user.bean.LoginInfoBean;

import java.util.List;

public class LoginInfoAdapter extends BaseAdapter {
    private Context context;
    private List<LoginInfoBean> mData;
    private OnClickDel onClick;
    private boolean isInActionMode;

    public LoginInfoAdapter(Context context, List<LoginInfoBean> mData, OnClickDel onClickDel) {
        this.context = context;
        this.mData = mData;
        this.onClick = onClickDel;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_login_info, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        LoginInfoBean bean = mData.get(position);
        holder.tvTime.setText("登录时间：" + bean.getLoginTime());
        holder.tvIpAddress.setText("登录IP：" + bean.getLoginAddress());
        holder.tvDeviceModel.setText("登录设备：" + bean.getDeviceModel());
        holder.tvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginInfoBean bean = mData.get(position);
                onClick.onClickDel(position, bean);
            }
        });
        holder.tvUpdatePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onClickUpdatePwd();
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tvTime, tvDeviceModel, tvIpAddress, tvDel, tvUpdatePwd;

        ViewHolder(View view) {
            tvTime = view.findViewById(R.id.item_login_info_time);
            tvDeviceModel = view.findViewById(R.id.item_login_info_device);
            tvIpAddress = view.findViewById(R.id.item_login_info_ip);
            tvDel = view.findViewById(R.id.item_login_info_tv_del);
            tvUpdatePwd = view.findViewById(R.id.item_login_info_update_pwd);
        }
    }

    public interface OnClickDel {
        void onClickDel(int position, LoginInfoBean bean);
        void onClickUpdatePwd();
    }

}
