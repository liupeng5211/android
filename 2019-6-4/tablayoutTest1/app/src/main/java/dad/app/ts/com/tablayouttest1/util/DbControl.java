package dad.app.ts.com.tablayouttest1.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DbControl {
    /**
     * delete the table all data.
     *
     * @param mSqLiteDatabase the SQLiteDatabase.
     * @param tableName       the table name.
     */
    public void delete(SQLiteDatabase mSqLiteDatabase, String tableName) {
        /**
         * SQLiteDatabase的delete方法签名为delete(String table,String whereClause,String[] whereArgs)，这个删除的参数说明如下：
         * table：代表想删除数据的表名。
         * whereClause：满足该whereClause子句的记录将会被删除。
         * whereArgs：用于为whereArgs子句传入参数。
         * 删除person_inf表中所有人名以孙开头的记录
         * int result=db.delete("person_inf","person_name like ?",new String[]{"孙_"});
         */
        mSqLiteDatabase.delete(tableName, null, null);

    }

    /**
     * delete the table part data.
     *
     * @param mSqLiteDatabase the SQLiteDatabase.
     * @param tableName       the table name.
     * @param element         the delete element.
     * @param value           the delete element value.
     */
    public void delete(SQLiteDatabase mSqLiteDatabase, String tableName, String element, String[] value) {
        mSqLiteDatabase.delete(tableName, element, value);

    }

    /**
     * select info from db.
     *
     * @param mSqLiteDatabase sqlitedatabase.
     * @param tablename       tablename.
     * @param element         select element.
     * @param value           the element value.
     */
    public Cursor select(SQLiteDatabase mSqLiteDatabase, String tablename, String element, String value) {
        Cursor cursor = null;
        if (element == null) {
            cursor = mSqLiteDatabase.rawQuery("select * from " + tablename, null);

        } else {
            cursor = mSqLiteDatabase.rawQuery("select * from " + tablename + " where " + element + "=?", new String[]{value});
        }

        return cursor;
    }


    /**
     * insert the info in datebase.
     *
     * @param mSqLiteDatabase SQLiteDatabase object.
     * @param contentValues   ContentValues.
     * @param tablename       the tablename.
     */
    public void insert(SQLiteDatabase mSqLiteDatabase, ContentValues contentValues, String tablename, String element) {
        if (mSqLiteDatabase != null) {
            Log.d("", "insert: " + contentValues.getAsString(element));
            boolean flag = ifExist(mSqLiteDatabase, element, contentValues.getAsString(element), tablename);
            if (flag) {
                return;
            } else {
                mSqLiteDatabase.insert(tablename, null, contentValues);
            }
        }
    }

    /**
     * judge the news if exist.
     *
     * @param mSqLiteDatabase the SQLiteDatabase.
     * @param element         the element need to judge if exists.
     * @param values          the info need to judge if exists.
     * @param tablename       the table name.
     * @return return the boolean.the true exists.the false not exists.
     */
    private boolean ifExist(SQLiteDatabase mSqLiteDatabase, String element, String values, String tablename) {
        boolean flag = false;

        Cursor cursor = mSqLiteDatabase.rawQuery("select * from " + tablename + " where " + element + " = ?;", new String[]{values});
        int count = cursor.getCount();
        if (count > 0) {
            flag = true;
        }
        return flag;
    }

}
