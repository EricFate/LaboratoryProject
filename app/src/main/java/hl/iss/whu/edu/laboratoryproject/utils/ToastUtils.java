package hl.iss.whu.edu.laboratoryproject.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;

/**
 * Created by fate on 2017/2/7.
 */

public class ToastUtils {
    public static void showVolume(Context context,int volume){
        View view = LayoutInflater.from(context).inflate(R.layout.toast_volume, null);
        ProgressBar progress = ButterKnife.findById(view,R.id.pg_sound);
        progress.setProgress(volume);
        Toast toast = new Toast(context);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        toast.setGravity(Gravity.TOP,0,height/3);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }
    public static void showBrightness(Context context,int brightness){
        View view = LayoutInflater.from(context).inflate(R.layout.toast_brightness, null);
        ProgressBar progress = ButterKnife.findById(view,R.id.pg_sound);
        progress.setProgress(brightness);
        Toast toast = new Toast(context);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        toast.setGravity(Gravity.TOP,0,height/3);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }
}
