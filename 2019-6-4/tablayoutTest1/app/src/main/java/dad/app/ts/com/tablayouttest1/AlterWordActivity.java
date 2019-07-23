package dad.app.ts.com.tablayouttest1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import dad.app.ts.com.tablayouttest1.base.BaseActivity;
import dad.app.ts.com.tablayouttest1.present.ConnectSeverPresent;

public class AlterWordActivity extends BaseActivity {
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
                mEditor.putString("word", user);
                mEditor.apply();
                String name = mSharedPreferences.getString("username", "");
                connectSeverPresent.alter(name, "myword", user);
                Intent intent = new Intent();
                intent.putExtra("word", user);
                setResult(0x2, intent);
                finish();
            }
        });
    }
}
