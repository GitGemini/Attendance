package com.example.yan.attendance.function;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;
import android.widget.ListView;

/**
 * Created by yan on 2017/3/28.
 */

public class MyExpandableListView extends ExpandableListView {
    public MyExpandableListView(Context context){
        super(context);
    }

    public MyExpandableListView(Context context, AttributeSet attrs){
        super(context, attrs);
    }
    public MyExpandableListView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
