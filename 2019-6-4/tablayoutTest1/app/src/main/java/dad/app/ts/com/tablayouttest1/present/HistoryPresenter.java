package dad.app.ts.com.tablayouttest1.present;

import android.content.Context;

import dad.app.ts.com.tablayouttest1.adatper.CollectNewsAdapter;
import dad.app.ts.com.tablayouttest1.model.HistoryNews;

public class HistoryPresenter implements IHistoryPresenter, HistoryNews.IHistoryLocalback {
    private Context mContext;
    private IHistoryView iHistoryView;
    private HistoryNews mHistoryNews;

    public HistoryPresenter(Context mContext, IHistoryView iHistoryView) {
        this.mContext = mContext;
        this.iHistoryView = iHistoryView;
        this.mHistoryNews = new HistoryNews();
    }

    @Override
    public void getData() {
        mHistoryNews.getDataFromDb(this, mContext);

    }

    @Override
    public void success(CollectNewsAdapter adapter) {
        iHistoryView.setAdapter(adapter);


    }

    @Override
    public void error(String message) {

    }
}
