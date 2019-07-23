package dad.app.ts.com.tablayouttest1.MyPopupwindow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import dad.app.ts.com.tablayouttest1.R;
import dad.app.ts.com.tablayouttest1.RegisterActivity;
import dad.app.ts.com.tablayouttest1.present.ConnectSeverPresent;
import dad.app.ts.com.tablayouttest1.util.GetImageData;

public class AddPopWindow extends PopupWindow implements IPopWindowView {
    private View conentView;
    private static final String USER = "user";
    private static final String PASSWORD = "password";
    private static final String CHECK = "check";
    private Context mContext;
    private ConnectSeverPresent mConnectSeverPresent;
    private static final String TAG = "popwindow";
    private static final String USERDATA = "userdata";
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mSharedPreferences;
    private static Handler mHandler;

    public AddPopWindow(final Activity context) {
        this.mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(USERDATA, 0);
        mEditor = mSharedPreferences.edit();
        mConnectSeverPresent = new ConnectSeverPresent(mContext, this);
        boolean flag = mSharedPreferences.getBoolean(CHECK, false);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.add_popup_dialog, null);
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);
        Button close = conentView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                AddPopWindow.this.dismiss();
            }
        });
        final EditText userText = (EditText) conentView
                .findViewById(R.id.user);
        final EditText passwordText = (EditText) conentView
                .findViewById(R.id.password);
        if (flag) {
            userText.setText(mSharedPreferences.getString(USER, ""));
        }

        final Button login = conentView.findViewById(R.id.login);
        final Button register = conentView.findViewById(R.id.register);
        login.requestFocus();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0x323) {
                    Bundle bundle = msg.getData();
                    String message = bundle.getString("message");
                    String word = bundle.getString("word");
                    String image = bundle.getString("image");
                    boolean imgflag = bundle.getBoolean("imgflag");
                    Log.d(TAG, "handleMessage: " + message);
                    if (message.equals("successed")) {
                        mEditor.putBoolean("login", true);
                        Log.d(TAG, "handleMessage: " + userText.getText().toString());
                        Log.d(TAG, "handleMessage: " + passwordText.getText().toString());
                        mEditor.putString("username", userText.getText().toString());
                        mEditor.putString("password", passwordText.getText().toString());
                        mEditor.putString("image", image);
                        mEditor.putBoolean("imgflag", imgflag);
                        mEditor.putString("word", word);
                        mEditor.apply();
                        GetImageData getImageData = new GetImageData(mContext);
                        getImageData.writeImgStr(image, userText.getText().toString());
                        dismiss();
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    } else {
                        login.setText("登录");
                        userText.setEnabled(true);
                        passwordText.setEnabled(true);
                        passwordText.setText("");

                        Toast.makeText(mContext, "用户名或密码不正确！", Toast.LENGTH_LONG).show();
                    }

                }

            }
        };
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                Intent intent = new Intent(mContext, RegisterActivity.class);
                mContext.startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                userText.setEnabled(false);
                passwordText.setEnabled(false);
                String name = userText.getText().toString();
                String password = passwordText.getText().toString();
                if (password.equals("") && name.equals("")) {
                    Toast.makeText(mContext, "用户名密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                login.setText("正在登录。。");
                //add to connect sever,  user and  password if true;
                //if true , save user and password.

                mConnectSeverPresent.login(name, password);


//                mEditor.putString(USER, name);
//                mEditor.putString(PASSWORD, password);
//                mEditor.apply();
                Log.d(TAG, "name: " + name);
                Log.d(TAG, "password: " + password);


            }
        });

    }


    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
//            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 18);
            this.showAtLocation(parent, 0, 0, 0);
        } else {
            this.dismiss();
        }
    }


    @Override
    public void showMessage(String message, String image, String word, boolean imgflag) {
        Message message1 = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("message", message);
        bundle.putString("image", image);
        bundle.putString("word", word);
        bundle.putBoolean("imgflag", imgflag);

        message1.setData(bundle);
        message1.what = 0x323;
        mHandler.sendMessage(message1);

    }
}
