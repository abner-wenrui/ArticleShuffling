package com.abner.app4;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.abner.app4.ui.ToggleView;

public class MainActivity extends AppCompatActivity {

    private ToggleView tv_toggle_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_toggle_view = (ToggleView) findViewById(R.id.tv_toggle_view);
//        tv_toggle_view.setSwitchBackoundResource(R.mipmap.switch_background);
//        tv_toggle_view.setSlideButtonResource(R.mipmap.slide_button);
//        tv_toggle_view.setSwitchState(false);

        tv_toggle_view.setOnSwitchStateUpdateListener(new ToggleView.OnSwitchStateUpdateListener(){

            @Override
            public void onStateUpdate(boolean state) {
                Toast.makeText(MainActivity.this, "11111111", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
