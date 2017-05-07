package hl.iss.whu.edu.laboratoryproject.utils;

import android.net.Uri;

import java.io.IOException;
import java.util.ArrayList;

import hl.iss.whu.edu.laboratoryproject.constant.Constant;
import hl.iss.whu.edu.laboratoryproject.entity.GroupInfo;
import hl.iss.whu.edu.laboratoryproject.entity.Info;
import io.rong.imkit.model.GroupUserInfo;
import io.rong.imlib.model.Group;
import retrofit2.Response;


/**
 * Created by fate on 2016/11/21.
 */

public class UserInfo {
    public static int id;
    public static String uid;
    public static String username;
    public static String imageURL;
    public static String nickname;
    public static String signiture;
    public static String token;

    public static ArrayList<String> groupNames = new ArrayList<>();
    public static String grade;

    public static Uri getPrivateChatUri(String uid,String title){
        return Uri.parse(Constant.URI_CONVERSATION_PRIVATE+uid+"&title="+title);
    }
    public static Uri getGroupChatUri(String gid,String title){
        return Uri.parse(Constant.URI_CONVERSATION_GROUP+gid+"&title="+title);
    }
    public static String getIdentity(String uid){
        String identity = "";
        switch (uid.charAt(0)) {
            case 's':
                identity = "学生";
                break;
            case 't':
                identity = "老师";
                break;
            case 'c':
                identity = "大学生";
                break;
            case 'a':
                identity = "家长";
                break;
            default:
                break;
        }
        return identity;
    }
    public static io.rong.imlib.model.UserInfo findUserByUid(String uid) {
        try {
            Response<Info> response = RetrofitUtils.getSyncService().getUserInfo(uid,UserInfo.uid).execute();
            Info info = response.body();
            io.rong.imlib.model.UserInfo userInfo=null;
            if (info!=null)
                userInfo = new io.rong.imlib.model.UserInfo(info.getUid(), info.getNickname(), Uri.parse(Constant.SERVER_URL + info.getImageURL()));
            return userInfo;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Group getGroupInfoById(String gid) {
        try {
            Response<GroupInfo> response = RetrofitUtils.getSyncService().getGroupInfo(gid).execute();
            GroupInfo info = response.body();
            Group group = null;
            if (info!=null)
                group = new Group("g"+info.getId(),info.getName(),Uri.parse(Constant.SERVER_URL+info.getImageURL()));
            return group;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static GroupUserInfo findGroupUserByUid(String gid,String uid) {
        try {
            Response<Info> response = RetrofitUtils.getSyncService().getUserInfo(uid,UserInfo.uid).execute();
            Info info = response.body();
            GroupUserInfo userInfo=null;
            if (info!=null)
                userInfo = new GroupUserInfo(gid,info.getUid(), info.getNickname());
            return userInfo;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
