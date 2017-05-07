package hl.iss.whu.edu.laboratoryproject.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fate on 2017/2/18.
 */

public class TimeUtils {
    private static SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static String format(Date origin){

        return mFormat.format(origin);
    }
    public static String getLearningTime(long duration){
        if (duration<1000*60*60)
            return duration/(1000*60)+"分钟";
        else if (duration<1000*60*60*10)
            return duration/(1000*60*60)+"小时"+(duration%(1000*60*60))/(1000*60)+"分钟";
        else
            return duration/(1000*60*60)+"小时";
    }
}
