package com.abner.app5;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RefureshListView lv_listview;
    private ArrayList<String> dates;
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv_listview = (RefureshListView) findViewById(R.id.lv_listview);

        dates = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            dates.add("这是一条ListView数据" + i);
        }
        mAdapter = new MyAdapter();
        lv_listview.setAdapter(mAdapter);

        lv_listview.setOnRefreshListener(new RefureshListView.OnRefreshListener() {

                                             @Override
                                             public void onRefresh() {
                                                 new Thread() {
                                                     @Override
                                                     public void run() {
                                                         try {
                                                             Thread.sleep(2000);
                                                         } catch (InterruptedException e) {
                                                             e.printStackTrace();
                                                         }
                                                         dates.add(0, "我是下拉刷新");
                                                         dates.add(0, "aaaaa");
                                                         runOnUiThread(new Runnable() {
                                                             @Override
                                                             public void run() {
                                                                 mAdapter.notifyDataSetChanged();
                                                                 lv_listview.onRefreshComplete();
                                                             }
                                                         });
                                                     }
                                                 }.start();
                                             }

                                             @Override
                                             public void onLoadMore() {
                                                 new Thread(){
                                                     @Override
                                                     public void run() {
                                                         try {
                                                             Thread.sleep(2000);
                                                         } catch (InterruptedException e) {
                                                             e.printStackTrace();
                                                         }
                                                         dates.add("jiazai加载");
                                                         dates.add("jiazai加载");
                                                         runOnUiThread(new Runnable() {
                                                             @Override
                                                             public void run() {
                                                                 mAdapter.notifyDataSetChanged();
                                                                 lv_listview.onLoadComplete();
                                                             }
                                                         });
                                                     }
                                                 }.start();
                                             }
                                         }

        );

    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return dates.size();
        }

        @Override
        public String getItem(int position) {
            return dates.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(getApplicationContext());
            textView.setTextSize(28);
            textView.setText(getItem(position));
            return textView;
        }
    }
}
