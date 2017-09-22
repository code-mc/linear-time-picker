package net.steamcrafted.lineartimepicker.adapter;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by Wannes2 on 19/03/2017.
 */

public interface LinearPickerAdapter {

    enum Gravity {TOP, BOTTOM, LEFT, RIGHT}
    enum ScreenHalf {NONE, LEFT, RIGHT}

    int getLargePipCount();
    int getSmallPipCount();
    int getInvisiblePipCount(int visiblePipIndex);
    void onDraw(Canvas canvas, Rect[] elementBounds, Gravity gravity);
    void onDrawElement(int index, Canvas canvas, Rect bounds, float yOffset, Gravity gravity);
    void onDrawHandle(int index, int intermediate, Canvas canvas, Rect bounds, Gravity gravity, ScreenHalf occluded);
}
