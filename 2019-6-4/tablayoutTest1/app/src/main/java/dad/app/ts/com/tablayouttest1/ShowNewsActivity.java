package dad.app.ts.com.tablayouttest1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.VideoView;

import dad.app.ts.com.tablayouttest1.MyPopupwindow.FunPopWindow;
import dad.app.ts.com.tablayouttest1.base.BaseActivity;
import dad.app.ts.com.tablayouttest1.present.IShowNewsView;
import dad.app.ts.com.tablayouttest1.present.ShowNewsPresent;

/**
 * show news activity.
 */
public class ShowNewsActivity extends AppCompatActivity implements IShowNewsView, View.OnClickListener {
    private LinearLayout mLinearLayout;
    private ShowNewsPresent showNewsPresent;
    private LinearLayout.LayoutParams mLayoutParams;
    private TextView mTitleText;
    private MediaController mConteoller;
    private VideoView mVideoView;
    private TextView textView;
    private ImageView imageView;
    private Handler mHandler;
    private String mtext;
    private String mimgsrc;
    private static int position = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_news);
        addToolBar();
        mLinearLayout = (LinearLayout) findViewById(R.id.layout);
        mVideoView = findViewById(R.id.video);
        mConteoller = new MediaController(this);
        mTitleText = findViewById(R.id.title);
        showNewsPresent = new ShowNewsPresent(this, this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        position = bundle.getInt("position");
        String url = bundle.getString("url");
        String title = bundle.getString("title");

        mTitleText.setText(title);

        mLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x1233) {
                    Bundle bundle1 = msg.getData();
                    String text = bundle1.getString("text");

                    textView = new TextView(getBaseContext());
                    textView.setText(text);
                    textView.setTextSize(20);
                    textView.setTextColor(Color.BLACK);
                    mLinearLayout.addView(textView, mLayoutParams);
                }
                if (msg.what == 0x1222) {
                    DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

                    imageView = new ImageView(getBaseContext());
                    final ImageView imageView1 = new ImageView(getBaseContext());
                    final Bitmap bitmap = (Bitmap) msg.obj;
                    imageView.setImageBitmap(bitmap);
                    imageView1.setImageBitmap(bitmap);
                    imageView.setMaxWidth(displayMetrics.widthPixels);
                    imageView.setMaxHeight(displayMetrics.heightPixels);
                    imageView.setLayoutParams(mLayoutParams);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final ImageView imageView2 = findViewById(R.id.image);
                            final ScrollView scrollView = findViewById(R.id.scrollview);
                            final LinearLayout linearLayout = findViewById(R.id.linear_layout);
                            final Toolbar toolbar = findViewById(R.id.toolbar);
                            linearLayout.setBackgroundColor(Color.BLACK);
                            toolbar.setVisibility(View.GONE);
                            scrollView.setVisibility(View.GONE);
                            imageView2.setVisibility(View.VISIBLE);
                            imageView2.setImageBitmap(bitmap);
                            imageView2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    scrollView.setVisibility(View.VISIBLE);
                                    imageView2.setVisibility(View.GONE);
                                    toolbar.setVisibility(View.VISIBLE);
                                    linearLayout.setBackgroundColor(Color.parseColor("#F5F5F5"));

                                }
                            });


//                            Intent intent1 = new Intent(ShowNewsActivity.this, PictureActivity.class);
//                            intent1.putExtra("bitmap", bitmap);
//                            startActivity(intent1);
                        }
                    });


                    mLinearLayout.addView(imageView, mLayoutParams);

                }
                if (msg.what == 0x1244) {
                    mVideoView.setVisibility(View.VISIBLE);
                    Bundle bundle1 = msg.getData();
                    String videoUrl = bundle1.getString("videoUrl");
                    Log.d("接收到视频增加消息--", videoUrl);


                    if (!videoUrl.equals("")) {
                        Uri uri = Uri.parse(videoUrl);
                        mVideoView.setVideoURI(uri);
                        mVideoView.setMediaController(mConteoller);
                        mConteoller.setMediaPlayer(mVideoView);
                        mVideoView.start();

                    }


                }


            }
        };

        showNewsPresent.showNews(url);
//        mLinearLayout.addView();

    }

    @Override
    public void onBackPressed() {
        Log.d("", "onBackPressed:");
        Intent i = new Intent();
        i.putExtra("position", position);
        setResult(4, i);

        finish();

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
                    bundle.putString("type", "usual_new");
                    FunPopWindow popupWindow = new FunPopWindow(ShowNewsActivity.this, bundle);
                    popupWindow.showPopupWindow(v);


                }
            });
        }
    }

    @Override
    public void addTextView(String text) {
        Message message = new Message();
        message.what = 0x1233;
        Bundle bundle = new Bundle();
        bundle.putString("text", text);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }

    @Override
    public void addImageView(Bitmap bitmap) {
        Message message = new Message();
        message.what = 0x1222;
        message.obj = bitmap;
        mHandler.sendMessage(message);
    }

    @Override
    public void addVideoView(String videoUrl) {
        Message message = new Message();
        message.what = 0x1244;
        Bundle bundle = new Bundle();
        bundle.putString("videoUrl", videoUrl);
        message.setData(bundle);
        mHandler.sendMessage(message);

    }


    @Override
    public void onClick(View v) {
        Log.d("", "onClick: ");
    }
}

