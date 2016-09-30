package net.steamcrafted.lineartimepicker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.TypedValue;

/**
 * Created by Wannes2 on 30/09/2016.
 */
public class LinearTimePickerDialog extends AlertDialog {

    private static final int NO_COLOR_SET = -1;

    private int mbgColor = NO_COLOR_SET;
    private int mTextColor = NO_COLOR_SET;
    private int mLineColor = NO_COLOR_SET;
    private int mTextBgColor = NO_COLOR_SET;

    private CharSequence mPositiveText;
    private CharSequence mNegativeText;
    private ButtonCallback mBtnCallback;

    protected LinearTimePickerDialog(Context context, ButtonCallback btnCallback, int bgColor, int textColor, int lineColor, int textbgcolor, CharSequence positiveText, CharSequence negativeText) {
        super(context);

        mBtnCallback = btnCallback;
        mbgColor = bgColor;
        mTextColor = textColor;
        mLineColor = lineColor;
        mTextBgColor = textbgcolor;
        mPositiveText = positiveText;
        mNegativeText = negativeText;

        init(context);
    }

    protected LinearTimePickerDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);

        initDefaults(context);
        init(context);
    }

    protected LinearTimePickerDialog(Context context, int themeResId) {
        super(context, themeResId);

        initDefaults(context);
        init(context);
    }

    private void initDefaults(Context themedContext){
        mPositiveText = themedContext.getString(android.R.string.ok);
        mNegativeText = themedContext.getString(android.R.string.cancel);
        mBtnCallback = new ButtonCallback() {
            @Override
            public void onPositive(DialogInterface dialog, int hour, int minutes) {
                dialog.dismiss();
            }

            @Override
            public void onNegative(DialogInterface dialog) {
                dialog.dismiss();
            }
        };
    }

    private void init(Context themedContext) {
        final LinearTimePickerView v = new LinearTimePickerView(getContext());
        setView(v);

        if(themedContext instanceof Activity){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && mbgColor == NO_COLOR_SET) {
                TypedArray arr = themedContext.getTheme().obtainStyledAttributes(new int[]{android.R.attr.colorPrimary});
                if(arr.hasValue(0)){
                    mbgColor = arr.getColor(0, Color.TRANSPARENT);
                }
                arr.recycle();
            }
        }

        v.setBackgroundColor(mbgColor);

        if(mLineColor != NO_COLOR_SET)
            v.setLineColor(mLineColor);
        if(mTextColor != NO_COLOR_SET)
            v.setTextColor(mTextColor);
        if(mTextBgColor != NO_COLOR_SET)
            v.setTextBackgroundColor(mTextBgColor);

        setButton(BUTTON_NEGATIVE, mNegativeText, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mBtnCallback.onNegative(dialogInterface);
            }
        });

        setButton(BUTTON_POSITIVE, mPositiveText, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mBtnCallback.onPositive(dialogInterface, v.getHour(), v.getMinutes());
            }
        });
    }

    public static class Builder {
        private final Context mContext;

        private int mbgColor   = NO_COLOR_SET;
        private int mLineColor = NO_COLOR_SET;
        private int mTextColor = NO_COLOR_SET;
        private int mTextBgColor = NO_COLOR_SET;

        private CharSequence mPositiveText;
        private CharSequence mNegativeText;
        private ButtonCallback mBtnCallback = new ButtonCallback() {
            @Override
            public void onPositive(DialogInterface dialog, int hour, int minutes) {
                dialog.dismiss();
            }

            @Override
            public void onNegative(DialogInterface dialog) {
                dialog.dismiss();
            }
        };

        public Builder(Context c){
            mContext = c;
            mPositiveText = c.getString(android.R.string.ok);
            mNegativeText = c.getString(android.R.string.cancel);
        }

        public Builder setBackgroundColor(int color){
            mbgColor = color;
            return this;
        }

        public Builder setTextColor(int color){
            mTextColor = color;
            return this;
        }

        public Builder setTextBackgroundColor(int color){
            mTextBgColor = color;
            return this;
        }

        public Builder setLineColor(int color){
            mLineColor = color;
            return this;
        }

        public Builder setPositiveText(CharSequence positiveText){
            mPositiveText = positiveText;
            return this;
        }

        public Builder setNegativeText(CharSequence negativeText){
            mNegativeText = negativeText;
            return this;
        }

        public Builder setButtonCallback(ButtonCallback callback){
            mBtnCallback = callback;
            return this;
        }

        public LinearTimePickerDialog build(){
            return new LinearTimePickerDialog(mContext, mBtnCallback, mbgColor, mTextColor, mLineColor, mTextBgColor, mPositiveText, mNegativeText);
        }
    }

    public interface ButtonCallback {
        void onPositive(DialogInterface dialog, int hour, int minutes);
        void onNegative(DialogInterface dialog);
    }
}
