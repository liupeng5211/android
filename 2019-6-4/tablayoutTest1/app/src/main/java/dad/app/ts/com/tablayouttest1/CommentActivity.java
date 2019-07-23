package dad.app.ts.com.tablayouttest1;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import dad.app.ts.com.tablayouttest1.adatper.CommentAdapter;
import dad.app.ts.com.tablayouttest1.base.BaseActivity;
import dad.app.ts.com.tablayouttest1.present.CommentPresenter;
import dad.app.ts.com.tablayouttest1.present.ICommentView;

public class CommentActivity extends BaseActivity implements ICommentView {
    private EditText commentEt;
    private ListView listView;
    private Button mButton;
    private TextView titleTv;
    private static String title;
    private static final String TAG = "CommentActivity";
    private CommentPresenter mCommentPresenter;
    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_activity);
        initView();
        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");
        String url = bundle.getString("url");
        Log.d(TAG, "title :  " + title);
        Log.d(TAG, "url :  " + url);
        titleTv.setText(title);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x010) {
                    CommentAdapter commentAdapter = (CommentAdapter) msg.obj;
                    listView.setAdapter(commentAdapter);
                }
                if (msg.what == 0x011) {
                    CommentAdapter commentAdapter = (CommentAdapter) msg.obj;
                    listView.setAdapter(commentAdapter);
                }
            }
        };
        mCommentPresenter.getComment(title);

    }


    private void initView() {
        mCommentPresenter = new CommentPresenter(this, this);
        mButton = findViewById(R.id.send);
        titleTv = findViewById(R.id.title);
        commentEt = (EditText) findViewById(R.id.comment_et);
        listView = findViewById(R.id.mylist);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = commentEt.getText().toString();
                Log.d(TAG, "comment: " + comment);
                Log.d(TAG, "title: " + title);
                mCommentPresenter.add(title, comment);

            }
        });
    }


    @Override
    public void addItem(CommentAdapter commentAdapter) {
        Message message = new Message();
        message.obj = commentAdapter;
        message.what = 0x011;
        mHandler.sendMessage(message);

    }

    @Override
    public void setAdapter(CommentAdapter commentAdapter) {
        Message message = new Message();
        message.obj = commentAdapter;
        message.what = 0x010;
        mHandler.sendMessage(message);
    }
}

