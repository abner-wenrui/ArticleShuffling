package com.abner.widgetdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.abner.widgetdemo.Utils.AnimationUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private android.widget.ImageButton home;
    private android.widget.RelativeLayout rllevel1;
    private android.widget.ImageView ivmenu;
    private android.widget.RelativeLayout rllevel2;
    private android.widget.RelativeLayout rllevel3;
    private android.widget.RelativeLayout activitymain;
    private ImageButton ibtnhome;
    private ImageButton ibtnmenu;
    boolean isLevel1Display1 = true;
    boolean isLevel1Display2 = true;
    boolean isLevel1Display3 = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void initUI() {
        this.rllevel3 = (RelativeLayout) findViewById(R.id.rl_level3);
        this.rllevel2 = (RelativeLayout) findViewById(R.id.rl_level2);
        this.ibtnmenu = (ImageButton) findViewById(R.id.ibtn_menu);
        this.rllevel1 = (RelativeLayout) findViewById(R.id.rl_level1);
        this.ibtnhome = (ImageButton) findViewById(R.id.ibtn_home);
        ibtnmenu.setOnClickListener(this);
        ibtnhome.setOnClickListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            long delay = 0;
            if (isLevel1Display3 ) {
                AnimationUtils.rotateOutAnim(rllevel3, delay);
                isLevel1Display3 = false;
                delay+=200;
            }
            if (isLevel1Display2) {
                AnimationUtils.rotateOutAnim(rllevel2, delay);
                isLevel1Display2 = false;
                delay+=200;
            }
            if (isLevel1Display1) {
                AnimationUtils.rotateOutAnim(rllevel1, delay);
                isLevel1Display1 = false;
            } else {
                AnimationUtils.rotateInAnim(rllevel1, 0);
                AnimationUtils.rotateInAnim(rllevel2, 200);
                AnimationUtils.rotateInAnim(rllevel3, 400);
                isLevel1Display1 = true;
                isLevel1Display2 = true;
                isLevel1Display3 = true;
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        if (AnimationUtils.runningAnimationCount > 0) {
            return;
        }
        switch (v.getId()) {
            case R.id.ibtn_home:

                if (isLevel1Display3 && isLevel1Display2) {
                    AnimationUtils.rotateOutAnim(rllevel3, 0);
                    AnimationUtils.rotateOutAnim(rllevel2, 100);
                    isLevel1Display2 = false;
                    isLevel1Display3 = false;
                } else if (isLevel1Display2 && !isLevel1Display3) {
                    AnimationUtils.rotateOutAnim(rllevel2, 0);
                    isLevel1Display2 = false;
                } else {
                    AnimationUtils.rotateInAnim(rllevel2, 0);
                    isLevel1Display2 = true;
                }
                break;
            case R.id.ibtn_menu:
                if (isLevel1Display3) {
                    AnimationUtils.rotateOutAnim(rllevel3, 0);
                    isLevel1Display3 = false;
                } else {
                    AnimationUtils.rotateInAnim(rllevel3, 0);
                    isLevel1Display3 = true;
                }
                break;
        }
    }
}
