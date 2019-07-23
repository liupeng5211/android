package dad.app.ts.com.tablayouttest1.adatper;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dad.app.ts.com.tablayouttest1.CommentListView.CommentListView;
import dad.app.ts.com.tablayouttest1.R;
import dad.app.ts.com.tablayouttest1.model.CommentNew;

/**
 * Created by Hades on 17/2/4.
 */
public class CommentAdapter extends BaseAdapter {

    private Context context;
    private List<CommentNew> mList;


    public CommentAdapter(Context context, List<CommentNew> list) {
        this.context = context;
        this.mList = list;

    }

    @Override
    public int getCount() {
        int size = 0;
        if (mList != null) {
            size = mList.size();
        }
        return size;
    }

    @Override
    public Object getItem(int i) {
        return 0;
    }

    @Override
    public long getItemId(int i) {
        return (long) i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
        ImageView imageView = view.findViewById(R.id.user_hear_img);
        TextView userTv = view.findViewById(R.id.user_name);
        TextView contentTv = view.findViewById(R.id.content);
        TextView timeTv = view.findViewById(R.id.upload_time);
        Bitmap bitmap = mList.get(i).getBitmap();
        String user = mList.get(i).getUser();
        String content = mList.get(i).getContent();
        String time = mList.get(i).getTime();
        imageView.setImageBitmap(bitmap);
        userTv.setText(user);
        contentTv.setText(content);
        timeTv.setText(time);
        return view;
    }


}
