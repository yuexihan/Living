package com.tencent.living;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class TextProgressBar extends ProgressBar
{
    private String str = "心情";
    private Paint mPaint;

    public TextProgressBar(Context context)
    {
        super(context);
        initText();
    }

    public TextProgressBar(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        initText();
    }

    public TextProgressBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        initText();
    }

    @Override
    public void setProgress(int progress)
    {
        super.setProgress(progress);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        Rect rect = new Rect();
        this.mPaint.setTextSize(getHeight() - 5);
        this.mPaint.getTextBounds(this.str, 0, this.str.length(), rect);
        int x = (getWidth() / 2) - rect.centerX();// 让现实的字体处于中心位置;;
        int y = getHeight() + 1;// 让显示的字体处于中心位置;;
        canvas.drawText(this.str, x, y, this.mPaint);
    }

    // 初始化，画笔
    private void initText()
    {
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);// 设置抗锯齿;;;;
        this.mPaint.setColor(Color.BLACK);
    }

    // 设置文字内容
    private void setText(String text)
    {
        this.str = text;
    }
}