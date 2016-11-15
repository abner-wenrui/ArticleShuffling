package com.abner.app4.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Project   com.abner.app4.ui
 *
 * @Author Abner
 * Time   2016/10/2.12:17
 */

public class ToggleView extends View {

    private Bitmap switchBackgroundBitmap;
    private Bitmap slideButtonResource;
    private Paint paint;
    private boolean mSwitchState = false;
    private float currentX;
    private boolean isTouchMode = false;
    private OnSwitchStateUpdateListener mOnSwitchStateUpdateListener;

    public ToggleView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint = new Paint();
    }

    public ToggleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        String namespace = "http://schemas.android.com/apk/res-auto";
        int switch_backound = attrs.getAttributeResourceValue(namespace, "switch_backound", -1);
        int slide_button = attrs.getAttributeResourceValue(namespace, "slide_button", -1);
        mSwitchState = attrs.getAttributeBooleanValue(namespace, "switch_state", false);
        setSwitchBackoundResource(switch_backound);
        setSlideButtonResource(slide_button);
    }

    public ToggleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(switchBackgroundBitmap.getWidth(), switchBackgroundBitmap.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(switchBackgroundBitmap, 0, 0, paint);
        if (isTouchMode) {
            float newLeft = currentX - slideButtonResource.getWidth() / 2.0f;
            if (newLeft < 0) newLeft = 0;
            else if (newLeft > switchBackgroundBitmap.getWidth() - slideButtonResource.getWidth())
                newLeft = switchBackgroundBitmap.getWidth() - slideButtonResource.getWidth();
            canvas.drawBitmap(slideButtonResource, newLeft, 0, paint);
        } else {
            if (mSwitchState) {
                canvas.drawBitmap(slideButtonResource, switchBackgroundBitmap.getWidth() - slideButtonResource.getWidth(), 0, paint);
            } else {
                canvas.drawBitmap(slideButtonResource, 0, 0, paint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouchMode = true;
                currentX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                currentX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                isTouchMode = false;
                currentX = event.getX();
                float center = switchBackgroundBitmap.getWidth() / 2.0f;

                if (currentX > center != mSwitchState && mOnSwitchStateUpdateListener != null) {
                    mOnSwitchStateUpdateListener.onStateUpdate(currentX > center);
                }
                mSwitchState = currentX > center;

                break;
        }
        invalidate();
        return true;
    }

    /**
     * 设置背景图片
     *
     * @param switch_background
     */
    public void setSwitchBackoundResource(int switch_background) {
        switchBackgroundBitmap = BitmapFactory.decodeResource(getResources(), switch_background);
    }

    /**
     * 设置滑块图片资源
     *
     * @param slide_button
     */
    public void setSlideButtonResource(int slide_button) {
        slideButtonResource = BitmapFactory.decodeResource(getResources(), slide_button);
    }

    //设置开关状态
    public void setSwitchState(boolean switchState) {
        mSwitchState = switchState;
    }

    public void setOnSwitchStateUpdateListener(OnSwitchStateUpdateListener onSwitchStateUpdateListener) {

        mOnSwitchStateUpdateListener = onSwitchStateUpdateListener;
    }

    public interface OnSwitchStateUpdateListener {
        void onStateUpdate(boolean state);
    }
}
