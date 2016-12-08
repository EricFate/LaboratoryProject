package hl.iss.whu.edu.laboratoryproject;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Process;

import hl.iss.whu.edu.laboratoryproject.manager.SmackManager;

/**
 * Created by fate on 2016/10/20.
 */

public class BaseApplication extends Application {
    private static Context context;
    private static Handler handler;
    private static int mainThreadId;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        handler = new Handler(getMainLooper());
        mainThreadId = Process.myTid();
    }


    public static Context getContext() {
        return context;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static int getMainThreadId() {
        return mainThreadId;
    }

    @Override
    public void onTerminate() {
        if(SmackManager.getConnection().isConnected()){
            SmackManager.getConnection().disconnect();
        }
        super.onTerminate();
    }
}
