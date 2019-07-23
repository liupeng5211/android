package dad.app.ts.com.tablayouttest1.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import dad.app.ts.com.tablayouttest1.adatper.CollectNewsAdapter;
import dad.app.ts.com.tablayouttest1.util.CollectDbHelper;
import dad.app.ts.com.tablayouttest1.util.DbControl;

public class GetNewsDb {
    public void getNews(final NetGetCollectNews.localback localback, Context context) {
        //get collect news from db.
        CollectDbHelper collectDbHelper = new CollectDbHelper(context, "collect_news", null, 1);
        List<CollectNews> list = new ArrayList<>();
        SQLiteDatabase database = collectDbHelper.getReadableDatabase();
        DbControl dbControl = new DbControl();
        String username = context.getSharedPreferences("userdata", 0).getString("username", "null");
        if (username != null && username.equals("null")) {
            localback.error("请先登录！");
            return;
        }
        Cursor cursor = dbControl.select(database, "collect_news", "user", username);
        int count = cursor.getCount();
        Log.d("", "count: " + count);
        if (count == 0) {
            localback.error("本地数据库没有数据，请下拉刷新");
            CollectNewsAdapter adapter = new CollectNewsAdapter(context, list);
            localback.success(adapter);
            return;
        }
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String type = cursor.getString(cursor.getColumnIndex("type"));
            String url = cursor.getString(cursor.getColumnIndex("url"));
            String videourl = cursor.getString(cursor.getColumnIndex("videourl"));
            Log.d("", "title: " + title);
            Log.d("", "type: " + type);
            Log.d("", "url: " + url);
            Log.d("", "videourl: " + videourl);
            CollectNews collectNews = new CollectNews();
            collectNews.setTitle(title);
            collectNews.setType(type);
            collectNews.setUrl(url);
            collectNews.setVideourl(videourl);
            list.add(collectNews);
        }
        CollectNewsAdapter adapter = new CollectNewsAdapter(context, list);
        localback.success(adapter);
        database.close();
        collectDbHelper.close();

    }
}
