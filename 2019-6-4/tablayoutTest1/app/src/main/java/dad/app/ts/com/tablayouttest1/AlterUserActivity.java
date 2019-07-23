package dad.app.ts.com.tablayouttest1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;

import dad.app.ts.com.tablayouttest1.base.BaseActivity;
import dad.app.ts.com.tablayouttest1.present.ConnectSeverPresent;

public class AlterUserActivity extends BaseActivity {
    private static final String USERDATA = "userdata";
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mSharedPreferences;
    private EditText editText;
    private Button button;
    private ConnectSeverPresent connectSeverPresent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alter_user_activity);
        connectSeverPresent = new ConnectSeverPresent();
        System.out.println("jjjj");
        mSharedPreferences = getSharedPreferences(USERDATA, 0);
        mEditor = mSharedPreferences.edit();
        editText = findViewById(R.id.edit);
        button = findViewById(R.id.bt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = editText.getText().toString().trim();

                String name = mSharedPreferences.getString("username", "");
                Log.d("", "onClick: " + name);
                File file = new File(String.valueOf(getDir(name + ".jpg", MODE_PRIVATE)) + "/" + name + ".jpg");
                File newFile = new File(String.valueOf(getDir(user + ".jpg", MODE_PRIVATE)) + "/" + user + ".jpg");

                if (file.exists()) {
                    file.renameTo(newFile);
                    Log.d("", "修改成功: " + file);
                } else {
                    Log.d("", "onClick: 修改失败");
                }
                mEditor.putString("username", user);
                mEditor.apply();
                connectSeverPresent.alter(name, "name", user);
                Intent intent = new Intent();
                intent.putExtra("user", user);
                setResult(0x0, intent);
                finish();
            }
        });
    }
}
