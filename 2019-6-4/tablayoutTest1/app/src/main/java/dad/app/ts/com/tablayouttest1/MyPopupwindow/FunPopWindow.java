package dad.app.ts.com.tablayouttest1.MyPopupwindow;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import dad.app.ts.com.tablayouttest1.CommentActivity;
import dad.app.ts.com.tablayouttest1.R;
import dad.app.ts.com.tablayouttest1.present.ConnectSeverPresent;

public class FunPopWindow extends PopupWindow {
    private View conentView;
    private Context mContext;
    private Bundle mBundle;
    private static final String TAG = "popwindow";
    private ConnectSeverPresent connectSeverPresent;
    private static final String USERDATA = "userdata";
    private SharedPreferences mSharedPreferences;


    public FunPopWindow(final Activity context, final Bundle bundle) {

        this.mContext = context;
        this.mBundle = bundle;
        connectSeverPresent = new ConnectSeverPresent(mContext);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.function_view, null);
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(200);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xbbbbbbbb);
//        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);
        final ListView listView = conentView.findViewById(R.id.listview);
        mSharedPreferences = mContext.getSharedPreferences(USERDATA, 0);
        final String username = mSharedPreferences.getString("username", "");
        if (username.equals("")) {
            Log.d(TAG, "FunPopWindow: " + "user为空字符串");
        }

        final boolean loginstatus = mSharedPreferences.getBoolean("login", false);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = mBundle.getString("title");
                String url = mBundle.getString("url");
                String type = mBundle.getString("type");
                Log.d(TAG, "title : " + title);
                Log.d(TAG, "url : " + url);
                Log.d(TAG, "type : " + type);
                String videourl = "";
                if (type.equals("video_new")) {
                    videourl = mBundle.getString("url");
                }
                Log.d(TAG, "videourl : " + videourl);
                Log.d(TAG, "position : " + position);
                if (position == 0) {
                    if (!loginstatus) {
                        Toast.makeText(mContext, "请先登录", Toast.LENGTH_LONG).show();
                        return;
                    }
                    //搜藏
                    connectSeverPresent.collectNews(type, username, title, url, videourl);
                    dismiss();


                } else if (position == 1) {
                    //分享
                    ClipboardManager clip = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    clip.setText(url);
                    dismiss();


                } else if (position == 2) {
                    //评论
                    if (!loginstatus) {
                        Toast.makeText(mContext, "请先登录", Toast.LENGTH_LONG).show();
                        return;
                    }
                    Intent intent = new Intent(mContext, CommentActivity.class);
                    intent.putExtras(mBundle);
                    mContext.startActivity(intent);
                    dismiss();


                }

            }
        });


    }

    /**
     * 显示popupWindow
     *
     * @param parent parent view.
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 18);
//            this.showAtLocation(parent, 0, 0, 0);
        } else {
            this.dismiss();
        }
    }
}
