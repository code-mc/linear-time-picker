package net.steamcrafted.lineartimepickerexample;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import net.steamcrafted.lineartimepicker.adapter.DateAdapter;
import net.steamcrafted.lineartimepicker.adapter.LinearPickerAdapter;
import net.steamcrafted.lineartimepicker.adapter.TimeAdapter;
import net.steamcrafted.lineartimepicker.dialog.LinearDatePickerDialog;
import net.steamcrafted.lineartimepicker.dialog.LinearTimePickerDialog;
import net.steamcrafted.lineartimepicker.view.LinearPickerView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final int backgroundDark = ResourcesCompat.getColor(getResources(), R.color.background_dark, getTheme());
        final int foregroundDark = ResourcesCompat.getColor(getResources(), R.color.foreground_dark, getTheme());
        final int colorAccent = ResourcesCompat.getColor(getResources(), R.color.colorAccent, getTheme());

        LinearPickerView v = new LinearPickerView(this);
        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);

        LinearPickerAdapter dateAdap = new DateAdapter(this, textPaint);
        LinearPickerAdapter timeAdap = new TimeAdapter(this, textPaint);
        LinearPickerAdapter colorAdap = new ColorAdapter(this, textPaint);
        LinearPickerAdapter graColorAdap = new GradientColorAdapter(this, textPaint);
        v.setAdapter(dateAdap);

//        setContentView(v);

        v.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        v.setLineColor(Color.GRAY);
        v.setActiveLineColor(Color.WHITE);
        v.setHandleBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//        v.setHandleBackgroundColor(Color.TRANSPARENT);

        setContentView(R.layout.activity_main);

        findViewById(R.id.ltp_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearTimePickerDialog.Builder.with(MainActivity.this)
                        .setDialogBackgroundColor(foregroundDark)
                        .setPickerBackgroundColor(backgroundDark)
//                        .setLineColor(Color.argb(64, 255, 255, 255))
                        .setTextColor(Color.WHITE)
                        .setShowTutorial(true)
//                        .setTextBackgroundColor(Color.argb(16, 255, 255, 255))
                        .setButtonCallback(new LinearTimePickerDialog.ButtonCallback() {
                            @Override
                            public void onPositive(DialogInterface dialog, int hour, int minutes) {
                                Toast.makeText(MainActivity.this, "" + hour + ":" + minutes, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNegative(DialogInterface dialog) {

                            }
                        })
                        .build()
                        .show();
            }
        });
        findViewById(R.id.ltp_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearDatePickerDialog.Builder.with(MainActivity.this)
                        .setDialogBackgroundColor(foregroundDark)
                        .setPickerBackgroundColor(backgroundDark)
//                        .setLineColor(Color.argb(64, 255, 255, 255))
                        .setTextColor(Color.WHITE)
//                        .setTextBackgroundColor(Color.argb(16, 255, 255, 255))
                        .setYear(2017)
                        .setMinYear(2000)
                        .setMaxYear(2030)
                        .setShowTutorial(true)
                        .setButtonCallback(new LinearDatePickerDialog.ButtonCallback() {
                            @Override
                            public void onPositive(DialogInterface dialog, int year, int month, int day) {
                                Toast.makeText(MainActivity.this, "" + year + "-" + month + "-" + day, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNegative(DialogInterface dialog) {

                            }
                        })
                        .build()
                        .show();
            }
        });
    }
}
