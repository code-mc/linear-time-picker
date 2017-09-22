package net.steamcrafted.lineartimepicker.view;

import android.animation.Animator;
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

import net.steamcrafted.lineartimepicker.adapter.LinearPickerAdapter;
import net.steamcrafted.lineartimepicker.Utils;

/**
 * Created by Wannes2 on 28/09/2016.
 */
public class LinearPickerView extends View {

    private enum DragMode { UP, INSIDE, OUTSIDE;}

    private static final int TEXTBOUND_HEIGHT_DP  = 32;

    private static final int TEXTBOUND_PADDING_DP = 6;

    private static final int STRIPE_WIDTH_EXTENDED_DP = 16;

    private static final int STRIPE_WIDTH_DP = 4;
    private static final int STRIPE_HEIGHT_DP = 2;

    private static final int CONTENT_WIDTH_SMALL = 48;

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
    private int mContentWidthSmall;
    private int mDragOffsetY = 0;
    private int mClosestIndex = 0;
    private int mInvisibleStep = 0;
    private float mStartX = 0;
    private float mStartY = 0;
    private boolean mValidClick;
    private boolean mUpdateHourValue = true;
    private boolean mInitializePosition = false;
    private boolean mAnimating = false;
    private boolean mOutsideDragEnabled = false;
    private int mHandleOffset;
    private LinearPickerAdapter.ScreenHalf mOccluded = LinearPickerAdapter.ScreenHalf.NONE;
    private long mOccludedChanged = 0;

    private LinearPickerAdapter mAdapter = new LinearPickerAdapter() {
        @Override
        public int getLargePipCount() {
            return 1;
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

        }

        @Override
        public void onDrawElement(int index, Canvas canvas, Rect bounds, float yOffset, Gravity gravity) {

        }

        @Override
        public void onDrawHandle(int index, int intermediate, Canvas canvas, Rect bounds, Gravity gravity, ScreenHalf occluded) {

        }
    };

    public LinearPickerView(Context context) {
        super(context);

        init(null);
    }

    public LinearPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public LinearPickerView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        mContentWidthSmall = dpToPx(CONTENT_WIDTH_SMALL);

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

