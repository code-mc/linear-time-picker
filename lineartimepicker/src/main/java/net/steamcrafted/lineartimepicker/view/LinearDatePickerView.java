package net.steamcrafted.lineartimepicker.view;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;

import net.steamcrafted.lineartimepicker.adapter.DateAdapter;

/**
 * Created by Wannes2 on 28/09/2016.
 */
public class LinearDatePickerView extends LinearPickerView {
    private Paint mTextPaint = new Paint();
    private DateAdapter mAdapter;

    public LinearDatePickerView(Context context) {
        super(context);

        init(null);
    }

    public LinearDatePickerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public LinearDatePickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    private void init(AttributeSet attrs){
        mTextPaint.setAntiAlias(true);
        mAdapter = new DateAdapter(getContext(), mTextPaint);
        setAdapter(mAdapter);
        setTutorialText("previous day", "next day");
    }

    public int getMonth(){
        return mAdapter.getMonth(getIndex());
    }

    public int getDay(){
        return mAdapter.getDay(getIndex(), getInvisibleStep());
    }

    @Override
    public void setActiveLineColor(int color) {
        mTextPaint.setColor(color);
        super.setActiveLineColor(color);
    }
}
