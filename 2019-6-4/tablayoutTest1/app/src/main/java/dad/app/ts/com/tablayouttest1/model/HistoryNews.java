package dad.app.ts.com.tablayouttest1.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import dad.app.ts.com.tablayouttest1.adatper.CollectNewsAdapter;
import dad.app.ts.com.tablayouttest1.util.HistoryDbHelper;

public class HistoryNews implements IHistory {
    @Override
    public void getDataFromDb(IHistoryLocalback localback, Context context) {
        String user = context.getSharedPreferences("userdata", 0).getString("username", "null");
        if (user.equals("null")) {
            localback.error("请先登录！");
            return;
        }

        HistoryDbHelper historyDbHelper = new HistoryDbHelper(context, "history_news", null, 1);
        SQLiteDatabase database = historyDbHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from history_news where user = ?", new String[]{user});
        List<CollectNews> list = new ArrayList<>();
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
        historyDbHelper.close();

    }

    public interface IHistoryLocalback {
        public void success(CollectNewsAdapter adapter);

        public void error(String message);
    }

}
