package com.example.yan.attendance.Adapter;

import android.content.Context;

import com.example.yan.attendance.Db.searchData;
import com.example.yan.attendance.R;

import java.util.List;

/**
 * Created by yan on 2017/10/29.
 */

public class SearchAdapter extends CommonAdapter<searchData> {
    public SearchAdapter(Context context, List<searchData> data, int layoutId){
        super(context, data, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, int position) {
        holder.setImageResource(R.id.item_search_iv_icon,mData.get(position).getIconId())
                .setText(R.id.item_search_tv_title,mData.get(position).getTitle())
                .setText(R.id.item_search_tv_content,mData.get(position).getContent())
                .setText(R.id.item_search_tv_comments,mData.get(position).getComments());
    }
}

