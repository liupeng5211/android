package dad.app.ts.com.tablayouttest1.adatper;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

import dad.app.ts.com.tablayouttest1.R;
import dad.app.ts.com.tablayouttest1.model.User;

public class UserListAdapter extends BaseAdapter {
    private List<User> mUserList;
    private Context mContext;

    public UserListAdapter(List<User> mUserList, Context context) {
        this.mUserList = mUserList;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (mUserList != null) {
            count = mUserList.size();
        }
        return count;
        //need
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //need
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_view_picture, null);
        TextView tilte = view.findViewById(R.id.title);
        ImageView image = view.findViewById(R.id.image);
        TextView time = view.findViewById(R.id.time);
        TextView url = view.findViewById(R.id.url);

        String age = mUserList.get(position).getmUesrAge();
        String name = mUserList.get(position).getmUserName();
        String sex = mUserList.get(position).getmUserSex();
        Bitmap bitmap = mUserList.get(position).getmImage();
        tilte.setText(name);
        image.setImageBitmap(bitmap);
        time.setText(age);
        url.setText(sex);
        return view;
    }
}
