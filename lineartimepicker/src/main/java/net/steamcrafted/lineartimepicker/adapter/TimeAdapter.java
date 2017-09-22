package net.steamcrafted.lineartimepicker.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.format.DateFormat;

/**
 * Created by Wannes2 on 9/04/2017.
 */

public class TimeAdapter extends BaseTextAdapter {

    private final String[][] handleText;
    private final String[]   labelText;
    private final boolean is24Hour;

    public TimeAdapter(Context context, Paint textPaint) {
        super(context, textPaint);

        is24Hour = DateFormat.is24HourFormat(context);
        labelText  = new String[getLargePipCount() * (getSmallPipCount()+1)];
        handleText = new String[getLargePipCount() * (getSmallPipCount()+1)][0];

        for(int x = 0; x < getLargePipCount() * (getSmallPipCount()+1); x++){
            handleText[x] = new String[(getSmallPipCount()+1) * (getInvisiblePipCount(x)+1)];
            for(int y = 0; y < (getSmallPipCount()+1) * (getInvisiblePipCount(x)+1); y++){
                handleText[x][y] = createHandleText(x, y);
            }
            labelText[x] = createLabelText(x);
        }
    }

    @Override
    public int getLargePipCount() {
        return 25;
    }

    @Override
    public int getSmallPipCount() {
        return 1;
    }

    @Override
    public int getInvisiblePipCount(int visiblePipIndex) {
        return 5;
    }

    @Override
    public void onDraw(Canvas canvas, Rect[] elementBounds, Gravity gravity) {

    }

    @Override
    protected String getLabelText(int index) {
        return labelText[index];
    }

    @Override
    protected String getHandleText(int index, int step) {
        return handleText[index][step];
    }

    private String createHandleText(int index, int step){
        int hourN = (index / (getSmallPipCount()+1));
        int hour24 = hourN;
        if(!is24Hour){
            hourN %= 12;
            if(hourN == 0) hourN = 12;
        }
        String hour = "" + hourN;
        hour = hour.length() == 1 ? "0" + hour : hour;
        String minutes = ""+((index % (getSmallPipCount()+1)) * (60/(getSmallPipCount()+1)) + step * 5);
        minutes = minutes.length() == 1 ? "0" + minutes : minutes;

        String out = hour + ":" + minutes;
        if(!is24Hour){
            out += hour24 < 12 || hour24 == 24 ? "ᴬᴹ" : "ᴾᴹ";
        }
        return out;
    }

    private String createLabelText(int index){
        if(is24Hour) return "" + index;
        index %= 12;
        if(index == 0) index = 12;
        return "" + index;
    }
}
