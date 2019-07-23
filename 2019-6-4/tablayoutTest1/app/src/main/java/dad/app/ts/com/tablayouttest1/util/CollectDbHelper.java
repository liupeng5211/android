package dad.app.ts.com.tablayouttest1.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CollectDbHelper extends SQLiteOpenHelper {
    public CollectDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void create(SQLiteDatabase db) {
        StringBuffer stringBuffer = new StringBuffer();
        /**
         * CREATE TABLE news
         * (
         * title TEXT,
         * url TEXT,
         * );
         */
        stringBuffer.append("CREATE TABLE collect_news(");
        stringBuffer.append("user TEXT,");
        stringBuffer.append("title TEXT,");
        stringBuffer.append("url TEXT,");
        stringBuffer.append("type TEXT,");
        stringBuffer.append("videourl TEXT");
        stringBuffer.append(");");
        String sql = stringBuffer.toString();
        db.execSQL(sql);
    }
}
