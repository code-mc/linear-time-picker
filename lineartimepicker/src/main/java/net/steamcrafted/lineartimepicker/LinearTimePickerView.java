package net.steamcrafted.lineartimepicker;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Wannes2 on 28/09/2016.
 */
public class LinearTimePickerView extends View {


    private enum DragMode { UP, INSIDE, OUTSIDE;}

    private static final int TEXTBOUND_HEIGHT_DP  = 32;

    private static final int TEXTBOUND_PADDING_DP = 6;

    private static final int STRIPE_WIDTH_EXTENDED_DP = 16;

    private static final int STRIPE_WIDTH_DP = 4;
    private static final int STRIPE_HEIGHT_DP = 2;

    private DragMode mDragmode = DragMode.UP;
    private Rect mTimetextBounds = new Rect();
    private Paint mTextBgPaint = new Paint();
    private Paint mTextTextPaint = new Paint();
    private Paint mLinePaint = new Paint();
    private int mTextBoundsHeight;

    private int mTextBoundsPadding;
    private int mStripeWidth;
    private int mStripeWidthExtended;
    private int mStripeHeight;
    private int mDragOffsetY = 0;
    private int mClosestHour = 0;
    private int mMinutes = 0;
    private float mStartX = 0;
    private float mStartY = 0;
    private boolean mValidClick;
    private boolean mUpdateHourValue = true;
    private boolean mInitializePosition = false;

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
        int textBgColor = Color.argb(130, 200, 200, 200);
        int textColor = Color.WHITE;
        int lineColor = Color.argb(130, 200, 200, 200);

        mTextBoundsHeight  = dpToPx(TEXTBOUND_HEIGHT_DP);
        mTextBoundsPadding = dpToPx(TEXTBOUND_PADDING_DP);
        mStripeWidth = dpToPx(STRIPE_WIDTH_DP);
        mStripeWidthExtended = dpToPx(STRIPE_WIDTH_EXTENDED_DP);
        mStripeHeight = dpToPx(STRIPE_HEIGHT_DP);

        mTextBgPaint.setColor(textBgColor);
        mTextBgPaint.setAntiAlias(true);
        mLinePaint.setColor(lineColor);
        mLinePaint.setAntiAlias(true);
        mTextTextPaint.setColor(textColor);
        mTextTextPaint.setTextSize(mTextBoundsHeight - dpToPx(2 * TEXTBOUND_PADDING_DP));
        mTextTextPaint.setAntiAlias(true);
        mTimetextBounds.set(0, 0, getMeasuredWidth(), mTextBoundsHeight);

        mInitializePosition = true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mTimetextBounds.set(mTimetextBounds.left, mTimetextBounds.top, getMeasuredWidth(), mTimetextBounds.top + mTextBoundsHeight);

