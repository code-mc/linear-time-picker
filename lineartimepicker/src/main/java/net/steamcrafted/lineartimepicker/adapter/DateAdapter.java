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
            case 0: return "Jan";
            case 1: return "Feb";
            case 2: return "Mar";
            case 3: return "Apr";
            case 4: return "May";
            case 5: return "Jun";
            case 6: return "Jul";
            case 7: return "Aug";
            case 8: return "Sep";
            case 9: return "Oct";
            case 10: return "Nov";
            case 11:
            default: return "Dec";
        }
    }

    @Override
    protected String getHandleText(int index, int step) {
        int month = index / (getSmallPipCount()+1);
        int days = getDaysInMonth(month);
        int offset = Math.min((index % (getSmallPipCount()+1) * 8 + step + 1), days);
        return getLabelText(month) + " " + offset;
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
