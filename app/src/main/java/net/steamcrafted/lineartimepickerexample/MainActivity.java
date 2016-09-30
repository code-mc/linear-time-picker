package net.steamcrafted.lineartimepickerexample;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import net.steamcrafted.lineartimepicker.LinearTimePickerDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(android.R.id.content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LinearTimePickerDialog.Builder(MainActivity.this)
                        .setBackgroundColor(Color.RED)
                        .setLineColor(Color.BLUE)
                        .setTextColor(Color.BLACK)
                        .setTextBackgroundColor(Color.YELLOW)
                        .setButtonCallback(new LinearTimePickerDialog.ButtonCallback() {
                            @Override
                            public void onPositive(DialogInterface dialog, int hour, int minutes) {

                            }

                            @Override
                            public void onNegative(DialogInterface dialog) {

                            }
                        })
                        .setPositiveText("lol")
                        .build()
                        .show();
            }
        });

    }
}
