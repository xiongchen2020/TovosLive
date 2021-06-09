package com.example.myapplication;

import android.app.Application;
import android.app.job.JobInfo;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Environment;

import com.example.commonlib.CommonContext;
import com.example.commonlib.utils.LogcatHelper;
import com.example.filgthhublibrary.HubContext;
import org.acra.*;
import org.acra.annotation.*;
import org.acra.data.StringFormat;

import androidx.multidex.MultiDex;

import static org.acra.ACRAConstants.DEFAULT_LOG_LINES;
import static org.acra.ACRAConstants.DEFAULT_RES_VALUE;

@AcraMailSender(mailTo = "xiongchen@tovos.cn",
        reportAsFile = false,
        reportFileName = "错误日志",resSubject = DEFAULT_RES_VALUE,resBody =DEFAULT_RES_VALUE
)
public class MApplication extends Application {


    public static String path = Environment.getExternalStorageDirectory()+"/tovos/";
    public static String wayPath = path+"waypoint/";
    public static String picPath = path+"pic/";
    public static boolean isTable = false;
    public static LogcatHelper logcatHelper;

    private static Application app = null;

    @Override
    public void onCreate() {
        super.onCreate();
        CommonContext.setContext(getApplicationContext());
        HubContext.setContext(getApplicationContext());
        ACRA.init(this);
    }

    /**
     * 判断是否平板设备
     * @param context
     * @return true:平板,false:手机
     */
    public static boolean isTabletDevice(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE;
    }


    public static Application getInstance(){
        return app;
    }

    @Override
    protected void attachBaseContext(Context paramContext) {
        super.attachBaseContext(paramContext);
        MultiDex.install(this);
        com.secneo.sdk.Helper.install(this);
        app = this;
    }
}
