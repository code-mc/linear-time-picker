package net.steamcrafted.lineartimepicker.adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;

import net.steamcrafted.lineartimepicker.Utils;

/**
 * Created by Wannes2 on 9/04/2017.
 */

public abstract class BaseTextAdapter implements LinearPickerAdapter {
    final Rect r = new Rect();
    final Paint bgPaint = new Paint();
    private final Paint mTextPaint;

    final int minTextSize;
    final int maxTextSize;
    final Context mContext;

    private final ValueAnimator mOcclusionProgress = ValueAnimator.ofFloat(0f, 1f);
    private ScreenHalf mPrevOcclusion = ScreenHalf.NONE;
    private float mPrevOffset, mFromOffset;
    private float mPrevScale = 1, mFromScale = 1;
    private final int mSmallSections = getSmallPipCount() + 1;

    protected abstract String getLabelText(int index);
    protected abstract String getHandleText(int index, int step);

    public BaseTextAdapter(Context context, Paint textPaint) {
        minTextSize = Utils.dpToPx(context, 8);
        maxTextSize = Utils.dpToPx(context, 24);
        mContext = context;
        mTextPaint = textPaint;

        mOcclusionProgress.setDuration(300);
        mOcclusionProgress.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    @Override
    public void onDrawElement(int index, Canvas canvas, Rect bounds, float yoff, Gravity gravity) {
        //int textSize = Math.min(maxTextSize, Math.max(bounds.height(), minTextSize));
        int textSize = (int) Math.max(bounds.height() * .5, minTextSize);
        int alpha = 255;

        bgPaint.setAlpha(100);
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setStrokeWidth(5);
        //canvas.drawRect(bounds, bgPaint);
        bgPaint.setAlpha(255);

        mTextPaint.setTextSize(textSize);
        bgPaint.setColor(Color.BLACK);

        mTextPaint.setAlpha(255);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        bgPaint.setAlpha(255);

        if(index % (3 * mSmallSections) == 0){
            alpha = 255;
        }
        else if(index % mSmallSections == 0 && yoff < 1f){
            alpha = (int) (255 * (1f - yoff));
        }else{
            alpha = 0;
        }

        mTextPaint.setAlpha(alpha);
        bgPaint.setAlpha(alpha);

        int bigIndex = index / mSmallSections;
        Utils.getTextBounds(mTextPaint, getLabelText(bigIndex), r);
        //canvas.drawRect(bounds, bgPaint);
        canvas.drawText(getLabelText(bigIndex), bounds.left, bounds.top + bounds.height() / 2 + r.height() / 2, mTextPaint);
    }

    @Override
    public void onDrawHandle(int index, int intermediate, Canvas canvas, Rect bounds, Gravity gravity, ScreenHalf occluded) {
        float textsize = bounds.height() * .75f;
        mTextPaint.setTextSize(textsize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setAlpha(255);

        String data = getHandleText(index, intermediate);
        Utils.getTextBounds(mTextPaint, data, r);

        canvas.save();

        if(mPrevOcclusion != occluded){
            mFromOffset = mPrevOffset;
            mFromScale  = mPrevScale;

            if(occluded == ScreenHalf.NONE){
                // reverse the animation
            }else{

            }

            mOcclusionProgress.setCurrentPlayTime(0);
            mOcclusionProgress.start();
        }

        mPrevOcclusion = occluded;
        float fraction = (float) mOcclusionProgress.getAnimatedValue();

        if(occluded == ScreenHalf.LEFT){
            mPrevOffset = mFromOffset * (1f-fraction) + bounds.width()/6f * fraction;
            canvas.translate(mPrevOffset, 0f);
            mPrevScale = (mFromScale * (1f-fraction)) + (.9f * fraction);
            canvas.scale(mPrevScale, mPrevScale, bounds.centerX(), bounds.centerY());
        }else if(occluded == ScreenHalf.RIGHT){
            mPrevOffset = mFromOffset * (1f-fraction) - bounds.width()/6f * fraction;
            canvas.translate(mPrevOffset, 0f);
            mPrevScale = (mFromScale * (1f-fraction)) + (.9f * fraction);
            canvas.scale(mPrevScale, mPrevScale, bounds.centerX(), bounds.centerY());
        }else{
            mPrevOffset = mFromOffset * (1f-fraction);
            canvas.translate(mPrevOffset, 0f);
            mPrevScale  = mFromScale * (1f-fraction) + fraction;
            canvas.scale(mPrevScale, mPrevScale, bounds.centerX(), bounds.centerY());
        }

        canvas.drawText(data, bounds.left + bounds.width()/2, bounds.bottom - (bounds.height() - r.height())/2, mTextPaint);

        canvas.restore();
    }
}
