package com.duke.yinyangli;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.util.Log;

import com.bytedance.boost_multidex.BoostMultiDex;
import com.bytedance.boost_multidex.Result;
import com.duke.yinyangli.bean.database.DaoMaster;
import com.duke.yinyangli.bean.database.DaoSession;
import com.duke.yinyangli.constants.Constants;
import com.duke.yinyangli.utils.SqliteUtil;
import com.lxj.xpopup.XPopup;
import com.tencent.mmkv.MMKV;

import java.util.List;

public class MyApplication extends Application {

    private static MyApplication sInstance;

    private DaoMaster.DevOpenHelper mHelper;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        BoostMultiDex.install(base);

        Result result = BoostMultiDex.install(this);
        if (result != null && result.fatalThrowable != null) {
            Log.e("BMD", "exception occored " + result.fatalThrowable);
        }
    }

    private void setDatabase() {
        //1.创建数据库
        mHelper = new DaoMaster.DevOpenHelper(this, Constants.DB_NAME, null);
        //2.获取读写对象
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.disableWriteAheadLogging();
        //3.获取管理器类
        mDaoMaster = new DaoMaster(db);
        //4.获取表对象
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDao() {
        return mDaoSession;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        String rootDir = MMKV.initialize(this);
        System.out.println("mmkv root: " + rootDir);

        XPopup.setPrimaryColor(getResources().getColor(R.color.colorPrimary));
        XPopup.setAnimationDuration(200);

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    SqliteUtil.copyDataBase(MyApplication.this, Constants.DB_NAME);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                setDatabase();
            }
        });
    }

    public static MyApplication getInstance() {
        if (sInstance != null) {
            return sInstance;
        } else {
            return null;
        }
    }

    public static Context getContext() {
        if (sInstance != null) {
            return sInstance.getApplicationContext();
        } else {
            return null;
        }
    }

    private String getProcessName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo proInfo : runningApps) {
            if (proInfo.pid == android.os.Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName;
                }
            }
        }
        return null;
    }
}
