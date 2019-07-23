package dad.app.ts.com.tablayouttest1;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;

/**
 * 启动页面的设计
 */

public class SplashActivity extends Activity {

    //显示广告页面的时间，3 秒
    long showTime = 3;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private static final int MY_PERMISSIONS_REQUEST_CALL_CAMERA = 2;
    private static final String TAG = "SplashActivity";
    String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE

    };
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 111) {

                Log.e("TAG", "倒计时" + showTime);

                showTime--;//时间减一秒
                if (showTime > 0) {
                    handler.sendEmptyMessageDelayed(111, 1000);//一秒后給自己发送一个信息
                }

            }
            if (msg.what == 112) {
                handler.postDelayed(myRunnable, showTime * 1000);
                handler.sendEmptyMessage(111);//給Handler对象发送信息
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        File file = new File(String.valueOf(this.getDir("headImage.jpg", this.MODE_PRIVATE))+"/headImage.jpg");
        if (file.exists()) {
            Log.d(TAG, "image exists!");
        }
        ActivityCompat.requestPermissions(this, permissions, MY_PERMISSIONS_REQUEST_CALL_CAMERA);

    }

    //创建Handler对象


    //创建Runnable对象
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            jundToMainActivity();
        }
    };

    //跳转到主页的方法，并关闭自身页面
    public void jundToMainActivity() {
        Intent intent = new Intent(SplashActivity.this,
                MainActivity.class);
        startActivity(intent);
        finish();
    }

    //关闭页面
    public void closeSplash(View view) {
        Log.e("TAG", "closeSplash");
        handler.removeCallbacks(myRunnable);//移出Runnable对象
        jundToMainActivity();

    }

    //回退键的监听方法，
    // 这里如果直接关闭页面，线程没有关闭的话，5秒后还是会启动主页面，除非移出线程对象
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "广告之后更精彩！", Toast.LENGTH_SHORT).show();

        //如果按回退键，关闭程序，代码设计
        finish();//关闭页面
        handler.removeCallbacks(myRunnable);//取消runnable对象

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult: +" + requestCode);

        handler.sendEmptyMessage(112);


    }


}
