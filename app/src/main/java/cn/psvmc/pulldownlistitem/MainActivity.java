package cn.psvmc.pulldownlistitem;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.psvmc.pulldownlistslideitem.HeaderOrFooter.ZJPullListAnimate;
import cn.psvmc.pulldownlistslideitem.Listener.ZJListItemClickListener;
import cn.psvmc.pulldownlistslideitem.Listener.ZJPullListListenerImpl;
import cn.psvmc.pulldownlistslideitem.SlideView;
import cn.psvmc.pulldownlistslideitem.ZJPullListView;


public class MainActivity extends Activity implements ZJListItemClickListener {

    final String TAG = "MainActivity";
    final List<String> adapterData = new ArrayList<>();
    private BaseAdapter mAdapter;
    private ListView mListView;
    Context mContext;
    ZJPullListView pullDownListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        for (int i = 0; i < 3; i++) {
            adapterData.add("111");
            adapterData.add("222");
            adapterData.add("333");
        }
        initAdapter();
        initListView();

    }

    private void initListView() {
        pullDownListView = (ZJPullListView) this.findViewById(R.id.pullDownListView);
        final ZJPullListAnimate listFooterView = (ZJPullListAnimate) this.findViewById(R.id.listFooterView);
        final ZJPullListAnimate listHeaderView = (ZJPullListAnimate) this.findViewById(R.id.listHeaderView);
        //点击监听
        pullDownListView.setListItemClickListener(this);
        mListView = pullDownListView.getListView();
        mListView.setAdapter(mAdapter);

        pullDownListView.setOnPullHeightChangeListener(new ZJPullListListenerImpl(pullDownListView, listHeaderView, listFooterView) {

            @Override
            public void onRefresh() {
                super.onRefresh();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapterData.removeAll(adapterData);
                        adapterData.add("222");
                        adapterData.add("222");
                        adapterData.add("111");
                        mAdapter.notifyDataSetChanged();
                        pullDownListView.endingRefreshOrLoadMore();
                    }

                }, 2000);
            }

            @Override
            public void onLoadMore() {
                super.onLoadMore();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        adapterData.add("000");
                        mAdapter.notifyDataSetChanged();
                        pullDownListView.endingRefreshOrLoadMore();
                    }

                }, 2000);
            }


        });
    }

    private void initAdapter() {
        mAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return adapterData.size();
            }

            @Override
            public Object getItem(int position) {

                return adapterData.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                SlideView slideView = (SlideView) convertView;
                if (slideView == null) {
                    TextView textView = new TextView(mContext);
                    textView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(mContext, 50)));

                    textView.setTextSize(20);
                    textView.setTextColor(0xff000000);
                    textView.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                    textView.setPadding(50, 0, 0, 0);
                    slideView = new SlideView(
                            MainActivity.this,
                            R.layout.slide_view_merge,
                            R.id.view_content,
                            R.id.left_holder,
                            R.id.right_holder
                    );
                    slideView.setContentView(textView);
                }
                ((TextView) (slideView.getItemView())).setText(adapterData.get(position));
                return slideView;
            }

        };

    }


    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }


    public void slideItemClick(View v) {
        Log.i(TAG, "点击的位置:" + pullDownListView.getClickPosition());
        if (v.getId() == R.id.check) {
            Log.i(TAG, "点击了选择");
        } else if (v.getId() == R.id.edit) {
            Log.i(TAG, "点击了编辑");
        } else if (v.getId() == R.id.delete) {
            Log.i(TAG, "点击了删除");
        }

    }

    @Override
    public void zjitemClick(int position) {
        Log.i(TAG, "点击的位置:" + position);
    }
}