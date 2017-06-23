package cn.dashu.appjumpdemo;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Eric on 2016/5/12.11:52
 * Email: 631554401@qq.com
 */
public class MyContentProvider extends ContentProvider {

    DBHelper mDBHelper;
    SQLiteDatabase mDatabase;

    private static final UriMatcher sMATCHER;

    static {
        sMATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        sMATCHER.addURI(Constant.AUTHORITY, Constant.TableName, Constant.ITEM);
        sMATCHER.addURI(Constant.AUTHORITY, Constant.TableName + "/#", Constant.ITEM_ID);

    }

    @Override
    public boolean onCreate() {

        mDBHelper = new DBHelper(getContext());

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        mDatabase = mDBHelper.getWritableDatabase();
        Cursor c;
        Log.i("-------", String.valueOf(sMATCHER.match(uri)));
        switch (sMATCHER.match(uri)) {
            case Constant.ITEM:
                c = mDatabase.query(Constant.TableName, projection, selection, selectionArgs, null, null, null);

                break;
            case Constant.ITEM_ID:
                String id = uri.getPathSegments().get(1);
                c = mDatabase.query(Constant.TableName, projection, Constant.ID + "=" + id + (!TextUtils.isEmpty(selection) ? "AND(" + selection + ')' : ""),
                        selectionArgs, null, null, sortOrder);
                break;
            default:
                Log.i("!!!!!!", "Unknown URI" + uri);
                throw new IllegalArgumentException("Unknown URI" + uri);
        }
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        switch (sMATCHER.match(uri)) {
            case Constant.ITEM:
                return Constant.CONTENT_TYPE;
            case Constant.ITEM_ID:
                return Constant.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI" + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        mDatabase = mDBHelper.getWritableDatabase();

        long rowId;

        if (sMATCHER.match(uri) != Constant.ITEM) {
            throw new IllegalArgumentException("Unknown URI" + uri);
        }

        rowId = mDatabase.insert(Constant.TableName, Constant.ID, values);

        if (rowId > 0) {
            Uri noteUri = ContentUris.withAppendedId(Constant.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }

        throw new IllegalArgumentException("Unknown URI" + uri);

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        mDatabase = mDBHelper.getWritableDatabase();
        int count = 0;
        switch (sMATCHER.match(uri)) {
            case 1:
                count = mDatabase.delete(Constant.TableName, selection, selectionArgs);
                break;

            case 2:
                String id = uri.getPathSegments().get(1);
                count = mDatabase.delete(Constant.ID, Constant.ID + "=" + id + (!TextUtils.isEmpty(Constant.ID = "?") ? "AND(" + selection + ')' : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        mDatabase = mDBHelper.getWritableDatabase();
        int count = 0;

        switch (sMATCHER.match(uri)) {

            case 1:
                count = mDatabase.update(Constant.TableName, values, selection, selectionArgs);
                break;

            case 2:

                break;

            default:
                throw new IllegalArgumentException("Unknown URI" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }
}
