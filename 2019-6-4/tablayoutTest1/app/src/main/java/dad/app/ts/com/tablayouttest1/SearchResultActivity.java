package dad.app.ts.com.tablayouttest1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import dad.app.ts.com.tablayouttest1.GradientShaderTextView.GradientTextView;
import dad.app.ts.com.tablayouttest1.adatper.SearchNewListAdapter;
import dad.app.ts.com.tablayouttest1.base.BaseActivity;
import dad.app.ts.com.tablayouttest1.present.ISearchResultView;
import dad.app.ts.com.tablayouttest1.present.SearchResultPresent;

public class SearchResultActivity extends BaseActivity implements ISearchResultView {
    private GradientTextView mTextView;
    private SearchResultPresent mSearchResultPresent;
    private ListView mListView;
    private SearchNewListAdapter mSearchNewListAdapter;
    private String mHint;
    private Timer mTimer;
    private String searchText;
    private static final String TAG = "SearchResultActivity";
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x123) {
                if (mSearchNewListAdapter != null) {
                    mTextView.setVisibility(View.GONE);
                    mListView.setAdapter(mSearchNewListAdapter);
                }
            }
            if (msg.what == 0x125) {
                mTimer.cancel();
                mTextView.setText("没有搜索到和" + searchText + "有关的关键字");
            }
            if (msg.what == 0x124) {
                String text = mTextView.getText().toString();
                Log.d(TAG, "text = " + text);
                if (text.equals(mHint)) {
                    mTextView.setText(mHint + ".");
                }
                if (text.equals(mHint + ".")) {
                    mTextView.setText(mHint + "..");
                }
                if (text.equals(mHint + "..")) {
                    mTextView.setText(mHint + "...");
                }
                if (text.equals(mHint + "...")) {
                    mTextView.setText(mHint);
                }


            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result_activity);
        mSearchResultPresent = new SearchResultPresent(this, this);
        searchText = getIntent().getStringExtra("search_info");
        mTextView = findViewById(R.id.title);
        mListView = findViewById(R.id.list);
        mHint = "正在搜索关键字‘" + searchText + "’";
        mTextView.setText(mHint);
        mTimer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Log.d(TAG, "task start");
                mHandler.sendEmptyMessage(0x124);
            }
        };
        mTimer.schedule(task, 0, 500);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView titleText = view.findViewById(R.id.title);
                TextView urlText = view.findViewById(R.id.url);
                String title = titleText.getText().toString();
                String url = urlText.getText().toString();
                Log.d("标题和网址", title + ", " + url);
                Intent intent = new Intent(SearchResultActivity.this, ShowNewsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", url);
                bundle.putString("title", title);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        mSearchResultPresent.searchUsualNews(searchText);

    }

    //日本

    @Override
    public void setAdapter(SearchNewListAdapter searchNewListAdapter) {
        mTimer.cancel();
        this.mSearchNewListAdapter = searchNewListAdapter;
        mSearchNewListAdapter.notifyDataSetChanged();
        mHandler.sendEmptyMessage(0x123);
    }

    @Override
    public void setNullResult() {
        mHandler.sendEmptyMessage(0x125);
    }
}
