package net.steamcrafted.lineartimepickerexample;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import net.steamcrafted.lineartimepicker.LinearTimePickerDialog;
import net.steamcrafted.lineartimepicker.adapter.DateAdapter;
import net.steamcrafted.lineartimepicker.adapter.LinearPickerAdapter;
import net.steamcrafted.lineartimepicker.adapter.TimeAdapter;
import net.steamcrafted.lineartimepicker.view.LinearPickerView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(android.R.id.content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearTimePickerDialog.Builder.with(MainActivity.this)
                        .setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimary, getTheme()))
                        .setLineColor(Color.GRAY)
                        .setTextColor(Color.WHITE)
                        .setTextBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, getTheme()))
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
    }
}
