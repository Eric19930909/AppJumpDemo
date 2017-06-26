package cn.dashu.appjumpdemo;

import android.provider.BaseColumns;

/**
 * Created by lushujie on 2017/6/26.
 */

public class InfraredInfo implements BaseColumns {

    /**
     * 表名
     */
    public static final String TABLE_NAME = "infrared";
    /**
     * 图片名称，后缀“.jpg”换成“.raw”为Raw图路径
     */
    public static final String COLUMN_PATH = "path";
    /**
     * Raw图中的最高温度值
     */
    public static final String COLUMN_MAX_TEMP = "maxTemp";
    /**
     * Raw图中的最低温度值
     */
    public static final String COLUMN_MIN_TEMP = "minTemp";
    /**
     * Raw图中的中心温度值
     */
    public static final String COLUMN_CENTER_TEMP = "centerTemp";
    /**
     * Raw图中的第一个选点温度值
     */
    public static final String COLUMN_TOUCH_TEMP = "touchTemp";

}
