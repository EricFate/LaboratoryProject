package hl.iss.whu.edu.laboratoryproject.service;

import java.util.ArrayList;


import hl.iss.whu.edu.laboratoryproject.entity.GroupInfo;
import hl.iss.whu.edu.laboratoryproject.entity.Info;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by fate on 2017/2/9.
 */

public interface ISyncService {

    @GET("GetUserInfoServlet")
    Call<Info> getUserInfo(@Query("quid")String queryUid,@Query("muid")String myUid);

    @GET("GetGroupInfoServlet")
    Call<GroupInfo> getGroupInfo(@Query("gid")String gid);
}
