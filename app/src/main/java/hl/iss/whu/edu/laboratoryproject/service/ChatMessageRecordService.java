package hl.iss.whu.edu.laboratoryproject.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import hl.iss.whu.edu.laboratoryproject.entity.MessageRecord;
import hl.iss.whu.edu.laboratoryproject.entity.Result;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by fate on 2017/2/25.
 */

public class ChatMessageRecordService extends Service {
    private List<MessageRecord> mRecords = new ArrayList<>();
    private Timer timer;
    private TimerTask task;
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    private int SEND_INTERVAL = 1000*30;
    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                if (mRecords.size()!=0)
                    uploadMessageRecord(mRecords);
            }
        };
        timer.schedule(task,SEND_INTERVAL,SEND_INTERVAL);
    }

    private void uploadMessageRecord(final List<MessageRecord> records) {
        UiUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
//        Toast.makeText(ChatMessageRecordService.this, "发送了一次请求"+records, Toast.LENGTH_SHORT).show();
        RetrofitUtils.getService().uploadMessageRecord(gson.toJson(records))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Result>() {
                    @Override
                    public void accept(Result result) throws Exception {
//                        Toast.makeText(ChatMessageRecordService.this, "成功", Toast.LENGTH_SHORT).show();
                        mRecords.clear();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
//                        Toast.makeText(ChatMessageRecordService.this, "错误"+throwable, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }
    public void record(String from,String to){
//        Toast.makeText(this, from+"向"+to+"发送了一条消息", Toast.LENGTH_SHORT).show();
        for (MessageRecord record : mRecords) {
            if (record.getFromUid().equals(from)&&record.getToUid().equals(to)) {
                record.increment();
                return;
            }
        }
        MessageRecord record = new MessageRecord(from,to);
        record.increment();
        mRecords.add(record);
    }

    public class MyBinder extends Binder{
        public ChatMessageRecordService getService(){
            return ChatMessageRecordService.this;
        }
    }
}
