package hl.iss.whu.edu.laboratoryproject.entity;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by fate on 2016/11/17.
 */

public interface IService {
    @POST("SignupServlet")
    @FormUrlEncoded
    Observable<Result> signup(@FieldMap Map<String,String> userMap);


    @POST("LoginServlet")
    @FormUrlEncoded
    Observable<Result> login(@Field("username") String username,@Field("password") String password);

    @POST("ImageUploadServlet")
    @Multipart
    Observable<Result> uploadImage(@Part("fileName") String description ,@Part("file\";filename=\"1.txt") RequestBody file);

    @GET("LoadAllLessonServlet")
    Observable<ArrayList<Subject>> loadAllLessons();
    @GET("LoadMyLessonServlet")
    Observable<ArrayList<Subject>> loadMyLessons();
    @GET("LoadDiscoverServlet")
    Observable<ArrayList<Discover>> loadDiscover();
    @GET("LoadChapter")
    Observable<ArrayList<Chapter>> loadChapter();
}
