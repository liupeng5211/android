package dad.app.ts.com.tablayouttest1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dad.app.ts.com.tablayouttest1.base.BaseActivity;
import dad.app.ts.com.tablayouttest1.present.ConnectSeverPresent;

public class AlterPasswordActivity extends BaseActivity {
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
        mSharedPreferences = getSharedPreferences(USERDATA, 0);
        mEditor = mSharedPreferences.edit();
        editText = findViewById(R.id.edit);
        button = findViewById(R.id.bt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = editText.getText().toString();
                if (user.equals("")) {
                    Toast.makeText(AlterPasswordActivity.this, "密码不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                mEditor.putString("password", user);
                mEditor.apply();
                String name = mSharedPreferences.getString("username", "");
                connectSeverPresent.alter(name, "password", user);
                Intent intent = new Intent();
                intent.putExtra("password", user);
                setResult(0x1, intent);
                finish();
            }
        });
    }
}
