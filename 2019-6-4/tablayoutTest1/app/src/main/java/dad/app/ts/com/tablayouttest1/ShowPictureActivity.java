package dad.app.ts.com.tablayouttest1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.ScrollView;
import android.widget.TextView;

import dad.app.ts.com.tablayouttest1.MyPopupwindow.FunPopWindow;
import dad.app.ts.com.tablayouttest1.base.BaseActivity;
import dad.app.ts.com.tablayouttest1.present.IShowPictureView;
import dad.app.ts.com.tablayouttest1.present.ShowPicturePresent;

public class ShowPictureActivity extends AppCompatActivity implements IShowPictureView {
    private TextView mTitleView;
    private LinearLayout mLinearLayout;
    private TextView mTextView;
    private ImageView mImageView;
    private ShowPicturePresent mShowPicturePresent;
    private Handler mHandler;
    private LinearLayout.LayoutParams mLayoutParams;
    private LinearLayout.LayoutParams mMatchLayoutParams;
    private static int position = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_activity);
        addToolBar();
        mTitleView = findViewById(R.id.title);
        mLinearLayout = findViewById(R.id.layout);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        position = bundle.getInt("position");
        String url = bundle.getString("url");
        String title = bundle.getString("title");
        mTitleView.setText(title);
        mLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mMatchLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //start get the info of page.
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x0033) {
                    Bundle bundle = msg.getData();
                    String text = bundle.getString("text");
                    mTextView = new TextView(getBaseContext());
                    mTextView.setText(text);
                    mTextView.setTextSize(20);
                    mTextView.setTextColor(Color.BLACK);
                    mLinearLayout.addView(mTextView, mLayoutParams);

                }
                if (msg.what == 0x1222) {
                    mImageView = new ImageView(getBaseContext());
                    final Bitmap bitmap = (Bitmap) msg.obj;
                    mImageView.setImageBitmap(bitmap);
                    mImageView.setLayoutParams(mMatchLayoutParams);
                    mLinearLayout.addView(mImageView, mMatchLayoutParams);

                    mImageView.setOnClickListener(new View.OnClickListener() {
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
                        }
                    });


                }
            }
        };
        mShowPicturePresent = new ShowPicturePresent(this, this);
        mShowPicturePresent.showNews(url);
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
                    bundle.putString("type", "picture_new");
                    FunPopWindow popupWindow = new FunPopWindow(ShowPictureActivity.this, bundle);
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

    @Override
    public void addImageView(Bitmap bitmap) {
        Message message = new Message();
        message.what = 0x1222;
        message.obj = bitmap;
        mHandler.sendMessage(message);
    }
}
