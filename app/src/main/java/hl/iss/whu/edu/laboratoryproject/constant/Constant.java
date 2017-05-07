package hl.iss.whu.edu.laboratoryproject.constant;

/**
 * Created by fate on 2016/11/16.
 */

public interface Constant {
    String INTENT_COURSE_LEARNING="hl.iss.whu.edu.laboratoryproject.intent.RECEIVER_COURSE_LEARNING";

    int DATAS_ONCE = 10;

    int REQURST_SETTING = 100;
    int REQURST_SEARCH = 101;
    int REQURST_ANSWER = 102;
    int REQURST_ASK = 103;
    int REQURST_RANK = 104;
    int REQURST_EXERCISE = 105;

    String INTENT_TYPE_DAILY = "daily";
    String INTENT_TYPE_NORMAL = "normal";
    String INTENT_TYPE_RECOMMENDED = "recommanded";

    String SERVER_URL = "http://60.205.190.45:8080/education/";


    String PREFERENCE_USERINFO = "UserInfo";
    //判断是否需要手动登录
    String KEY_NEED_LOGIN = "needlogin";
    String KEY_EXERCISE_NUMBER = "exercise_number";

    long CACHE_DURATION = 30000;
    String URI_CONVERSATION_PRIVATE = "rong://hl.iss.whu.edu.laboratoryproject/conversation/private?targetId=";
    String URI_CONVERSATION_GROUP = "rong://hl.iss.whu.edu.laboratoryproject/conversation/group?targetId=";
    int IMAGE_RADIUS = 5;
}
