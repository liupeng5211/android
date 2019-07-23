package dad.app.ts.com.tablayouttest1;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dad.app.ts.com.tablayouttest1.base.BaseActivity;
import dad.app.ts.com.tablayouttest1.constant.ImageInfo;
import dad.app.ts.com.tablayouttest1.present.IRegisterView;
import dad.app.ts.com.tablayouttest1.present.RegisterPresenter;

public class RegisterActivity extends BaseActivity implements IRegisterView {
    private static final String TAG = "RegisterActivity";
    private EditText mUserEt;
    private EditText mPwdEt;
    private EditText mWordEt;
    private Button mRegisterBt;
    private RegisterPresenter mRegisterPresenter;
    private Handler mHandler;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity_view);
        initView();
        mRegisterPresenter = new RegisterPresenter(this, this);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x08) {
                    String message = (String) msg.obj;
                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
                    mUserEt.setText("");

                }
                if (msg.what == 0x09) {
                    RegisterActivity.this.finish();

                }
            }
        };
        mRegisterBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: ");
                String user = mUserEt.getText().toString();
                String pwd = mPwdEt.getText().toString();
                String word = mWordEt.getText().toString();
                String imageInfo = ImageInfo.imageInfo;
                Log.d(TAG, "user: " + user);
                Log.d(TAG, "pwd: " + pwd);
                Log.d(TAG, "word: " + word);
                Log.d(TAG, "imageInfo: " + imageInfo);
                Bundle bundle = new Bundle();
                bundle.putString("user", user);
                bundle.putString("password", pwd);
                bundle.putString("word", word);
                bundle.putString("imageInfo", imageInfo);
                mRegisterPresenter.connectServer(bundle);
                mRegisterBt.setText("正在注册。。");
            }
        });

    }

    public void initView() {
        mUserEt = findViewById(R.id.user);
        mPwdEt = findViewById(R.id.password);
        mWordEt = findViewById(R.id.word);
        mRegisterBt = findViewById(R.id.register);
    }

    @Override
    public void loading() {

    }

    @Override
    public void showMessage(String tip) {
        Message message = new Message();
        message.what = 0x08;
        message.obj = tip;
        mHandler.sendMessage(message);

    }

    @Override
    public void successRegister() {

        mHandler.sendEmptyMessage(0x09);

    }


}
