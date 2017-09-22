package net.steamcrafted.lineartimepickerexample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import net.steamcrafted.lineartimepicker.adapter.BaseTextAdapter;

/**
 * Created by Wannes2 on 11/04/2017.
 */

public class ColorAdapter extends BaseTextAdapter {
    protected String colors[] = new String[]{
            "E51C23",
            "E91E63",
            "9C27B0",
            "673AB7",
            "3F51B5",
            "5677FC",
            "03A9F4",
            "00BCD4",
            "009688",
            "259B24",
            "8BC34A",
            "CDDC39",
            "FFEB3B",
            "FFC107",
            "FF9800",
            "FF5722",
    };

    protected Paint paint = new Paint();

    public ColorAdapter(Context context, Paint paint) {
        super(context, paint);
    }

    @Override
    public int getLargePipCount() {
        return colors.length;
    }

    @Override
    public int getSmallPipCount() {
        return 0;
    }

    @Override
    public int getInvisiblePipCount(int visiblePipIndex) {
        return 0;
    }

    @Override
    public void onDraw(Canvas canvas, Rect[] elementBounds, Gravity gravity) {
        for(int i = 0; i < elementBounds.length - 1; i++){
            paint.setColor(Color.parseColor("#"+colors[i]));

            if(i == 0){
                canvas.drawRect(0, 0, canvas.getWidth(), elementBounds[0].bottom, paint);
            }
            else if(i == elementBounds.length - 2){
                canvas.drawRect(0, elementBounds[i-1].bottom, canvas.getWidth(), canvas.getHeight(), paint);
            }
            else{
                canvas.drawRect(0, elementBounds[i-1].bottom, canvas.getWidth(), elementBounds[i].bottom, paint);
            }
        }
    }

    @Override
    protected String getLabelText(int index) {
        return "";
    }

    @Override
    protected String getHandleText(int index, int step) {
        return colors[index];
    }
}
