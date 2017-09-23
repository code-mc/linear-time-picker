package net.steamcrafted.lineartimepicker.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import net.steamcrafted.lineartimepicker.R;
import net.steamcrafted.lineartimepicker.view.LinearTimePickerView;

/**
 * Created by Wannes2 on 23/09/2017.
 */

public class LinearTimePickerDialog extends BaseLinearPickerDialog {

    private final ButtonCallback defaultBtnCallback = new ButtonCallback() {
        @Override
        public void onPositive(DialogInterface dialog, int hour, int minutes) {
            dialog.dismiss();
        }

        @Override
        public void onNegative(DialogInterface dialog) {
            dialog.dismiss();
        }
    };
    private final ButtonCallback mBtnCallback;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.ltp_dialog_time;
    }

    protected LinearTimePickerDialog(Context context, ButtonCallback btnCallback, int bgColor, int textColor,
                                     int lineColor, int textbgcolor, int buttonColor, int dialogBgColor, boolean showTutorial) {
        super(context, bgColor, textColor, lineColor, textbgcolor, buttonColor, dialogBgColor, showTutorial);
        mBtnCallback = btnCallback;
        init();
    }

    protected LinearTimePickerDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mBtnCallback = defaultBtnCallback;
        init();
    }

    protected LinearTimePickerDialog(Context context, int themeResId) {
        super(context, themeResId);
        mBtnCallback = defaultBtnCallback;
        init();
    }

    private void init() {
        final LinearTimePickerView v = (LinearTimePickerView) contentView.findViewById(R.id.ltp);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View ignore) {
                mBtnCallback.onPositive(LinearTimePickerDialog.this, v.getHour(), v.getMinutes());
                dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View ignore) {
                mBtnCallback.onNegative(LinearTimePickerDialog.this);
                dismiss();
            }
        });
    }

    public static class Builder extends BaseLinearPickerDialog.Builder<Builder> {
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

        public LinearTimePickerDialog build(){
            return new LinearTimePickerDialog(mContext, mBtnCallback, mbgColor, mTextColor, mLineColor,
                    mTextBgColor, mButtonColor, mDialogBgColor, mShowTutorial);
        }
    }

    public interface ButtonCallback {
        void onPositive(DialogInterface dialog, int hour, int minutes);
        void onNegative(DialogInterface dialog);
    }
}
