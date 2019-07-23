package dad.app.ts.com.tablayouttest1.adatper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

import dad.app.ts.com.tablayouttest1.R;
import dad.app.ts.com.tablayouttest1.model.CollectNews;

public class CollectNewsAdapter extends BaseAdapter {
    private List<CollectNews> mList = null;
    private Context mContext;


    public CollectNewsAdapter(Context context, List<CollectNews> mList) {
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
        View view1 = LayoutInflater.from(mContext).inflate(R.layout.list_collect_view, null);
        TextView titleTv = view1.findViewById(R.id.title);
        TextView urlTv = view1.findViewById(R.id.url);
        TextView typeTv = view1.findViewById(R.id.type);
        TextView videourlTv = view1.findViewById(R.id.videourl);
        if (mList != null) {
            titleTv.setText(mList.get(i).getTitle());
            urlTv.setText(mList.get(i).getUrl());
            typeTv.setText(mList.get(i).getType());
            videourlTv.setText(mList.get(i).getVideourl());
        }

        return view1;
    }
}
