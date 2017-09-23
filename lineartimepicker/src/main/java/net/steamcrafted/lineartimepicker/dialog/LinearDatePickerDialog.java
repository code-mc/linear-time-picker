package net.steamcrafted.lineartimepicker.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import net.steamcrafted.lineartimepicker.R;
import net.steamcrafted.lineartimepicker.view.LinearDatePickerView;
import net.steamcrafted.lineartimepicker.view.YearDialView;

/**
 * Created by Wannes2 on 23/09/2017.
 */

public class LinearDatePickerDialog extends BaseLinearPickerDialog {

    private static final int DEFAULT_MIN_YEAR = 1900, DEFAULT_MAX_YEAR = 3000;

    private final ButtonCallback defaultBtnCallback = new ButtonCallback() {
        @Override
        public void onPositive(DialogInterface dialog, int year, int month, int day) {
            dialog.dismiss();
        }

        @Override
        public void onNegative(DialogInterface dialog) {
            dialog.dismiss();
        }
    };
    private final ButtonCallback mBtnCallback;
    private final int mYear;
    private final int mMinYear;
    private final int mMaxYear;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.ltp_dialog_date;
    }

    protected LinearDatePickerDialog(Context context, ButtonCallback btnCallback, int bgColor,
                                     int textColor, int lineColor, int textbgcolor, int buttonColor,
                                     int dialogBgColor, int year, int minYear, int maxYear, boolean showTutorial) {
        super(context, bgColor, textColor, lineColor, textbgcolor, buttonColor, dialogBgColor, showTutorial);
        mBtnCallback = btnCallback;
        mYear = year;
        mMinYear = minYear;
        mMaxYear = maxYear;
        init();
    }

    protected LinearDatePickerDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mBtnCallback = defaultBtnCallback;
        mYear = 0;
        mMinYear = DEFAULT_MIN_YEAR;
        mMaxYear = DEFAULT_MAX_YEAR;
        init();
    }

    protected LinearDatePickerDialog(Context context, int themeResId) {
        super(context, themeResId);
        mBtnCallback = defaultBtnCallback;
        mYear = 0;
        mMinYear = DEFAULT_MIN_YEAR;
        mMaxYear = DEFAULT_MAX_YEAR;
        init();
    }

    private void init() {
        final LinearDatePickerView v = (LinearDatePickerView) contentView.findViewById(R.id.ltp);

        final YearDialView yearView = new YearDialView(getContext());
        toolbar.addView(yearView, 0, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        yearView.setMinYear(mMinYear);
        yearView.setMaxYear(mMaxYear);
        yearView.setSelectedYear(mYear);

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View ignore) {
                mBtnCallback.onPositive(LinearDatePickerDialog.this, yearView.getSelectedYear(), v.getMonth(), v.getDay());
                dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View ignore) {
                mBtnCallback.onNegative(LinearDatePickerDialog.this);
                dismiss();
            }
        });
    }

    public static class Builder extends BaseLinearPickerDialog.Builder<Builder> {
        private ButtonCallback mBtnCallback = new ButtonCallback() {
            @Override
            public void onPositive(DialogInterface dialog, int year, int month, int day) {
                dialog.dismiss();
            }

            @Override
            public void onNegative(DialogInterface dialog) {
                dialog.dismiss();
            }
        };
        private int mYear = 0;
        private int mMinYear = DEFAULT_MIN_YEAR;
        private int mMaxYear = DEFAULT_MAX_YEAR;

        private Builder(Context c) {
            super(c);
        }

        public static Builder with(Context c){
            return new Builder(c);
        }

        public Builder setButtonCallback(ButtonCallback buttonCallback){
            mBtnCallback = buttonCallback;
            return this;
        }

        public Builder setYear(int year){
            mYear = year;
            return this;
        }

        public Builder setMinYear(int year){
            mMinYear = year;
            return this;
        }

        public Builder setMaxYear(int year){
            mMaxYear = year;
            return this;
        }

        public LinearDatePickerDialog build(){
            return new LinearDatePickerDialog(mContext, mBtnCallback, mbgColor, mTextColor, mLineColor,
                    mTextBgColor, mButtonColor, mDialogBgColor, mYear, mMinYear, mMaxYear, mShowTutorial);
        }
    }

    public interface ButtonCallback {
        void onPositive(DialogInterface dialog, int year, int month, int day);
        void onNegative(DialogInterface dialog);
    }
}
