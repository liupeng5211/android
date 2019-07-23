package dad.app.ts.com.tablayouttest1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import dad.app.ts.com.tablayouttest1.base.BaseActivity;

public class SettingActivity extends BaseActivity {
//    private Button userBt;
    private Button pwdBt;
    private Button wordBt;
    private Button exitBt;
    private TextView userTv;
    private TextView wordTv;
    private static final String TAG = "popwindow";
    private static final String USERDATA = "userdata";
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mSharedPreferences;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        exitBt = findViewById(R.id.exit);
//        userBt = findViewById(R.id.user);
        pwdBt = findViewById(R.id.password);
        wordBt = findViewById(R.id.my_word);
//        userTv = findViewById(R.id.user_text);
        wordTv = findViewById(R.id.word_text);
//        userBt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(SettingActivity.this, AlterUserActivity.class);
//                startActivityForResult(intent, 0x0);
//
//
//            }
//        });
        exitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSharedPreferences = getSharedPreferences(USERDATA, 0);
                mEditor = mSharedPreferences.edit();
                mEditor.putBoolean("login", false);
//                mEditor.putString("username", "Android Studio");
//                mEditor.putString("password", "");
//                mEditor.putString("image", "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");
//                mEditor.putBoolean("imgflag", true);
//                mEditor.putString("word", "android.studio@android.com");
                mEditor.apply();
                Toast.makeText(SettingActivity.this, "您已成功注销", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        pwdBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, AlterPasswordActivity.class);
                startActivityForResult(intent, 0x1);
            }
        });
        wordBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingActivity.this, AlterWordActivity.class);
                startActivityForResult(intent, 0x2);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        if (requestCode == 0x0 && resultCode == 0x0) {
            String user = intent.getStringExtra("user");
            if (!user.equals("")) {
                userTv.setText(user);
            }


        }
        if (requestCode == 0x1 && resultCode == 0x1) {


        }
        if (requestCode == 0x2 && resultCode == 0x2) {
            String word = intent.getStringExtra("word");
            if (!word.equals(""))
                wordTv.setText(word);


        }
    }
}
