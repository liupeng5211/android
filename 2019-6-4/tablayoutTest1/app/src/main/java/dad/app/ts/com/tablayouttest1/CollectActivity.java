package dad.app.ts.com.tablayouttest1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import dad.app.ts.com.tablayouttest1.MyPopupwindow.DeletePopupWindow;
import dad.app.ts.com.tablayouttest1.MyPopupwindow.FunPopWindow;
import dad.app.ts.com.tablayouttest1.adatper.CollectNewsAdapter;
import dad.app.ts.com.tablayouttest1.base.BaseActivity;
import dad.app.ts.com.tablayouttest1.present.CollectPresenter;
import dad.app.ts.com.tablayouttest1.present.ConnectSeverPresent;
import dad.app.ts.com.tablayouttest1.present.ICollectView;
import dad.app.ts.com.tablayouttest1.util.CollectDbHelper;
import dad.app.ts.com.tablayouttest1.util.HistoryDbHelper;

public class CollectActivity extends BaseActivity implements ICollectView {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView;
    private Handler mHandler;
    private CollectPresenter mCollectPresenter;
    private Button mAllDeleteBt;
    private static final String USERDATA = "userdata";
    private SharedPreferences mSharedPreferences;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect_activity);
        mSwipeRefreshLayout = findViewById(R.id.refresh);
        mListView = findViewById(R.id.listview);
        mAllDeleteBt = findViewById(R.id.all_delete);
        mAllDeleteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mSharedPreferences = getSharedPreferences(USERDATA, 0);
                final String username = mSharedPreferences.getString("username", "");
                ConnectSeverPresent connectSeverPresent = new ConnectSeverPresent(CollectActivity.this);
                connectSeverPresent.deleteAllCollect(username, CollectActivity.this);

            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView titleTv = view.findViewById(R.id.title);
                String title = titleTv.getText().toString();
                Log.d("", "title: " + title);
                Bundle bundle = new Bundle();
                bundle.putString("title", title);
                DeletePopupWindow popupWindow = new DeletePopupWindow(CollectActivity.this, bundle, CollectActivity.this);
                popupWindow.showPopupWindow(view);


                return true;
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("", "onItemClick: " + i);
                TextView typeTv = view.findViewById(R.id.type);
                TextView titleTv = view.findViewById(R.id.title);
                TextView urlTv = view.findViewById(R.id.url);
                TextView videoTv = view.findViewById(R.id.videourl);
                String title = titleTv.getText().toString();
                String type = typeTv.getText().toString();
                String url = urlTv.getText().toString();
                String videourl = videoTv.getText().toString();
                if (type.equals("usual_new")) {
                    Intent intent = new Intent(CollectActivity.this, ShowNewsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", i);
                    bundle.putString("title", title);
                    bundle.putString("url", url);
                    intent.putExtras(bundle);
                    startActivity(intent);


                }
                if (type.equals("picture_new")) {
                    Intent intent = new Intent(CollectActivity.this, ShowPictureActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", i);
                    bundle.putString("title", title);
                    bundle.putString("url", url);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }
                if (type.equals("video_new")) {
                    Intent intent = new Intent(CollectActivity.this, ShowVideoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", i);
                    bundle.putString("title", title);
                    bundle.putString("videourl", url);
                    bundle.putString("url", videourl);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }


            }
        });

        mCollectPresenter = new CollectPresenter(this, this);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x0) {
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);

                    }
                    Log.d("", "handleMessage: 开始设置adapter");
                    CollectNewsAdapter adapter = (CollectNewsAdapter) msg.obj;
                    adapter.notifyDataSetChanged();
                    mListView.setAdapter(adapter);
                }
                if (msg.what == 0x1) {
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);

                    }
                    String message = (String) msg.obj;
                    Toast.makeText(CollectActivity.this, message, Toast.LENGTH_LONG).show();

                }
            }
        };
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("", "onRefresh: ");
                mCollectPresenter.getdata();

            }
        });
        mCollectPresenter.getdata();
    }


    @Override
    public void setAdapter(CollectNewsAdapter adapter) {
        Message message = new Message();
        message.obj = adapter;
        message.what = 0x0;
        mHandler.sendMessage(message);


    }

    @Override
    public void tip(String text) {
        Message message = new Message();
        message.obj = text;
        message.what = 0x1;
        mHandler.sendMessage(message);


    }

    @Override
    public void refreshAdapter() {
        mCollectPresenter.getdata();
    }
}
