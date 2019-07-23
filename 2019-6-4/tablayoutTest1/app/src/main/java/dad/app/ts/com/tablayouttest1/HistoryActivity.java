package dad.app.ts.com.tablayouttest1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import dad.app.ts.com.tablayouttest1.MyPopupwindow.DeleteHistoryPopupWindow;
import dad.app.ts.com.tablayouttest1.MyPopupwindow.DeletePopupWindow;
import dad.app.ts.com.tablayouttest1.adatper.CollectNewsAdapter;
import dad.app.ts.com.tablayouttest1.base.BaseActivity;
import dad.app.ts.com.tablayouttest1.present.HistoryPresenter;
import dad.app.ts.com.tablayouttest1.present.ICollectView;
import dad.app.ts.com.tablayouttest1.present.ICommentView;
import dad.app.ts.com.tablayouttest1.present.IHistoryView;
import dad.app.ts.com.tablayouttest1.util.HistoryDbHelper;

public class HistoryActivity extends BaseActivity implements IHistoryView, ICollectView {
    private ListView mListView;
    private HistoryPresenter mHistoryPresenter;
    private Handler mHandler;
    private Button mAllDeleteBt;
    private static final String USERDATA = "userdata";
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_activity);
        mListView = findViewById(R.id.history_list);
        mAllDeleteBt = findViewById(R.id.all_delete);
        mAllDeleteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSharedPreferences = getSharedPreferences(USERDATA, 0);
                final String username = mSharedPreferences.getString("username", "");
                HistoryDbHelper historyDbHelper = new HistoryDbHelper(HistoryActivity.this, "history_news", null, 1);
                int count = historyDbHelper.getReadableDatabase().delete("history_news", "user = ?", new String[]{username});
                Log.d("", "count: " + count);
                refreshAdapter();
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
                DeleteHistoryPopupWindow popupWindow = new DeleteHistoryPopupWindow(HistoryActivity.this, bundle, HistoryActivity.this);
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
                    Intent intent = new Intent(HistoryActivity.this, ShowNewsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", i);
                    bundle.putString("title", title);
                    bundle.putString("url", url);
                    intent.putExtras(bundle);
                    startActivity(intent);


                }
                if (type.equals("picture_new")) {
                    Intent intent = new Intent(HistoryActivity.this, ShowPictureActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("position", i);
                    bundle.putString("title", title);
                    bundle.putString("url", url);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }
                if (type.equals("video_new")) {
                    Intent intent = new Intent(HistoryActivity.this, ShowVideoActivity.class);
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
        mHistoryPresenter = new HistoryPresenter(this, this);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x0) {

                    Log.d("", "handleMessage: 开始设置adapter");
                    CollectNewsAdapter adapter = (CollectNewsAdapter) msg.obj;
                    mListView.setAdapter(adapter);
                }
                if (msg.what == 0x1) {

                    String message = (String) msg.obj;
                    Toast.makeText(HistoryActivity.this, message, Toast.LENGTH_LONG).show();

                }
            }
        };
        mHistoryPresenter.getData();

    }

    @Override
    public void setAdapter(CollectNewsAdapter collectNewsAdapter) {
        Message message = new Message();
        message.obj = collectNewsAdapter;
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
        mHistoryPresenter.getData();
    }

    @Override
    public void showMessage(String text) {
        Message message = new Message();
        message.obj = text;
        message.what = 0x1;
        mHandler.sendMessage(message);
    }
}
