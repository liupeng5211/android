package dad.app.ts.com.tablayouttest1.adatper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import dad.app.ts.com.tablayouttest1.R;
import dad.app.ts.com.tablayouttest1.model.CommentNew;

public class MyCommentAdapter extends BaseAdapter {
    private List<CommentNew> mList;
    private Context mContext;

    public MyCommentAdapter(Context context, List<CommentNew> mList) {
        this.mList = mList;
        this.mContext = context;
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
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1 = View.inflate(mContext, R.layout.my_comment_list, null);
        TextView titleTv = view1.findViewById(R.id.title);
        TextView timeTv = view1.findViewById(R.id.time);
        TextView comentTv = view1.findViewById(R.id.comment);
        titleTv.setText(mList.get(i).getTitle());
        timeTv.setText(mList.get(i).getTime());
        comentTv.setText(mList.get(i).getContent());
        return view1;
    }
}
