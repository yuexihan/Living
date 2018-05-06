package com.tencent.living;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class MyListView  extends ListView{

    private  boolean autoMeasure;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (autoMeasure) {
            int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                    MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);
        }else
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    public void setAutoMeasure(boolean autoMeasure){
        this.autoMeasure = autoMeasure;
    }
    public MyListView(Context context) {
        super(context);
        autoMeasure = false;
    }

    public  MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public  MyListView(Context context, AttributeSet attrs,
                            int defStyle) {
        super(context, attrs, defStyle);

    }
}
