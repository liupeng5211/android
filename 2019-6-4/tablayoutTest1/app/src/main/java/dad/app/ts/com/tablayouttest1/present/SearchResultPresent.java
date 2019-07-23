package dad.app.ts.com.tablayouttest1.present;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import dad.app.ts.com.tablayouttest1.adatper.SearchNewListAdapter;
import dad.app.ts.com.tablayouttest1.model.SearchNew;
import dad.app.ts.com.tablayouttest1.util.SearchDbHelper;

public class SearchResultPresent {
    private Context mContext;
    private static final String TAG = "SearchResultPresent";
    private ISearchResultView mISearchResultView;
    private static String USER_AGENT = "User-Agent";
    private static String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko Core/1.63.6821.400 QQBrowser/10.3.3040.400";

    public SearchResultPresent(Context mContext, ISearchResultView mISearchResultView) {
        this.mContext = mContext;
        this.mISearchResultView = mISearchResultView;
    }

    /**
     * search usual news.
     *
     * @param info the info of search news.
     */
    public void searchUsualNews(final String info) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SearchDbHelper searchDbHelper = new SearchDbHelper(mContext, "news", null, 1);
                SQLiteDatabase sqLiteDatabase = searchDbHelper.getReadableDatabase();
                Cursor cursor = sqLiteDatabase.rawQuery("select * from news where title LIKE '%" + info + "%';", null);
                List<SearchNew> list = new ArrayList<>();
                Log.d(TAG, "执行游标之前");
//                cursor.moveToFirst();如幻
                Log.d(TAG, "cursor count " + cursor.getCount());
                while (cursor.moveToNext()) {
                    String title = "";
                    String url = "";

                    for (int i = 0; i < cursor.getCount(); i++) {
                        title = cursor.getString(cursor.getColumnIndex("title"));
                        url = cursor.getString(cursor.getColumnIndex("url"));
                    }
                    SearchNew searchNew = new SearchNew();
                    searchNew.setType(SearchNew.USUAL_NEW);
                    if (!title.equals("")) {
                        searchNew.setTitle(title);
                    }
                    if (!url.equals("")) {
                        searchNew.setUrl(url);
                    }
                    list.add(searchNew);
                }
                Log.d(TAG, "执行游标之后:" + list.size());
                if (list.size()==0){
                    mISearchResultView.setNullResult();
                    return;
                }
                SearchNewListAdapter searchNewListAdapter = new SearchNewListAdapter(list, mContext);
                mISearchResultView.setAdapter(searchNewListAdapter);
                sqLiteDatabase.close();
            }
        }).start();

    }
}
