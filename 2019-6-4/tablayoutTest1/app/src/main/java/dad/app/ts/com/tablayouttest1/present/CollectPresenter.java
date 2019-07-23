package dad.app.ts.com.tablayouttest1.present;

import android.content.Context;

import dad.app.ts.com.tablayouttest1.adatper.CollectNewsAdapter;
import dad.app.ts.com.tablayouttest1.model.GetNewsDb;
import dad.app.ts.com.tablayouttest1.model.NetGetCollectNews;

public class CollectPresenter implements NetGetCollectNews.localback, ICollectPresenter {
    private Context mContext;
    private ICollectView mCollectView;
    private NetGetCollectNews mNetGetCollectNews;
    private GetNewsDb mGetNewsDb;

    public CollectPresenter(Context mContext, ICollectView mCollectView) {
        this.mContext = mContext;
        this.mCollectView = mCollectView;
        mNetGetCollectNews = new NetGetCollectNews();
        mGetNewsDb = new GetNewsDb();

    }

    @Override
    public void success(CollectNewsAdapter adapter) {
        mCollectView.setAdapter(adapter);
    }

    @Override
    public void error(String message) {
        mCollectView.tip(message);

    }

    @Override
    public void getdata() {
        mNetGetCollectNews.getCollectNews(this, mContext);

    }

    @Override
    public void getdataFromDb() {

        mGetNewsDb.getNews(this, mContext);

    }
}
