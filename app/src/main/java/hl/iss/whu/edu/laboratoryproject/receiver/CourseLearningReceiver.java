package hl.iss.whu.edu.laboratoryproject.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import hl.iss.whu.edu.laboratoryproject.entity.Result;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by fate on 2017/2/25.
 */

public class CourseLearningReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int sid = intent.getIntExtra("sid", 0);
        int cid = intent.getIntExtra("cid", 0);
        long duration = intent.getLongExtra("duration",0);
        if (cid!=0&&sid!=0&&duration!=0)
            uploadCourseLearning(cid,sid,duration,context);

    }

    private void uploadCourseLearning(int cid, int sid, long duration, final Context context) {
        Toast.makeText(context, "发送了一次请求", Toast.LENGTH_SHORT).show();
        RetrofitUtils.getService().uploadCourseLearning(sid,cid,duration).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Result>() {
                    @Override
                    public void accept(Result result) throws Exception {
                        Toast.makeText(context, "成功", Toast.LENGTH_SHORT).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(context, "错误:"+throwable, Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
