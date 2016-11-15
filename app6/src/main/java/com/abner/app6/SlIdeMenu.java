package com.abner.app6;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Project   com.abner.app6
 *
 * @Author Abner
 * Time   2016/10/3.21:39
 */

public class SlIdeMenu extends ViewGroup {


    private float mDownX;
    private float mMoveX;
    private static final int MAIN_STATE = 0;
    private static final int MENU_STATE = 1;
    private int currentStart = MAIN_STATE;
    private Scroller mScroller;
    private float mDownY;

    public SlIdeMenu(Context context) {
        super(context);
        init();
    }

    public SlIdeMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SlIdeMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initAnimation();
    }

    private void initAnimation() {
        mScroller = new Scroller(getContext());
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        View leftMemu = getChildAt(0);
        leftMemu.measure(leftMemu.getLayoutParams().width, heightMeasureSpec);

        View mainContent = getChildAt(1);
        mainContent.measure(widthMeasureSpec, heightMeasureSpec);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        View leftMenu = getChildAt(0);
        leftMenu.layout(-leftMenu.getMeasuredWidth(), 0, 0, b);
        getChildAt(1).layout(l, t, r, b);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                mMoveX = event.getX();
                int scrollx = (int) (mDownX - mMoveX);
                int newScrollPositoin = getScrollX() + scrollx;
                if (newScrollPositoin < -(getChildAt(0).getMeasuredWidth())) {
                    scrollTo((-getChildAt(0).getMeasuredWidth()), 0);
                } else if (newScrollPositoin > 0) {
                    scrollTo(0, 0);
                } else {
                    scrollBy(scrollx, 0);
                }
                mDownX = mMoveX;
                break;
            case MotionEvent.ACTION_UP:
                if (getScrollX() < -getChildAt(0).getMeasuredWidth() / 2) {
                    //向左划
                    currentStart = MENU_STATE;
                    updateCurrentContent();
                } else {
                    //向右划
                    currentStart = MAIN_STATE;
                    updateCurrentContent();
                }
                break;
        }
        return true;
    }

    private void updateCurrentContent() {
//        int dx = 0;
//        int startX = getScrollX();
//        switch (currentStart) {
//            case MAIN_STATE:
//                dx = -startX;
//                int durations = Math.abs(dx * 10);
//                mScroller.startScroll(startX, 0, -dx, 0, durations);
//                //scrollTo(0,0);
//                invalidate();
//                break;
//            case MENU_STATE:
//                //scrollTo((-getChildAt(0).getMeasuredWidth()), 0);
//                dx = -getChildAt(0).getMeasuredWidth() - startX;
//                int duration = Math.abs(dx * 10);
//                mScroller.startScroll(startX, 0, dx, 0, duration);
//                invalidate();
//                break;
//        }

        int dx = 0;
        int startX = getScrollX();
        if (currentStart == MENU_STATE) {
            dx = -getChildAt(0).getMeasuredWidth() - startX;
        } else {
            dx = 0 - startX;
        }

        int durations = Math.abs(dx * 4);
        mScroller.startScroll(startX, 0, dx, 0, durations);
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            int currX = mScroller.getCurrX();
            scrollTo(currX, 0);
            invalidate();
        }
    }

    public void open() {
        currentStart = MENU_STATE;
        updateCurrentContent();
    }

    public void close() {
        currentStart = MAIN_STATE;
        updateCurrentContent();
    }

    public int getCurrentStart() {
        return currentStart;
    }

    public void switchState() {
        if (currentStart == MENU_STATE) {
            close();
        } else {
            open();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float xOffset = Math.abs(event.getX() - mDownX);
                float yOffset = Math.abs(event.getY() - mDownY);

                if (xOffset > yOffset && xOffset > 10) {
                    return true;
                }

                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptHoverEvent(event);
    }
}