        if(mInitializePosition){
            mInitializePosition = false;
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    setBoundsForHour(24, 0, false);
                }
            }, 1);
        }
    }

    public int getHour(){
        return mClosestHour / 2;
    }

    public int getMinutes(){
        return mClosestHour % 2 * 30 + mMinutes;
    }

    public void setTextColor(int color){
        mTextTextPaint.setColor(color);
        postInvalidate();
    }

    public void setTextBackgroundColor(int color){
        mTextBgPaint.setColor(color);
        postInvalidate();
    }

    public void setLineColor(int color){
        mLinePaint.setColor(color);
        postInvalidate();
    }

    private int dpToPx(int dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private String getTimeText(){
        int timeMins = getMinutes();
        int timeHrs  = getHour();

        String timeMinsText = String.valueOf(timeMins);
        timeMinsText = (timeMinsText.length() > 1 ? "" : "0") + timeMinsText;
        String timeHrsText  = String.valueOf(timeHrs);
        timeHrsText = (timeHrsText.length() > 1 ? "" : "0") + timeHrsText;

        return timeHrsText + "∶" + timeMinsText;
    }

    private void getTextBounds(Paint paint, String text, Rect bounds) {
        paint.getTextBounds(text, 0, text.length(), bounds);
    }

    private int calculateClosestHourFromBounds(Rect bounds){
        int offsetTop = bounds.height() / 2;
        int offsetBottom = offsetTop;
        int timeLineHeight = getMeasuredHeight() - offsetTop - offsetBottom;
        int climaxY = bounds.centerY();

        float closestDist = Math.max(0, climaxY - offsetTop);
        float hourPerc = closestDist / (float)timeLineHeight;

        return (int)(hourPerc * 48f + .5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Rect b = new Rect();

        // text drawing

        canvas.drawRect(mTimetextBounds, mTextBgPaint);
        getTextBounds(mTextTextPaint, getTimeText(), b);
        int actualTextHeight = (int) mTextTextPaint.getTextSize();
        float textScale = (float) actualTextHeight / (float) b.height();

        mTextTextPaint.setTextSize(mTextTextPaint.getTextSize() * textScale);
        getTextBounds(mTextTextPaint, getTimeText(), b);
        canvas.drawText(getTimeText(), (canvas.getWidth() - b.width()) / 2, mTimetextBounds.bottom - mTextBoundsPadding, mTextTextPaint);
        mTextTextPaint.setTextSize(actualTextHeight);

        // Drawing the timeline

        int offsetTop = mTextBoundsHeight / 2;
        int offsetBottom = offsetTop;
        int timeLineHeight = canvas.getHeight() - offsetTop - offsetBottom;
        int climaxY = mTimetextBounds.centerY();

        final int lines = 24+25;
        float lineOffset = (float) timeLineHeight / (float)(lines-1);

        float lineSpacings[] = new float[lines-1];

        if(mDragmode == DragMode.INSIDE)
            mClosestHour = calculateClosestHourFromBounds(mTimetextBounds);

        for(int i = 0; i < lineSpacings.length; i++){
            float x1 = (float)i / (float)lineSpacings.length;
            float x2 = (float)(i+1) / (float)lineSpacings.length;
            float area = calculateAreaUnderneath(x1, x2, (float)(climaxY - offsetTop) / (float)timeLineHeight);
            lineSpacings[i] = timeLineHeight * area;
        }

        float spacingTop = 0;
        for(int i = 0; i < lines; i++){
            if(i > 0) spacingTop += lineSpacings[i-1];

            boolean closest = i == mClosestHour;

            int exactY = (int) (lineOffset * i + offsetTop);
            int yoffset = Math.abs(climaxY - exactY);
            float yoffsetPerc = Math.min(1f, (float)yoffset / (lineOffset * 6));
            float linewidth = (1f - yoffsetPerc) * (mStripeWidthExtended - mStripeWidth) + mStripeWidth;

            int drawY = offsetTop + (int) (spacingTop - mStripeHeight/2);
            int drawYCenter = offsetTop + (int)spacingTop;

            if(i % 2 == 0 || yoffsetPerc < 1f){
                int color = mLinePaint.getColor();

                if(i % 2 != 0 && yoffsetPerc < 1f){
                    int alpha = (int) (Color.alpha(color) * (1f - yoffsetPerc));
                    mLinePaint.setColor(Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color)));
                    linewidth /= 2;
                }
                canvas.drawRect(0, drawY, linewidth, drawY + mStripeHeight, closest ? mTextTextPaint : mLinePaint);

                mLinePaint.setColor(color);
            }

            if(i % 2 == 0)
            {
                float baseTextSize = lineOffset;
                String text = "" + (i/2);
                if(yoffsetPerc < 1f){
                    int color = mLinePaint.getColor();
                    float size = mTextTextPaint.getTextSize();

                    if(i % 6 != 0){
                        int alpha = (int) (Color.alpha(color) * (1f - yoffsetPerc));
                        mLinePaint.setColor(Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color)));
                    }
                    mLinePaint.setTextSize(baseTextSize * (2f - yoffsetPerc));
                    mTextTextPaint.setTextSize(baseTextSize * (2f - yoffsetPerc));

                    getTextBounds(mLinePaint, text, b);
                    canvas.drawText(text, linewidth + mTextBoundsPadding, drawYCenter + b.height()/2,
                            closest ? mTextTextPaint : mLinePaint);

                    mTextTextPaint.setTextSize(size);
                    mLinePaint.setColor(color);
                }
                else if(i % 6 == 0){
                    mLinePaint.setTextSize(baseTextSize);
                    getTextBounds(mLinePaint, text, b);
                    canvas.drawText(text, linewidth + mTextBoundsPadding, drawYCenter + b.height()/2, mLinePaint);
                }
            }
        }
    }

    private float calculateAreaUnderneath(float x1, float x2, float middle) {
        if(x1 > x2) return calculateAreaUnderneath(x2, x1, middle);

        if(x1 <= middle && x2 <= middle){
            float offsetY = .3f;

            float rico = (2f - (offsetY * 2)) / middle;
            float y1 = rico * x1 + offsetY;
            float y2 = rico * x2 + offsetY;

            float area = (y1 + y2) / 2f * (x2 - x1);
            return area;
        }
        else if(x1 >= middle && x2 >= middle){
            return calculateAreaUnderneath(1f - x1, 1f - x2, 1f - middle); // mirror the coords and re-use the formula above
        }
        else{
            return calculateAreaUnderneath(x1, middle, middle) + calculateAreaUnderneath(middle, x2, middle);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int ac  = event.getAction();
        float x = event.getX();
        float y = event.getY();

        if(ac == MotionEvent.ACTION_DOWN){
            mStartX = x;
            mStartY = y;
            mValidClick = true;

            if(mTimetextBounds.contains((int)x, (int)y)){
                mDragmode = DragMode.INSIDE;
                mMinutes = 0;

                mDragOffsetY = (int) (y - mTimetextBounds.top);
            }
            else{
                mDragmode = DragMode.OUTSIDE;
            }

            postInvalidate();

            return true;
        }
        else
        if(ac == MotionEvent.ACTION_MOVE){
            if(mDragmode == DragMode.INSIDE){
                int top = (int) (y - mDragOffsetY);
                top = Math.max(0, top);
                top = Math.min(getMeasuredHeight() - mTextBoundsHeight, top);
                mTimetextBounds.set(mTimetextBounds.left, top, mTimetextBounds.right, top + mTextBoundsHeight);

                postInvalidate();
            }
            else if(mDragmode == DragMode.OUTSIDE && Math.sqrt((x - mStartX) * (x - mStartX) + (y - mStartY) * (y - mStartY)) > 50){
                mValidClick = false;
            }

            return true;
        }
        else
        if(ac == MotionEvent.ACTION_UP){
            if(mDragmode == DragMode.OUTSIDE && mValidClick){
                handleClick(mStartX, mStartY);
            }
            else if(mDragmode == DragMode.INSIDE){
                mClosestHour = calculateClosestHourFromBounds(mTimetextBounds);
                setBoundsForHour(mClosestHour);
            }

            mDragmode = DragMode.UP;

            postInvalidate();

            return true;
        }

        return super.onTouchEvent(event);
    }

    private void handleClick(float x, float y) {
        if(y < mTimetextBounds.centerY()){
            if(mMinutes >= 5){
                mMinutes -= 5;
                setBoundsForHour(mClosestHour, mMinutes);
            }else{
                mMinutes = 25;
                setBoundsForHour(mClosestHour - 1, mMinutes);
            }
        }
        else{
            if(mMinutes < 25){
                mMinutes += 5;
                setBoundsForHour(mClosestHour, mMinutes);
            }else{
                mMinutes = 0;
                setBoundsForHour(mClosestHour + 1, mMinutes);
            }
        }
    }

    private void setBoundsForHour(int hour){
        setBoundsForHour(hour, 0, true);
    }

    private void setBoundsForHour(int hour, int minutes){
        setBoundsForHour(hour, minutes, true);
    }

    private void setBoundsForHour(int hour, int minutes, boolean animate) {
        if(hour < 0 || hour > 48) return;

        int offsetTop = mTextBoundsHeight / 2;
        int offsetBottom = offsetTop;
        int timeLineHeight = getMeasuredHeight() - offsetTop - offsetBottom;
        float spaceHeight = (float) timeLineHeight / 48f;

        float minoffset = (minutes / 30f) * spaceHeight;

        mClosestHour = hour;

        float top = (float)hour * spaceHeight + minoffset;

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB && animate) {
            ValueAnimator anim = ValueAnimator.ofFloat(mTimetextBounds.top, top);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float top = (float)valueAnimator.getAnimatedValue();

                    mTimetextBounds.set(mTimetextBounds.left, (int) top, mTimetextBounds.right, (int) top + mTextBoundsHeight);
                    postInvalidate();
                }
            });
            anim.setDuration((long) (300f * Math.abs(top - mTimetextBounds.top) / spaceHeight));
            anim.start();
        }else{
            mTimetextBounds.set(mTimetextBounds.left, (int) top, mTimetextBounds.right, (int) top + mTextBoundsHeight);
            postInvalidate();
        }
    }
}
