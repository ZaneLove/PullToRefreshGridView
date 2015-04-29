package com.zanelove.pulltorefreshgridview;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import com.zanelove.gridviewpulltorefresh.library.ILoadingLayout;
import com.zanelove.gridviewpulltorefresh.library.PullToRefreshBase;
import com.zanelove.gridviewpulltorefresh.library.PullToRefreshGridView;
import java.util.LinkedList;

public class MainActivity extends Activity {
    private LinkedList<String> mListItems;
    private PullToRefreshGridView mPullRefreshListView;
    private ArrayAdapter<String> mAdapter;

    private int mItemCount = 13;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 得到控件
        mPullRefreshListView = (PullToRefreshGridView) findViewById(R.id.pull_refresh_grid);

        // 初始化数据和数据源
        initDatas();

        initIndicator();

        mAdapter = new ArrayAdapter<String>(this, R.layout.grid_item, R.id.id_grid_item_text, mListItems);
        mPullRefreshListView.setAdapter(mAdapter);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                String label = DateUtils.formatDateTime(
                        getApplicationContext(),
                        System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME
                                | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                new GetDataTask().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                new GetDataTask().execute();
            }
        });
    }

    private void initIndicator() {
        ILoadingLayout startLabels = mPullRefreshListView.getLoadingLayoutProxy(true, false);
        //刚下拉时,显示的提示
        startLabels.setPullLabel("您可劲拉,拉...");
        //刷新时
        startLabels.setRefreshingLabel("好勒,正在刷新...");
        //下拉达到一定距离时,显示的提示
        startLabels.setReleaseLabel("您敢放手,我敢刷新...");

        ILoadingLayout endLabels = mPullRefreshListView.getLoadingLayoutProxy(false,true);
        endLabels.setPullLabel("您可劲扯,扯...");
        endLabels.setRefreshingLabel("好勒,正在加载...");
        endLabels.setReleaseLabel("您敢放手,我敢加载...");

//        只能下拉,不能上拉
//        mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
    }

    private void initDatas() {
        mListItems = new LinkedList<String>();
        for (int i = 0; i < mItemCount; i++) {
            mListItems.add(i + "");
        }
    }

    private class GetDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mListItems.add("" + mItemCount++);
            mAdapter.notifyDataSetChanged();
            // Call onRefreshComplete when the list has been refreshed.
            mPullRefreshListView.onRefreshComplete();
        }
    }
}