package dad.app.ts.com.tablayouttest1.adatper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import dad.app.ts.com.tablayouttest1.R;
import dad.app.ts.com.tablayouttest1.model.SearchNew;

public class SearchNewListAdapter extends BaseAdapter {
    private List<SearchNew> mList;
    private Context mContext;

    public SearchNewListAdapter(List<SearchNew> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
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
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(mContext, R.layout.search_result_list_view, null);
        TextView titleTv = view.findViewById(R.id.title);
        TextView urlTv = view.findViewById(R.id.url);
        TextView typeTv = view.findViewById(R.id.type);
        String title = mList.get(position).getTitle();
        String type = mList.get(position).getType();
        String url = mList.get(position).getUrl();
        titleTv.setText(title);
        urlTv.setText(url);
        typeTv.setText(type);
        return view;
    }
}
