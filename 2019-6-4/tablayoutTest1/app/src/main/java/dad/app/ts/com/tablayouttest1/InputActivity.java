package dad.app.ts.com.tablayouttest1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dad.app.ts.com.tablayouttest1.base.BaseActivity;

public class InputActivity extends BaseActivity {
    private EditText mEdit;
    private Button mBt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_activity);
        mEdit = findViewById(R.id.edit);
        mBt = findViewById(R.id.define);
        mBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = mEdit.getText().toString();
                if (url.equals("")) {
                    Toast.makeText(InputActivity.this, "url不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent(InputActivity.this, ShowNewsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", url);
                bundle.putString("title", url);
                bundle.putInt("position", 0);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
}
