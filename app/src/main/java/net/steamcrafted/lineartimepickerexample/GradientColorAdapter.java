package net.steamcrafted.lineartimepickerexample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import java.util.Arrays;

/**
 * Created by Wannes2 on 10/08/2017.
 */

public class GradientColorAdapter extends ColorAdapter {

    private Drawable[] gradients;

    public GradientColorAdapter(Context context, Paint paint) {
        super(context, paint);

        colors = Arrays.copyOfRange(colors, 0, colors.length);
        gradients = new Drawable[colors.length];

        for(int i = 0; i < gradients.length; i++)
            gradients[i] = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{0xFF000000 | Integer.parseInt(colors[i], 16), 0xFF000000 | Integer.parseInt(colors[(i+1) % colors.length], 16)});
    }

    @Override
    public void onDraw(Canvas canvas, Rect[] elementBounds, Gravity gravity) {
        for(int i = 0; i < elementBounds.length - 1; i++){
            paint.setColor(Color.parseColor("#"+colors[i]));

            Drawable d = gradients[i];

            if(i == 0){
                d.setBounds(0, 0, canvas.getWidth(), elementBounds[0].bottom);
            }
            else if(i == elementBounds.length - 2){
                d.setBounds(0, elementBounds[i-1].bottom, canvas.getWidth(), canvas.getHeight());
            }
            else{
                d.setBounds(0, elementBounds[i-1].bottom, canvas.getWidth(), elementBounds[i].bottom);
            }
            d.draw(canvas);
        }
    }
}
