package com.abner.app3;


import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ImageButton ib_dropdown;
    private ListView listView;
    private EditText et_input;
    private ArrayList<String> datas;
    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ib_dropdown = ((ImageButton) findViewById(R.id.ib_dropdown));
        et_input = (EditText) findViewById(R.id.et_input);
        ib_dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });
    }

    private void showPopupWindow() {
        initListView();
        mPopupWindow = new PopupWindow(listView, et_input.getWidth(), 500);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.showAsDropDown(et_input, 0, 0);
    }

    private void initListView() {
        listView = new ListView(this);
        listView.setDividerHeight(0);
        listView.setBackgroundResource(R.mipmap.listview_background);


        datas = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            datas.add(100000 + i + "");
        }

        listView.setAdapter(new MyAdapter());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                et_input.setText(datas.get(position));
                mPopupWindow.dismiss();
            }
        });
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public String getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.item_number, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_number11 = (TextView) convertView.findViewById(R.id.tv_number);
                viewHolder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = ((ViewHolder) convertView.getTag());
            }
            viewHolder.tv_number11.setText(getItem(position));
            viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datas.remove(position);
                    notifyDataSetChanged();
                    if (datas.size() == 0)
                        mPopupWindow.dismiss();
                }
            });
            return convertView;
        }
    }

    class ViewHolder {
        public TextView tv_number11;
        public ImageView iv_delete;
    }
}
