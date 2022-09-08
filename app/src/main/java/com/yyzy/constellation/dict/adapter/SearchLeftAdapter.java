package com.yyzy.constellation.dict.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.yyzy.constellation.R;
import com.yyzy.constellation.dict.entity.PinBuEntity;
import com.yyzy.constellation.utils.CommonUtils;


import java.util.List;

import static com.yyzy.constellation.R.color.blue;
import static com.yyzy.constellation.R.color.yellow_300;

public class SearchLeftAdapter extends BaseExpandableListAdapter {
    private Context context;
    //表示分组的列表
    private List<String> groupDatas;
    //将每组对应的子类列表存放到这个集合
    private List<List<PinBuEntity.ResultDTO>> childDatas;
    private LayoutInflater inflater;
    //表示被选中组的位置、被选中的组里面item的位置
    private int selectGroupPos = 0, selectChildPos = 0;
    //因为拼音和部首都用此适配器，所以要通过这个属性，进行类型区分
    private int type;

    public void setSelectChildPos(int selectChildPos) {
        this.selectChildPos = selectChildPos;
    }

    public void setSelectGroupPos(int selectGroupPos) {
        this.selectGroupPos = selectGroupPos;
    }

    public SearchLeftAdapter(Context context, List<String> groupDatas, List<List<PinBuEntity.ResultDTO>> childDatas, int type){
        this.context = context;
        this.groupDatas = groupDatas;
        this.childDatas = childDatas;
        this.type = type;
        inflater = LayoutInflater.from(context);    //初始化布局加载器
    }

    //获取分组的数量
    @Override
    public int getGroupCount() {
        return groupDatas.size();
    }

    //获取指定分组当中有几个item
    @Override
    public int getChildrenCount(int groupPosition) {
        return childDatas.get(groupPosition).size();
    }

    //获取分组指定位置的数据
    @Override
    public Object getGroup(int groupPosition) {
        return groupDatas.get(groupPosition);
    }

    //给出第几组第几个，求出指定位置的对象
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childDatas.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_exlistview_group,null);
            holder = new GroupViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        //获取指定位置的数据
        String data = groupDatas.get(groupPosition);
        if (type == CommonUtils.TYPE_PINYIN) {
            holder.groupTv.setText(data);
        }else {
            holder.groupTv.setText(data+"画");
        }

        //因为选中位置会改变颜色，和其他布局颜色不一样，所以判断一下，位置是否选中
        if (selectGroupPos == groupPosition){
            convertView.setBackgroundColor(Color.parseColor("#6495ED"));
            holder.groupTv.setTextColor(Color.parseColor("#FF9800"));
        }else {
            convertView.setBackgroundResource(R.color.yellow_100);
            holder.groupTv.setTextColor(Color.BLACK);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_exlistview_child,null);
            holder = new ChildViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        PinBuEntity.ResultDTO entity = childDatas.get(groupPosition).get(childPosition);
        if (type == CommonUtils.TYPE_PINYIN) {
            holder.childTv.setText(entity.getPinyin());
        }else {
            holder.childTv.setText(entity.getBushou());
        }
        //设置改变选中项目的颜色
        if (selectGroupPos == groupPosition && selectChildPos == childPosition) {
            convertView.setBackgroundColor(Color.parseColor("#95B6F1"));
            holder.childTv.setTextColor(Color.parseColor("#FF9800"));
        }else {
            convertView.setBackgroundResource(R.color.yellow_100);
            holder.childTv.setTextColor(Color.BLACK);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupViewHolder{
        TextView groupTv;
        public GroupViewHolder(View view){
            groupTv = view.findViewById(R.id.item_group_tv);
        }
    }

    class ChildViewHolder{
        TextView childTv;
        public ChildViewHolder(View view){
            childTv = view.findViewById(R.id.item_child_tv);
        }
    }
}
