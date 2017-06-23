package cn.dashu.appjumpdemo;

import android.net.Uri;

/**
 * Created by Bob on 2016/5/9.
 */
public class Constant {

    public static final String AUTHORITY = "cn.dashu.appjumpdemo";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY +"/infrared");

    public static final String DBNAME = "KeNeng.db";
    public static final String TableName = "infrared";
    public static final int VERSION = 1;

    public static String ID = "_id";
    public static final String PATH = "Path";
    public static final String MAX_TEMP = "MaxTemp";
    public static final String CENTER_TEMP = "CenterTemp";

    public static final int ITEM = 1;
    public static final int ITEM_ID = 2;

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.KeNeng";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.KeNeng";


}
