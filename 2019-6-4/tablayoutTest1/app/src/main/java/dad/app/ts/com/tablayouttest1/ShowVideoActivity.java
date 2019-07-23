package dad.app.ts.com.tablayouttest1;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import dad.app.ts.com.tablayouttest1.MyPopupwindow.FunPopWindow;
import dad.app.ts.com.tablayouttest1.base.BaseActivity;
import dad.app.ts.com.tablayouttest1.present.IShowVideoView;
import dad.app.ts.com.tablayouttest1.present.ShowVideoPresent;

public class ShowVideoActivity extends AppCompatActivity implements IShowVideoView {
    private TextView mTitleView;
    private LinearLayout mLinearLayout;
    private VideoView mVideoView;
    private MediaController mConteoller;
    private TextView mTextView;
    private ShowVideoPresent mShowVideoPresent;
    private Handler mHandler;
    private LinearLayout.LayoutParams mLayoutParams;
    private static int position = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_activity);
        addToolBar();
        mTitleView = findViewById(R.id.title);
        mVideoView = findViewById(R.id.video);
        mLinearLayout = findViewById(R.id.layout);
        mConteoller = new MediaController(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        position = bundle.getInt("position");
        String videourl = bundle.getString("videourl");
        String title = bundle.getString("title");
        String playurl = bundle.getString("url");
        mTitleView.setText(title);
        mLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        //start get the info of page.
        //
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x0033){
                    Bundle bundle = msg.getData();
                    String text = bundle.getString("text");
                    mTextView = new TextView(getBaseContext());
                    mTextView.setText(text);
                    mTextView.setTextSize(20);
                    mTextView.setTextColor(Color.BLACK);
                    mLinearLayout.addView(mTextView,mLayoutParams);

                }
            }
        };
        mShowVideoPresent = new ShowVideoPresent(this, this);
        mShowVideoPresent.showVideoNews(videourl);
        Uri uri = Uri.parse(playurl);
        mVideoView.setVideoURI(uri);
        mVideoView.setMediaController(mConteoller);
        mConteoller.setMediaPlayer(mVideoView);
        mVideoView.start();
    }
    protected void addToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView back = toolbar.findViewById(R.id.back);
        ImageView imageView = toolbar.findViewById(R.id.function);


        if (back != null) {
            Log.d("", "addToolBar: ");
            back.setClickable(true);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent();
                    i.putExtra("position", position);
                    setResult(4, i);

                    finish();
                }
            });
        }
        if (imageView != null) {
            Log.d("", "addToolBar: ");
            imageView.setClickable(true);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("", "onClick:function ");
                    Intent intent = getIntent();
                    Bundle bundle = intent.getExtras();
                    bundle.putString("type","video_new");
                    FunPopWindow popupWindow = new FunPopWindow(ShowVideoActivity.this, bundle);
                    popupWindow.showPopupWindow(v);


                }
            });
        }
    }
    @Override
    public void addTextView(String text) {
        Message message = new Message();
        message.what = 0x0033;
        Bundle bundle = new Bundle();
        bundle.putString("text", text);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }
}
