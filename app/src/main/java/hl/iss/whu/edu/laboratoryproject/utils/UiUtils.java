package hl.iss.whu.edu.laboratoryproject.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Process;
import android.view.View;

import hl.iss.whu.edu.laboratoryproject.BaseApplication;


/**
 * Created by fate on 2016/10/20.
 */

public class UiUtils {
    public static Context getContext(){
        return BaseApplication.getContext();
    }
    public static Handler getHandler(){
        return  BaseApplication.getHandler();
    }
    public static View inflate(int id){
        return View.inflate(getContext(),id,null);
    }
    public static boolean isRunOnUiThread(){
        return Process.myTid() == BaseApplication.getMainThreadId();
    }

    public static void runInMainThread(Runnable runnable){
        if (isRunOnUiThread()) {
            runnable.run();
        }else {
            getHandler().post(runnable);
        }
    }
}
