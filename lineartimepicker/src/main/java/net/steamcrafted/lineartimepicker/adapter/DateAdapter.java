package net.steamcrafted.lineartimepicker.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Wannes2 on 9/04/2017.
 */

public class DateAdapter extends BaseTextAdapter {

    public DateAdapter(Context context, Paint textPaint) {
        super(context, textPaint);
    }

    @Override
    public int getLargePipCount() {
        return 13;
    }

    @Override
    public int getSmallPipCount() {
        return 3;
    }

    @Override
    public int getInvisiblePipCount(int visiblePipIndex) {
        int month = visiblePipIndex / (getSmallPipCount()+1);
        int part = visiblePipIndex % (getSmallPipCount()+1);
        if(part == getSmallPipCount()){
            return (getDaysInMonth(month) % 8) - 1;
        }
        return 7;
    }

    @Override
    public void onDraw(Canvas canvas, Rect[] elementBounds, Gravity gravity) {

    }

    @Override
    protected String getLabelText(int index) {
        switch (index % 12){
            case 0: return "JAN";
            case 1: return "FEB";
            case 2: return "MAR";
            case 3: return "APR";
            case 4: return "MAY";
            case 5: return "JUN";
            case 6: return "JUL";
            case 7: return "AUG";
            case 8: return "SEP";
            case 9: return "OCT";
            case 10: return "NOV";
            case 11:
            default: return "DEC";
        }
    }

    @Override
    protected String getHandleText(int index, int step) {
        return getLabelText(getMonthIndex(index)) + " " + getDay(index, step);
    }

    private int getMonthIndex(int index){
        return index / (getSmallPipCount()+1);
    }

    public int getMonth(int index){
        return getMonthIndex(index) + 1;
    }

    public int getDay(int index, int step){
        int days = getDaysInMonth(getMonthIndex(index));
        return Math.min((index % (getSmallPipCount()+1) * 8 + step + 1), days);
    }

    private int getDaysInMonth(int month) {
        switch (month % 12){
            case 1: return 28;
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11: return 31;
            default: return 30;
        }
    }

    private String getDaySuffix(int day){
        if(day < 10 || day > 19){
            switch (day % 10){
                case 1: return "st";
                case 2: return "nd";
                case 3: return "rd";
                default: return "th";
            }
        }
        return "th";
    }
}