    public void setAdapter(LinearPickerAdapter adapter){
        mAdapter = adapter;
        smallPipCache = mAdapter.getSmallPipCount() + 1;
        postInvalidate();
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
                    setBoundsForHour(getTotalPositions()/2, 0, false);
                }
            }, 1);
        }
    }

    public void setHandleBackgroundColor(int color){
        mTextBgPaint.setColor(color);
        postInvalidate();
    }

    public void setLineColor(int color){
        mLinePaint.setColor(color);
        postInvalidate();
    }

    public void setActiveLineColor(int color){
        mTextTextPaint.setColor(color);
        postInvalidate();
    }

    public int getIndex(){
        return mClosestIndex;
    }

    public int getInvisibleStep(){
        return mInvisibleStep;
    }

    protected int dpToPx(int dp){
        return Utils.dpToPx(getContext(), dp);
    }

    private int calculateClosestHourFromBounds(Rect bounds){
        int offsetTop = bounds.height() / 2;
        int offsetBottom = offsetTop;
        int timeLineHeight = getMeasuredHeight() - offsetTop - offsetBottom;
        int climaxY = bounds.centerY();

        float closestDist = Math.max(0, climaxY - offsetTop);
        float hourPerc = closestDist / (float)timeLineHeight;

        return (int)(hourPerc * getTotalPositions() + .5f);
    }

    private int getTotalPositions(){
        return (mAdapter.getLargePipCount() - 1) * getAdjustedSmallPipCount();
    }

    static class Element {
        public final Rect bounds;
        public float yOffsetPerc;

        public Element(Rect bounds, float yOffsetPerc) {
            this.bounds = bounds;
            this.yOffsetPerc = yOffsetPerc;
        }
    }

    private final Rect mRect = new Rect();
    private Element mElements[] = new Element[0];
    private Rect mBounds[] = new Rect[0];
    private float mLineSpacings[] = new float[0];

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int offset = mHandleOffset;
        if(offset + mTimetextBounds.bottom > canvas.getHeight()){
            offset = canvas.getHeight() - mTimetextBounds.bottom;
        }
        else if(offset + mTimetextBounds.top < 0){
            offset = -mTimetextBounds.top;
        }

        mTimetextBounds.set(
                mTimetextBounds.left, mTimetextBounds.top + offset,
                mTimetextBounds.right, mTimetextBounds.bottom + offset
        );

        // text drawing
        int actualTextHeight = (int) mTextTextPaint.getTextSize();

        mTextTextPaint.setTextSize(actualTextHeight);

        // Drawing the timeline

        int offsetTop = mTextBoundsHeight / 2;
        int offsetBottom = offsetTop;
        int timeLineHeight = canvas.getHeight() - offsetTop - offsetBottom;
        int climaxY = mTimetextBounds.centerY();
        final int stepC = mAdapter.getSmallPipCount() + 1;

        final int lines = getTotalPositions() + 1;
        float lineOffset = (float) timeLineHeight / (float)(lines-1);

        if(mLineSpacings.length != lines - 1) mLineSpacings  = new float[lines - 1];
        if(mElements.length != lines / getAdjustedSmallPipCount() + 1){
            mElements = new Element[lines / getAdjustedSmallPipCount() + 1];
            mBounds = new Rect[lines / getAdjustedSmallPipCount() + 1];
            for(int i = 0; i < mElements.length; i++) mElements[i] = new Element(new Rect(), 0);
        }

        if(mDragmode == DragMode.INSIDE)
            mClosestIndex = calculateClosestHourFromBounds(mTimetextBounds);

        for(int i = 0; i < mLineSpacings.length; i++){
            float x1 = (float)i / (float) mLineSpacings.length;
            float x2 = (float)(i+1) / (float) mLineSpacings.length;
            float area = calculateAreaUnderneath(x1, x2, (float)(climaxY - offsetTop) / (float)timeLineHeight);
            mLineSpacings[i] = timeLineHeight * area;
        }

        float spacingTop = 0;
        for(int i = 0; i < lines; i++){
            if(i > 0) spacingTop += mLineSpacings[i-1];

            int exactY = (int) (lineOffset * i + offsetTop);
            int yoffset = Math.abs(climaxY - exactY);
            float yoffsetPerc = Math.min(1f, (float)yoffset / (lineOffset * 6));
            float linewidth = (1f - yoffsetPerc) * (mStripeWidthExtended - mStripeWidth) + mStripeWidth;

            int drawYCenter = offsetTop + (int)spacingTop;

            if(i % stepC == 0 || yoffsetPerc < 1f) {
                if (i % stepC != 0 && yoffsetPerc < 1f) {
                    linewidth /= 2;
                }

                if(i % getAdjustedSmallPipCount() == 0){
                    float rectTop = 0;
                    float rectBottom = 0;

                    final int stepOffset = (int) Math.round(getAdjustedSmallPipCount() / 2.0);
                    final boolean halfOnEdges = getAdjustedSmallPipCount() % 2 == 1;
                    final int loopStart = Math.max(0, i - stepOffset);
                    final int loopEnd = Math.min(mLineSpacings.length, i + stepOffset);

                    for (int spacingIndex = loopStart; spacingIndex < loopEnd; spacingIndex++) {
                        float diff = mLineSpacings[spacingIndex];
                        if (halfOnEdges && (spacingIndex == loopStart || spacingIndex == loopEnd - 1))
                            diff /= 2;

                        if (spacingIndex < i)
                            rectTop += diff;
                        else
                            rectBottom += diff;
                    }

                    if (i == 0) rectTop = rectBottom;
                    if (i == lines - 1) rectBottom = rectTop;

                    int spacing = (int) Math.min(rectBottom, rectTop);

                    mElements[i / getAdjustedSmallPipCount()].bounds.set(
                            (int) (linewidth + mTextBoundsPadding),
                            (int) (drawYCenter - spacing),
                            (int) (linewidth + mTextBoundsPadding + mContentWidthSmall),
                            (int) (drawYCenter + spacing));
                    mElements[i / getAdjustedSmallPipCount()].yOffsetPerc = yoffsetPerc;
                }
            }
        }

        for(int i = 0; i < mElements.length; i++) mBounds[i] = mElements[i].bounds;
        // let the adapter draw a custom background
        mAdapter.onDraw(canvas, mBounds, LinearPickerAdapter.Gravity.LEFT);

        // Draw the handle overlay
        canvas.drawRect(mTimetextBounds, mTextBgPaint);

        spacingTop = 0;
        for(int i = 0; i < lines; i++){
            if(i > 0) spacingTop += mLineSpacings[i-1];

            boolean closest = i == mClosestIndex;

            int exactY = (int) (lineOffset * i + offsetTop);
            int yoffset = Math.abs(climaxY - exactY);
            float yoffsetPerc = Math.min(1f, (float)yoffset / (lineOffset * 6));
            float linewidth = (1f - yoffsetPerc) * (mStripeWidthExtended - mStripeWidth) + mStripeWidth;

            int drawY = offsetTop + (int) (spacingTop - mStripeHeight/2);

            if(i % stepC == 0 || yoffsetPerc < 1f) {
                int color = mLinePaint.getColor();

                if (i % stepC != 0 && yoffsetPerc < 1f) {
                    int alpha = (int) (Color.alpha(color) * (1f - yoffsetPerc));
                    mLinePaint.setColor(Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color)));
                    linewidth /= 2;
                }

                canvas.drawRect(0, drawY, linewidth, drawY + mStripeHeight, closest ? mTextTextPaint : mLinePaint);

                if(i % getAdjustedSmallPipCount() == 0){
                    Element e = mElements[i / getAdjustedSmallPipCount()];
                    mAdapter.onDrawElement(i, canvas, e.bounds, e.yOffsetPerc, LinearPickerAdapter.Gravity.LEFT);
                }

                mLinePaint.setColor(color);
            }
        }

        // Draw handle foreground
        mAdapter.onDrawHandle(mClosestIndex, mInvisibleStep, canvas, mTimetextBounds,
                LinearPickerAdapter.Gravity.LEFT, mOccluded);

        mTimetextBounds.set(
                mTimetextBounds.left, mTimetextBounds.top - offset,
                mTimetextBounds.right, mTimetextBounds.bottom - offset
        );

        // user is interacting with view, continuously update the view for animations inside the handler
        if(mOccludedChanged + 1000 > System.currentTimeMillis()){
            postInvalidate();
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
                mInvisibleStep = 0;

                mDragOffsetY = (int) (y - mTimetextBounds.top);

                LinearPickerAdapter.ScreenHalf newOcclude = x < getWidth() / 2 ?
                        LinearPickerAdapter.ScreenHalf.LEFT : LinearPickerAdapter.ScreenHalf.RIGHT;
                if(newOcclude != mOccluded){
                    mOccluded = newOcclude;
                    mOccludedChanged = System.currentTimeMillis();
                }
            }
            else if(mOutsideDragEnabled){
                mDragmode = DragMode.OUTSIDE;
            }

            postInvalidate();

            return true;
        }
        else
        if(ac == MotionEvent.ACTION_MOVE){
            double distance = Math.sqrt((x - mStartX) * (x - mStartX) + (y - mStartY) * (y - mStartY));
            if(mDragmode == DragMode.INSIDE){
                int top = (int) (y - mDragOffsetY);
                top = Math.max(0, top);
                top = Math.min(getMeasuredHeight() - mTextBoundsHeight, top);
                mTimetextBounds.set(mTimetextBounds.left, top, mTimetextBounds.right, top + mTextBoundsHeight);

                LinearPickerAdapter.ScreenHalf newOcclude = x < getWidth() / 2 ?
                        LinearPickerAdapter.ScreenHalf.LEFT : LinearPickerAdapter.ScreenHalf.RIGHT;
                if(newOcclude != mOccluded){
                    mOccluded = newOcclude;
                    mOccludedChanged = System.currentTimeMillis();
                }

                postInvalidate();
            }
            else if(mDragmode == DragMode.OUTSIDE && distance > 50){
                mValidClick = false;

                if(distance > 100 && !mAnimating){
                    mDragOffsetY = mTimetextBounds.height()/2;
                    mDragmode = DragMode.INSIDE;
                    mAnimating = true;
                    animateDragOffset((int) (mTimetextBounds.top - y) + mTimetextBounds.height()/2, 0);
                }
            }

            return true;
        }
        else
        if(ac == MotionEvent.ACTION_UP){

            if(mOccluded != LinearPickerAdapter.ScreenHalf.NONE){
                mOccluded = LinearPickerAdapter.ScreenHalf.NONE;
                mOccludedChanged = System.currentTimeMillis();
            }

            if(mDragmode == DragMode.OUTSIDE && mValidClick){
                handleClick(mStartX, mStartY);
            }
            else if(mDragmode == DragMode.INSIDE){
                mClosestIndex = calculateClosestHourFromBounds(mTimetextBounds);
                setBoundsForHour(mClosestIndex);
            }

            mDragmode = DragMode.UP;

            postInvalidate();

            return true;
        }

        if(mOccluded != LinearPickerAdapter.ScreenHalf.NONE){
            mOccluded = LinearPickerAdapter.ScreenHalf.NONE;
            mOccludedChanged = System.currentTimeMillis();
        }

        return super.onTouchEvent(event);
    }

    private void animateDragOffset(float start, float end) {
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            ValueAnimator anim = ValueAnimator.ofFloat(start, end);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float animatedtop = (float)valueAnimator.getAnimatedValue();
                    mHandleOffset = (int) animatedtop;
                    postInvalidate();
                }
            });
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    mAnimating = false;
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            anim.setDuration(500);
            anim.start();
        }else{
            mAnimating = false;
            mDragOffsetY = (int) end;
            postInvalidate();
        }
    }

    private void handleClick(float x, float y) {
        if(y < mTimetextBounds.centerY()){
            if(mInvisibleStep >= 1){
                mInvisibleStep -= 1;
                setBoundsForHour(mClosestIndex, mInvisibleStep);
            }else{
                mInvisibleStep = mAdapter.getInvisiblePipCount(mClosestIndex);
                setBoundsForHour(mClosestIndex - 1, mInvisibleStep);
            }
        }
        else{
            if(mInvisibleStep < mAdapter.getInvisiblePipCount(mClosestIndex)){
                mInvisibleStep += 1;
                setBoundsForHour(mClosestIndex, mInvisibleStep);
            }else{
                mInvisibleStep = 0;
                setBoundsForHour(mClosestIndex + 1, mInvisibleStep);
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
        if(hour < 0 || hour > getTotalPositions()) return;

        int offsetTop = mTextBoundsHeight / 2;
        int offsetBottom = offsetTop;
        int timeLineHeight = getMeasuredHeight() - offsetTop - offsetBottom;
        float spaceHeight = (float) timeLineHeight / getTotalPositions();

        float minoffset = (minutes / (float) (mAdapter.getInvisiblePipCount(hour)+1)) * spaceHeight;

        mClosestIndex = hour;

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
            anim.setDuration(Math.min(500, (long) (300f * Math.abs(top - mTimetextBounds.top) / spaceHeight)));
            anim.start();
        }else{
            mTimetextBounds.set(mTimetextBounds.left, (int) top, mTimetextBounds.right, (int) top + mTextBoundsHeight);
            postInvalidate();
        }
    }

    private int smallPipCache = 0;
    private int getAdjustedSmallPipCount(){
        return smallPipCache;
    }
}
