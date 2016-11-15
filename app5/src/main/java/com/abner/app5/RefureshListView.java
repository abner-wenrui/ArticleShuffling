package com.abner.app5;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;

/**
 * Project   com.abner.app5
 *
 * @Author Abner
 * Time   2016/10/2.19:39
 */

public class RefureshListView extends ListView {

    private View mHeadView;
    private float downY;
    private float moveY;
    private float upY;
    private int mMeasuredHeight;
    public static final int PULL_TO_REFRESH = 0;
    public static final int RELEASE_REFRESH = 1;
    public static final int REFRESHNG = 2;
    private int currentState;
    private RotateAnimation mRotateUpAnimation;
    private RotateAnimation mRotateDownAnimation;
    private ImageView iv_arrow;
    private ProgressBar pb_progress;
    private TextView tv_title;
    private TextView tv_desc_last_refresh;
    private int mPaddingTop;
    private OnRefreshListener mOnRefreshListener;
    private View mMFooterView;
    private int mFooterHeight;

    public RefureshListView(Context context) {
        super(context);
        init();
    }

    public RefureshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RefureshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initHeaderView();
        initFooterView();
        initAnimation();

        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE && getLastVisiblePosition()>=getCount()-1){
                    mMFooterView.setPadding(0,0,0,0);
                    setSelection(getCount());
                    if (mOnRefreshListener != null){
                        mOnRefreshListener.onLoadMore();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void initFooterView() {
        mMFooterView = View.inflate(getContext(), R.layout.layout_footer_list, null);
        mMFooterView.measure(0, 0);
        mFooterHeight = mMFooterView.getMeasuredHeight();
        mMFooterView.setPadding(0, 0, 0, -mFooterHeight);
        addFooterView(mMFooterView);
    }

    private void initAnimation() {
        mRotateUpAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateUpAnimation.setDuration(500);
        mRotateUpAnimation.setFillAfter(true);

        mRotateDownAnimation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateDownAnimation.setDuration(500);
        mRotateDownAnimation.setFillAfter(true);
    }

    private void initHeaderView() {
        mHeadView = View.inflate(getContext(), R.layout.layout_header_list, null);
        iv_arrow = (ImageView) mHeadView.findViewById(R.id.iv_arrow);
        pb_progress = (ProgressBar) mHeadView.findViewById(R.id.pb_progress);
        tv_title = ((TextView) mHeadView.findViewById(R.id.tv_title));
        tv_desc_last_refresh = ((TextView) mHeadView.findViewById(R.id.tv_desc_last_refresh));
        mHeadView.measure(0, 0);
        mMeasuredHeight = mHeadView.getMeasuredHeight();
        mHeadView.setPadding(0, -mMeasuredHeight, 0, 0);

        addHeaderView(mHeadView);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                moveY = ev.getY();
                float offset = moveY - downY;
                if (offset >= 0 && getFirstVisiblePosition() == 0) {
                    mPaddingTop = (int) (offset - mMeasuredHeight);
                    mHeadView.setPadding(0, mPaddingTop, 0, 0);
                    if (mPaddingTop >= 0 && currentState != RELEASE_REFRESH) {
                        currentState = RELEASE_REFRESH;
                        updateHeader();

                    } else if ((mPaddingTop < 0 && currentState != PULL_TO_REFRESH)) {
                        currentState = PULL_TO_REFRESH;
                        updateHeader();
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mPaddingTop < 0) {
                    mHeadView.setPadding(0, -mMeasuredHeight, 0, 0);
                } else {
                    mHeadView.setPadding(0, 0, 0, 0);
                    currentState = REFRESHNG;
                    updateHeader();
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void updateHeader() {
        switch (currentState) {
            case PULL_TO_REFRESH:
                iv_arrow.startAnimation(mRotateDownAnimation);
                tv_title.setText("下拉刷新");
                break;
            case RELEASE_REFRESH:
                iv_arrow.startAnimation(mRotateUpAnimation);
                tv_title.setText("释放刷新");
                break;
            case REFRESHNG:
                iv_arrow.clearAnimation();
                pb_progress.setVisibility(View.VISIBLE);
                iv_arrow.setVisibility(View.INVISIBLE);
                tv_title.setText("正在刷新");
                if (mOnRefreshListener != null) {
                    mOnRefreshListener.onRefresh();
                }
                break;
            default:
                break;
        }
    }

    public void onRefreshComplete() {
        currentState = PULL_TO_REFRESH;
        tv_title.setText("下拉刷新");
        iv_arrow.setVisibility(View.VISIBLE);
        pb_progress.setVisibility(View.INVISIBLE);
        mHeadView.setPadding(0, -mMeasuredHeight, 0, 0);
        String time = getTime();
        tv_desc_last_refresh.setText("最后刷新时间" + time);
    }

    private String getTime() {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(currentTimeMillis);
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {

        mOnRefreshListener = onRefreshListener;
    }

    public void onLoadComplete() {
        mMFooterView.setPadding(0,0,0,-mFooterHeight);
    }


    public interface OnRefreshListener{
        void onRefresh();
        void onLoadMore();
    }


}
