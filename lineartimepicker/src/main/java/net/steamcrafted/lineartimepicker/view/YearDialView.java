package net.steamcrafted.lineartimepicker.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;

import net.steamcrafted.lineartimepicker.R;
import net.steamcrafted.lineartimepicker.Utils;

/**
 * Created by Wannes2 on 23/09/2017.
 */

public class YearDialView extends HorizontalScrollView {

    private int mMinYear = 1900;
    private int mMaxYear = 3000;
    private int mYearTextWidth;

    private View mInnerView;
    private Paint mTextPaint;
    private final Rect r = new Rect();
    private boolean fingerDown = false;

    public YearDialView(Context context) {
        super(context);

        init(null);
    }

    public YearDialView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public YearDialView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    private void init(AttributeSet attrs){
        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(false);

        FrameLayout container = new FrameLayout(getContext());
        mInnerView = new View(getContext());

        container.addView(mInnerView, new LayoutParams(5000, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(container, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

        float yearTextSize = getResources().getDimensionPixelSize(R.dimen.ltp_dialog_year_text_size);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(yearTextSize);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        Utils.getTextBounds(mTextPaint, "0000", r);
        mYearTextWidth = r.width();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        updateInnerView();
    }

    private void updateInnerView() {
        mInnerView.getLayoutParams().width = mYearTextWidth * getCount() + (getMeasuredWidth() - mYearTextWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.getClipBounds(r);
        int canvasCenterX = r.centerX();
        int canvasCenterY = r.centerY();

        int year = getCenteredYear();
        String text = ""+year;
        Utils.getTextBounds(mTextPaint, text, r);

        float offsetProgress = getCenterXOffset() / (float)mYearTextWidth;
        float alphaProgress = (1f - Math.abs(2*offsetProgress - 1f));
        int alpha = (int) (alphaProgress * 127 + 128);
        mTextPaint.setAlpha(alpha);

        {
            float x = canvasCenterX - r.width() + (1f-offsetProgress) * mYearTextWidth;
            float y = canvasCenterY + r.height() / 2;

            float scale = .75f + .25f * alphaProgress;
            canvas.save();
            canvas.scale(scale, scale, x + mYearTextWidth/2, y - r.height()/2);
            canvas.drawText(text, x, y, mTextPaint);
            canvas.restore();
        }

        if(year > mMinYear){
            String prevText = "" + (year-1);
            Utils.getTextBounds(mTextPaint, prevText, r);
            mTextPaint.setAlpha((int) ((1f-offsetProgress) * 128));

            float x = canvasCenterX - r.width() + (-offsetProgress) * mYearTextWidth;
            float y = canvasCenterY + r.height() / 2;
            float scale = .75f - .25f * offsetProgress;
            canvas.save();
            canvas.scale(scale, scale, x + mYearTextWidth/2, y - r.height()/2);
            canvas.drawText(prevText, x, y, mTextPaint);
            canvas.restore();
        }

        if(year < mMaxYear){
            String nextText = "" + (year+1);
            Utils.getTextBounds(mTextPaint, nextText, r);
            mTextPaint.setAlpha((int) ((offsetProgress) * 128));

            float x = canvasCenterX - r.width() + (2f-offsetProgress) * mYearTextWidth;
            float y = canvasCenterY + r.height() / 2;
            float scale = .5f + .25f * offsetProgress;
            canvas.save();
            canvas.scale(scale, scale, x + mYearTextWidth/2, y - r.height()/2);
            canvas.drawText(nextText, x, y, mTextPaint);
            canvas.restore();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                fingerDown = true;
                break;
            case MotionEvent.ACTION_UP:
                fingerDown = false;
                centerToYear();
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void centerToYear() {
        final int x = getScrollX();
        postDelayed(new Runnable() {
            @Override
            public void run() {
               if(x == getScrollX()){
                   int scrollX =  - getCenterXOffset() + mYearTextWidth/2;
                   ValueAnimator anim = ValueAnimator.ofInt(x, x + scrollX)
                           .setDuration(150);
                   anim.setInterpolator(new AccelerateDecelerateInterpolator());
                   anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                       @Override
                       public void onAnimationUpdate(ValueAnimator animation) {
                           scrollTo((int) animation.getAnimatedValue(), 0);
                       }
                   });
                   anim.start();
               }
            }
        }, 50);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if(Math.abs(l - oldl) <= 2 && !fingerDown){
            Log.d("dsf", "oldx: " + oldl + " nx: " + l + " finger: " + fingerDown);
            centerToYear();
        }
    }

    private int getCount(){
        return mMaxYear - mMinYear + 1; // inclusive on lower and upper bound
    }

    private int getCenteredYear(){
        int padding = (getMeasuredWidth() - mYearTextWidth) / 2;
        int xCenter = getScrollX() + getMeasuredWidth() / 2 - padding;
        int pos = xCenter / mYearTextWidth;
        return mMinYear + pos;
    }

    private int getCenterXOffset(){
        int padding = (getMeasuredWidth() - mYearTextWidth) / 2;
        int xCenter = getScrollX() + getMeasuredWidth() / 2 - padding;
        int pos = xCenter / mYearTextWidth;
        return xCenter - pos * mYearTextWidth;
    }

    public int getSelectedYear(){
        return getCenteredYear();
    }

    public void setSelectedYear(final int year){
        postDelayed(new Runnable() {
            @Override
            public void run() {
                int yearOffset = year - mMinYear;
                if(yearOffset >= 0){
                    int x = yearOffset * mYearTextWidth;
                    scrollTo(x, 0);
                }
            }
        }, 1);
    }

    public void setMaxYear(int year){
        mMaxYear = year;
        updateInnerView();
    }

    public void setMinYear(int year){
        mMinYear = year;
        updateInnerView();
    }
}
