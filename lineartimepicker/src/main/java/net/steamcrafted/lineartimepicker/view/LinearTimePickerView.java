package net.steamcrafted.lineartimepicker.view;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;

import net.steamcrafted.lineartimepicker.adapter.TimeAdapter;

/**
 * Created by Wannes2 on 28/09/2016.
 */
public class LinearTimePickerView extends LinearPickerView {
    private Paint mTextPaint = new Paint();

    public LinearTimePickerView(Context context) {
        super(context);

        init(null);
    }

    public LinearTimePickerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public LinearTimePickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    private void init(AttributeSet attrs){
        mTextPaint.setAntiAlias(true);
        setAdapter(new TimeAdapter(getContext(), mTextPaint));
        setTutorialText("-5 minutes", "+5 minutes");
    }

    public int getHour(){
        return getIndex() / 2;
    }

    public int getMinutes(){
        return getIndex() % 2 * 30 + getInvisibleStep() * 5;
    }

    @Override
    public void setActiveLineColor(int color) {
        mTextPaint.setColor(color);
        super.setActiveLineColor(color);
    }
}
