package dad.app.ts.com.tablayouttest1;

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

import dad.app.ts.com.tablayouttest1.MyPopupwindow.DeleteCommentPopupWindow;
import dad.app.ts.com.tablayouttest1.MyPopupwindow.DeletePopupWindow;
import dad.app.ts.com.tablayouttest1.adatper.CollectNewsAdapter;
import dad.app.ts.com.tablayouttest1.adatper.MyCommentAdapter;
import dad.app.ts.com.tablayouttest1.base.BaseActivity;
import dad.app.ts.com.tablayouttest1.present.ConnectSeverPresent;
import dad.app.ts.com.tablayouttest1.present.ICollectView;
import dad.app.ts.com.tablayouttest1.present.IMyCommentView;
import dad.app.ts.com.tablayouttest1.present.MyCommentPresenter;

public class MyCommentActivity extends BaseActivity implements IMyCommentView, ICollectView {
    private ListView mListView;
    private Handler mHandler;
    private MyCommentPresenter mMyCommentPresenter;
    private Button mAllDeleteBt;
    private static final String USERDATA = "userdata";
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.my_comment_activity);
        mAllDeleteBt = findViewById(R.id.all_delete);
        mAllDeleteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSharedPreferences = getSharedPreferences(USERDATA, 0);
                final String username = mSharedPreferences.getString("username", "");
                ConnectSeverPresent connectSeverPresent = new ConnectSeverPresent(MyCommentActivity.this);
                connectSeverPresent.deleteAllComment(username, MyCommentActivity.this);


            }
        });
        mListView = findViewById(R.id.my_comment_list);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView titleTv = view.findViewById(R.id.title);
                TextView commentTv = view.findViewById(R.id.comment);
                String title = titleTv.getText().toString();
                String comment = commentTv.getText().toString();
                Log.d("", "title: " + title);
                Bundle bundle = new Bundle();
                bundle.putString("title", title);
                bundle.putString("content", comment);
                DeleteCommentPopupWindow popupWindow = new DeleteCommentPopupWindow(MyCommentActivity.this, bundle, MyCommentActivity.this);
                popupWindow.showPopupWindow(view);


                return false;
            }
        });
        mMyCommentPresenter = new MyCommentPresenter(this, this);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x01) {
                    MyCommentAdapter commentAdapter = (MyCommentAdapter) msg.obj;
                    commentAdapter.notifyDataSetChanged();
                    mListView.setAdapter(commentAdapter);
                }
                if (msg.what == 0x02) {
                    String message = (String) msg.obj;
                    Toast.makeText(MyCommentActivity.this, message, Toast.LENGTH_LONG).show();
                }

            }
        };
        mMyCommentPresenter.getData();

    }

    @Override
    public void setAdapter(MyCommentAdapter commentAdapter) {
        Message message = new Message();
        message.obj = commentAdapter;
        message.what = 0x01;
        mHandler.sendMessage(message);

    }

    @Override
    public void showMesage(String text) {
        Message message = new Message();
        message.obj = text;
        message.what = 0x02;
        mHandler.sendMessage(message);
    }

    @Override
    public void setAdapter(CollectNewsAdapter adapter) {

    }

    @Override
    public void tip(String text) {
        Message message = new Message();
        message.obj = text;
        message.what = 0x02;
        mHandler.sendMessage(message);

    }

    @Override
    public void refreshAdapter() {
        mMyCommentPresenter.getData();

    }
}
