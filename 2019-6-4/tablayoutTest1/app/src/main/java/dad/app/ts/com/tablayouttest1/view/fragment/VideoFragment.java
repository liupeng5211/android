package dad.app.ts.com.tablayouttest1.view.fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import dad.app.ts.com.tablayouttest1.R;
import dad.app.ts.com.tablayouttest1.ShowVideoActivity;
import dad.app.ts.com.tablayouttest1.present.ConnectSeverPresent;
import dad.app.ts.com.tablayouttest1.util.HistoryDbHelper;

public class VideoFragment extends Fragment implements IVideoView {
    private static final String TAG = "VideoFragment";
    private ConnectSeverPresent mSeverPresent;
    private ProgressBar mProgressBar;
    private AnimationSet textAnimationSet;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SimpleAdapter mSimpleAdapter;
    private ListView mListView;
    private static int mPage = 1;
    private static int currentStatue = 0;
    private static int currentItem = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x77) {
                mProgressBar.setVisibility(View.GONE);
                mSimpleAdapter.notifyDataSetChanged();
                mListView.setAdapter(mSimpleAdapter);
                mSwipeRefreshLayout.setRefreshing(false);
                mListView.setSelection(currentItem);

                currentStatue = 0;
            }
            if (msg.what == 0x987) {
                Bundle bundle = msg.getData();
                int value = bundle.getInt("value");
                mProgressBar.setProgress(value);
            }
            if (msg.what == 0x988) {
                Bundle bundle = msg.getData();
                String value = bundle.getString("info");
                mProgressBar.setIndeterminate(false);
                Toast.makeText(getContext(), value + "视频链接超时！", Toast.LENGTH_SHORT).show();
            }
            if (msg.what == 0x001234) {
                Bundle bundle = msg.getData();
                int position = bundle.getInt("position");

//                listView.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.whitesmoke));
                mListView.setSelection(position);

            }
            if (msg.what == 0x984) {
                Bundle bundle = msg.getData();
                Boolean value = bundle.getBoolean("flag");
                mProgressBar.setIndeterminate(value);
            }
            if (msg.what == 0x0000) {
                mSwipeRefreshLayout.setRefreshing(false);
                mProgressBar.setVisibility(View.GONE);
                currentStatue = 0;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_fragment, container, false);
        mListView = view.findViewById(R.id.list);
        mProgressBar = view.findViewById(R.id.annimotion);
        mSwipeRefreshLayout = view.findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSeverPresent.initVideo(mPage);
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }

            }
        });
//        LinearGradient mLinearGradient = new LinearGradient(0, 0, 0, mTextView.getPaint().getTextSize(), Color.WHITE, Color.BLACK, Shader.TileMode.CLAMP);
//        mTextView.getPaint().setShader(mLinearGradient);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView titleText = view.findViewById(R.id.title);
                String title = titleText.getText().toString();
                TextView videourlText = view.findViewById(R.id.videourl);
                String videourl = videourlText.getText().toString();
                TextView playurlText = view.findViewById(R.id.playurl);
                String playurl = playurlText.getText().toString();
                HistoryDbHelper historyDbHelper = new HistoryDbHelper(getActivity(), "history_news", null, 1);
                SQLiteDatabase database = historyDbHelper.getReadableDatabase();
                ContentValues contentValues = new ContentValues();
                String user = getActivity().getSharedPreferences("userdata", 0).getString("username", "null");
                contentValues.put("user", user);
                contentValues.put("title", title);
                contentValues.put("url", videourl);
                contentValues.put("type", "video_new");
                contentValues.put("videourl", playurl);
                database.insert("history_news", null, contentValues);
                database.close();
                historyDbHelper.close();

                Intent intent = new Intent(getContext(), ShowVideoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", playurl);
                bundle.putString("title", title);
                bundle.putString("videourl", videourl);
                bundle.putInt("position", position);
                intent.putExtras(bundle);
                startActivityForResult(intent, 123);

            }
        });
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if (i == 0) {
                    Log.d("ListView", "##### 滚动到顶部 #####");
                } else if ((i1 + i) == i2) {
                    Log.d("ListView", "##### 滚动到底部 ######");
                    if (currentStatue == 0) {
                        currentStatue = 1;
                        mPage = mPage + 1;
                        currentItem = i2;
                        mSwipeRefreshLayout.setRefreshing(true);
                        mSeverPresent.initVideo(mPage);
                        mProgressBar.setVisibility(View.VISIBLE);

                    }


                }

            }
        });
        mSeverPresent = new ConnectSeverPresent(this, getContext());
        mSeverPresent.initVideo(1);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "onResume: ");

    }

    @Override
    public void setAdapter(SimpleAdapter simpleAdapter) {
        this.mSimpleAdapter = simpleAdapter;
        mHandler.sendEmptyMessage(0x77);


    }

    @Override
    public void setValue(int value) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putInt("value", value);
        message.setData(bundle);
        message.what = 0x987;
        mHandler.sendMessage(message);
    }

    @Override
    public void showMessage(String message) {
        Message message1 = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("info", message);
        message1.what = 0x988;
        mHandler.sendMessage(message1);
    }

    @Override
    public void setProgressMode(boolean flag) {
        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putBoolean("flag", flag);
        message.what = 0x984;
        mHandler.sendMessage(message);
    }

    @Override
    public void error() {
        mHandler.sendEmptyMessage(0x0000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 123 && resultCode == 4) {
            int position = data.getIntExtra("position", 1);
            Message message = new Message();
            message.what = 0x001234;
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            message.setData(bundle);
            mHandler.sendMessage(message);

        }
    }
}
