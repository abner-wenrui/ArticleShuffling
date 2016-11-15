package com.abner.app2;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static com.abner.app2.R.mipmap.c;

public class MainActivity extends AppCompatActivity {

    private static final int LONGCOUNT = 1002;
    private android.support.v4.view.ViewPager vp_view_page;
    private android.widget.TextView tv_text;
    private android.widget.LinearLayout ll_point_container;
    private int[] mImageResIds;
    private ArrayList<ImageView> imageViewList;
    private String[] mContentDesc;
    private int lastEnablePosition = 0;
    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        initAdapter();

        // 开启轮询
        new Thread(){
            public void run() {
                isRunning = true;
                while(isRunning){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 往下跳一位
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            vp_view_page.setCurrentItem(vp_view_page.getCurrentItem()+1);
                        }
                    });
                }
            };
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    private void initData() {
        //图片资源Id数组
        mImageResIds = new int[]{R.mipmap.a, R.mipmap.b, c, R.mipmap.d, R.mipmap.e};
        mContentDesc = new String[]{
                "第一张", "第二张", "第三张", "第四章", "第五章", "第六章"
        };
        //初始化要展示的5隔ImageView
        imageViewList = new ArrayList<>();
        ImageView imageView;
        View pointView;
        LinearLayout.LayoutParams params;
        for (int i = 0; i < mImageResIds.length; i++) {
            imageView = new ImageView(this);
            imageView.setBackgroundResource(mImageResIds[i]);
            imageViewList.add(imageView);
            pointView = new View(this);
            pointView.setBackgroundResource(R.drawable.select_bg_point);
            params = new LinearLayout.LayoutParams(5, 5);
            if (i != 0)
                params.leftMargin = 10;
            pointView.setEnabled(false);
            tv_text.setText(mContentDesc[i]);
            ll_point_container.addView(pointView, params);
        }
    }

    private void initView() {
        this.ll_point_container = (LinearLayout) findViewById(R.id.ll_point_container);
        this.tv_text = (TextView) findViewById(R.id.tv_text);
        this.vp_view_page = (ViewPager) findViewById(R.id.vp_viewpage);
        vp_view_page.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tv_text.setText(mContentDesc[position % imageViewList.size()]);
//                for (int i = 0; i < ll_point_container.getChildCount(); i++) {
//                    View childAt = ll_point_container.getChildAt(position);
//                    childAt.setEnabled(position == i);
//                }
                ll_point_container.getChildAt(lastEnablePosition % imageViewList.size()).setEnabled(false);
                ll_point_container.getChildAt(position % imageViewList.size()).setEnabled(true);
                lastEnablePosition = position % imageViewList.size();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initAdapter() {

        ll_point_container.getChildAt(0).setEnabled(true);
        tv_text.setText(mContentDesc[0]);

        int pos = LONGCOUNT / 2 - (LONGCOUNT / 2) % imageViewList.size();

        vp_view_page.setAdapter(new MyAdapter());
        vp_view_page.setCurrentItem(500);

    }

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return LONGCOUNT;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //1.返回要显示的条目内容
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int newPosition = position % imageViewList.size();
            ImageView imageView = imageViewList.get(newPosition);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }
}
