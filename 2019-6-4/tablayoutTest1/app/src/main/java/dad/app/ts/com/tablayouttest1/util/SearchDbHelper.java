package dad.app.ts.com.tablayouttest1.util;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//森林
public class SearchDbHelper extends SQLiteOpenHelper {
    public SearchDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public SearchDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
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
        stringBuffer.append("CREATE TABLE news(");
        stringBuffer.append("title TEXT,");
        stringBuffer.append("url TEXT");
        stringBuffer.append(");");
        String sql = stringBuffer.toString();
        db.execSQL(sql);
    }
}
