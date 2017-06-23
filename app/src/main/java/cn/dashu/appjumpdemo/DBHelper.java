package cn.dashu.appjumpdemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Eric on 2016/5/12.13:45
 * Email: 631554401@qq.com
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, Constant.DBNAME, null, Constant.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + Constant.TableName + "(" +
                Constant.ID + " integer primary key autoincrement not null," +
                Constant.PATH + " text not null," +
                Constant.MAX_TEMP + " double," +
                Constant.CENTER_TEMP + " double" +
                ");");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public void add(String path, double maxTemp, double centerTemp) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues mContentValues = new ContentValues();
        mContentValues.put(Constant.PATH, path);
        mContentValues.put(Constant.MAX_TEMP, maxTemp);
        mContentValues.put(Constant.CENTER_TEMP, centerTemp);

        db.insert(Constant.TableName, "", mContentValues);

    }

}
