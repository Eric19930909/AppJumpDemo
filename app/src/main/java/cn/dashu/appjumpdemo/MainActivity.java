package cn.dashu.appjumpdemo;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView tvMaxTemp;
    private TextView tvCenterTemp;

    private ImageView mImage;

    private Button btnJump;

    public static final String INFRARED_PACKAGE_NAME = "com.yado.camera";

    public static final int REQUEST_CODE_FOR_INFRARED = 0x1000;

    public static final String JUMP_INFO = "cn.dashu.appjumpdemo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0x0001);
        }

        initView();
        initEvent();

    }

    private void initView() {

        tvMaxTemp = (TextView) findViewById(R.id.max_temp);
        tvCenterTemp = (TextView) findViewById(R.id.center_temp);

        mImage = (ImageView) findViewById(R.id.image);

        btnJump = (Button) findViewById(R.id.jump);

    }

    private void initEvent() {

        btnJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //缺少返回的结果
                if (isAppInstalled(MainActivity.this, INFRARED_PACKAGE_NAME)) {

                    Intent mLaunchIntent = MainActivity.this.getPackageManager().getLaunchIntentForPackage(INFRARED_PACKAGE_NAME);
                    mLaunchIntent.putExtra("infrared", JUMP_INFO);

//                    isInfrared = true;

                    startActivityForResult(mLaunchIntent, REQUEST_CODE_FOR_INFRARED);
                } else {
                    Toast.makeText(MainActivity.this, "没有安装红外双视...", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        ContentResolver mResolver = getContentResolver();
        Cursor mCursor = mResolver.query(Uri.parse("content://cn.dashu.appjumpdemo/infrared"), null, null, null, null);
        if (mCursor != null) {
            //会执行完毕后清空数据，故游标只有一条记录
            while (mCursor.moveToNext()) {

                String path = mCursor.getString(1);

                Log.e(TAG, "onResume: " + path);

                Bitmap bitmap = BitmapFactory.decodeFile(path);

                mImage.setImageBitmap(bitmap);

                double maxTemp = mCursor.getDouble(2);
                double centerTemp = mCursor.getDouble(3);

                tvMaxTemp.setText(String.format(Locale.getDefault(), "%.2f", maxTemp));
                tvCenterTemp.setText(String.format(Locale.getDefault(), "%.2f", centerTemp));

//                Bitmap bm = mBitmapUtil.decodeFile(newFile, mImageButtonAddImage.getWidth(), mImageButtonAddImage.getHeight());
//                if (!mInfraredPathList.contains(path)) {
//                    mInfraredPathList.add(path);
//
//                    //添加操作
//                    ImageView newImage = new ImageView(CatchActivity.this);
//
//                    mInfraredFileList.add(newFile);
//
//                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                            mImageButtonAddInfrared.getWidth(), mImageButtonAddInfrared.getHeight());
//                    lp.gravity = Gravity.CENTER;
//                    lp.setMargins(7, 7, 7, 7);
//
//                    newImage.setLayoutParams(lp);
//
//                    newImage.setImageBitmap(bm);
//
//                    final Uri uri = Uri.fromFile(new File(path));
//                    newImage.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            Intent intent = new Intent();
//                            intent.setAction(Intent.ACTION_VIEW);
//                            intent.setDataAndType(uri, "image/jpeg");
//                            startActivity(intent);
//
//                        }
//                    });
//
//                    mLinearLayoutInfrared.addView(newImage);
//
//                }

            }

            //清空数据
            mResolver.delete(Uri.parse("content://cn.dashu.appjumpdemo/infrared"), Constant.PATH + "!=?", new String[]{" "});

            mCursor.close();

        }

    }

    //检查是否存在此包名的应用程序
    private boolean isAppInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfo = packageManager.getInstalledPackages(0);
        List<String> pName = new ArrayList<String>();
        if (packageInfo != null) {
            for (int i = 0; i < packageInfo.size(); i++) {
                String pn = packageInfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);
    }

}
