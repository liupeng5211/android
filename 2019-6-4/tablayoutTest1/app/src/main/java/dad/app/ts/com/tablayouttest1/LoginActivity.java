package dad.app.ts.com.tablayouttest1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import dad.app.ts.com.tablayouttest1.adatper.UserListAdapter;
import dad.app.ts.com.tablayouttest1.base.BaseActivity;
import dad.app.ts.com.tablayouttest1.model.User;

public class LoginActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ListView listView = findViewById(R.id.list);
        List<User> list = initInfo();
        UserListAdapter adapter = new UserListAdapter(list, this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public List<User> initInfo() {
        List<User> list = new ArrayList<>();
        User user = new User();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.s23);
        user.setmImage(bitmap);
        user.setmUesrAge(18 + "");
        user.setmUserName("liupeng");
        user.setmUserSex("nan");
        list.add(user);
        return list;


    }
}
