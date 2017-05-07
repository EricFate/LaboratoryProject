package hl.iss.whu.edu.laboratoryproject;

import android.app.Application;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Process;
import android.view.View;

import java.io.IOException;

import hl.iss.whu.edu.laboratoryproject.constant.Constant;
import hl.iss.whu.edu.laboratoryproject.entity.Info;
import hl.iss.whu.edu.laboratoryproject.listener.RecordSendMessageListener;
import hl.iss.whu.edu.laboratoryproject.service.ChatMessageRecordService;
import hl.iss.whu.edu.laboratoryproject.ui.activity.PersonalInfoActivity;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.vov.vitamio.Vitamio;
import retrofit2.Response;

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
        RongIM.init(this);
        Vitamio.isInitialized(this);
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

        super.onTerminate();
    }
}
