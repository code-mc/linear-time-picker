package net.steamcrafted.lineartimepicker.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Build;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import net.steamcrafted.lineartimepicker.R;
import net.steamcrafted.lineartimepicker.view.LinearPickerView;

/**
 * Created by Wannes2 on 30/09/2016.
 */
abstract class BaseLinearPickerDialog extends AlertDialog {

    private static final int NO_COLOR_SET = -0x1ffffff;

    private int mbgColor = NO_COLOR_SET;
    private int mTextColor = NO_COLOR_SET;
    private int mLineColor = NO_COLOR_SET;
    private int mTextBgColor = NO_COLOR_SET;
    private int mButtonColor = NO_COLOR_SET;
    private int mDialogBgColor = NO_COLOR_SET;
    private boolean mShowTutorial = false;
    protected FrameLayout toolbar;
    protected ImageView btnCancel;
    protected ImageView btnApply;
    protected FrameLayout contentView;

    protected abstract int getLayoutResourceId();

    protected BaseLinearPickerDialog(Context context, int bgColor, int textColor, int lineColor, int textbgcolor, int buttonColor, int dialogBgColor, boolean showTutorial) {
        super(context);

        mbgColor = bgColor;
        mDialogBgColor = dialogBgColor;
        mTextColor = textColor;
        mLineColor = lineColor;
        mTextBgColor = textbgcolor;
        mButtonColor = buttonColor;
        mShowTutorial = showTutorial;

        init(context);
    }

    protected BaseLinearPickerDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);

        initDefaults(context);
        init(context);
    }

    protected BaseLinearPickerDialog(Context context, int themeResId) {
        super(context, themeResId);

        initDefaults(context);
        init(context);
    }

    private void initDefaults(Context themedContext){

    }

    private void init(Context themedContext) {
        contentView = (FrameLayout) getLayoutInflater().inflate(getLayoutResourceId(), null);

        final LinearPickerView v = (LinearPickerView) contentView.findViewById(R.id.ltp);
        toolbar = (FrameLayout) contentView.findViewById(R.id.toolbar);
        btnCancel = (ImageView) contentView.findViewById(R.id.btn_cancel);
        btnApply = (ImageView) contentView.findViewById(R.id.btn_apply);

        setView(contentView);

        if(themedContext instanceof Activity){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && mbgColor == NO_COLOR_SET) {
                TypedArray arr = themedContext.getTheme().obtainStyledAttributes(new int[]{android.R.attr.colorPrimary});
                if(arr.hasValue(0)){
                    mbgColor = arr.getColor(0, Color.TRANSPARENT);
                }
                arr.recycle();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && mDialogBgColor == NO_COLOR_SET) {
                TypedArray arr = themedContext.getTheme().obtainStyledAttributes(new int[]{android.R.attr.colorPrimaryDark});
                if(arr.hasValue(0)){
                    mDialogBgColor = arr.getColor(0, Color.TRANSPARENT);
                }
                arr.recycle();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && mButtonColor == NO_COLOR_SET) {
                TypedArray arr = themedContext.getTheme().obtainStyledAttributes(new int[]{android.R.attr.colorAccent});
                if(arr.hasValue(0)){
                    int color = arr.getColor(0, Color.TRANSPARENT);
                    if(color != Color.TRANSPARENT)
                        mButtonColor = color;
                }
                arr.recycle();
            }
        }

        v.setBackgroundColor(mbgColor);

        if(mLineColor != NO_COLOR_SET)
            v.setLineColor(mLineColor);
        if(mTextColor != NO_COLOR_SET)
            v.setActiveLineColor(mTextColor);
        if(mTextBgColor != NO_COLOR_SET)
            v.setHandleBackgroundColor(mTextBgColor);

//        setButton(BUTTON_NEGATIVE, mNegativeText, new OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                mBtnCallback.onNegative(dialogInterface);
//            }
//        });
//
//        setButton(BUTTON_POSITIVE, mPositiveText, new OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                mBtnCallback.onPositive(dialogInterface, v.getHour(), v.getMinutes());
//            }
//        });

        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
//                getButton(BUTTON_POSITIVE).setTextColor(mButtonColor);
//                getButton(BUTTON_NEGATIVE).setTextColor(mButtonColor);

                if(mDialogBgColor != NO_COLOR_SET){
                    getWindow().getDecorView().getBackground().mutate()
                            .setColorFilter(new PorterDuffColorFilter(mDialogBgColor, PorterDuff.Mode.SRC));

                    toolbar.setBackgroundColor(mDialogBgColor);
                }

                if(mShowTutorial){
                    v.showTutorial();
                }
            }
        });
    }

    public abstract static class Builder<T extends Builder> {
        protected final Context mContext;

        protected int mbgColor   = NO_COLOR_SET;
        protected int mDialogBgColor = NO_COLOR_SET;
        protected int mLineColor = NO_COLOR_SET;
        protected int mTextColor = NO_COLOR_SET;
        protected int mTextBgColor = NO_COLOR_SET;
        protected int mButtonColor = NO_COLOR_SET;
        protected boolean mShowTutorial = false;

        protected Builder(Context c){
            mContext = c;
        }

        public T setPickerBackgroundColor(int color){
            mbgColor = color;
            return (T) this;
        }

        public T setDialogBackgroundColor(int color){
            mDialogBgColor = color;
            return (T) this;
        }

        public T setTextColor(int color){
            mTextColor = color;
            return (T) this;
        }

        public T setTextBackgroundColor(int color){
            mTextBgColor = color;
            return (T) this;
        }

        public T setLineColor(int color){
            mLineColor = color;
            return (T) this;
        }

        public T setButtonColor(int color) {
            this.mButtonColor = color;
            return (T) this;
        }

        public T setShowTutorial(boolean showTutorial) {
            this.mShowTutorial = showTutorial;
            return (T) this;
        }
    }
}
