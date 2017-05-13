package hl.iss.whu.edu.laboratoryproject.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import hl.iss.whu.edu.laboratoryproject.entity.AdminClass;
import hl.iss.whu.edu.laboratoryproject.entity.Answer;
import hl.iss.whu.edu.laboratoryproject.entity.ChatGroup;
import hl.iss.whu.edu.laboratoryproject.entity.Course;
import hl.iss.whu.edu.laboratoryproject.entity.CourseLearning;
import hl.iss.whu.edu.laboratoryproject.entity.DayStudyInfo;
import hl.iss.whu.edu.laboratoryproject.entity.Exercise;
import hl.iss.whu.edu.laboratoryproject.entity.ExerciseCategory;
import hl.iss.whu.edu.laboratoryproject.entity.Info;
import hl.iss.whu.edu.laboratoryproject.entity.Major;
import hl.iss.whu.edu.laboratoryproject.entity.Chapter;
import hl.iss.whu.edu.laboratoryproject.entity.FriendRequest;
import hl.iss.whu.edu.laboratoryproject.entity.InfoDetail;
import hl.iss.whu.edu.laboratoryproject.entity.Issue;
import hl.iss.whu.edu.laboratoryproject.entity.Lesson;
import hl.iss.whu.edu.laboratoryproject.entity.MessageRecord;
import hl.iss.whu.edu.laboratoryproject.entity.Notice;
import hl.iss.whu.edu.laboratoryproject.entity.QueryItem;
import hl.iss.whu.edu.laboratoryproject.entity.Question;
import hl.iss.whu.edu.laboratoryproject.entity.Rank;
import hl.iss.whu.edu.laboratoryproject.entity.Result;
import hl.iss.whu.edu.laboratoryproject.entity.RosterGroup;
import hl.iss.whu.edu.laboratoryproject.entity.TotalCourseLearning;
import hl.iss.whu.edu.laboratoryproject.entity.TotalMessageRecord;
import hl.iss.whu.edu.laboratoryproject.entity.VideoInfo;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
/**
 * Created by fate on 2016/11/17.
 */

public interface IService {
    @POST("AddFriendServlet")
    @FormUrlEncoded
    Observable<Result> addFriend(@Field("rid")int rid ,@Field("group")String group,@Field("remark")String remark);

    @POST("UploadCourseLearningServlet")
    @FormUrlEncoded
    Observable<Result> uploadCourseLearning(@Field("sid")int sid ,@Field("cid")int cid,@Field("duration")long duration);

    @POST("UploadMessageRecordServlet")
    @FormUrlEncoded
    Observable<Result> uploadMessageRecord(@Field("content")String content);

    @POST("RequestFriendServlet")
    @FormUrlEncoded
    Observable<Result> requestFriend(@Field("quid")String quid ,@Field("muid")String muid ,@Field("group")String group,@Field("remark")String remark,@Field("message")String message);

    @POST("SignupServlet")
    @FormUrlEncoded
    Observable<Result> signup(@FieldMap Map<String,String> userMap);

    @POST("LoginServlet")
    @FormUrlEncoded
    Observable<Result> login(@Field("username") String username,@Field("password") String password);

    @POST("InfoChangeServlet")
    @FormUrlEncoded
    Observable<Result> changeInfo(@FieldMap Map<String,String> map);

    @POST("AskQuestionServlet")
    @FormUrlEncoded
    Observable<Result> submitQuestion(@Field("title") String title,@Field("content") String content,@Field("anonymous") boolean anonymous,@Field("id")int id);

    @POST("SubmitRankServlet")
    @FormUrlEncoded
    Observable<Result> submitRank(@Field("content") String content,@Field("id")int id,@Field("rank")float rank,@Field("cid")int cid);

    @POST("WriteAnswerServlet")
    @FormUrlEncoded
    Observable<Result> submitAnswer(@Field("content") String content,@Field("anonymous") boolean anonymous,@Field("iid") int iid,@Field("uid")String uid);

    @POST("ImageUploadServlet")
    @Multipart
    Observable<Result> uploadImage(@Part("fileName") String uid , @Part("file\";filename=\"1.jpg") RequestBody file);

    @POST("servletc/GetVideoServlet")
    @FormUrlEncoded
    Observable<ArrayList<VideoInfo>> getVideo(@Field("userId") int userId);

    @GET("LoadSubjectDetailServlet")
    Observable<Course> loadSubjectDetail(@Query("cid")int cid);

    @GET("LoadLessonDetailServlet")
    Observable<Lesson> loadLessonDetail(@Query("lid")int lid);

    @GET("LoadRanksServlet")
    Observable<ArrayList<Rank>> loadRanks(@Query("cid")int cid);

    @GET("LoadAllLessonServlet")
    Observable<ArrayList<Major>> loadAllLessons();

    @GET("LoadMyQuestionsServlet")
    Observable<ArrayList<Issue>> loadMyQuestions(@Query("id")int id);

    @GET("LoadMyAnswersServlet")
    Observable<ArrayList<Answer>> loadMyAnswer(@Query("uid")String uid);

    @GET("LoadMyRanksServlet")
    Observable<ArrayList<Rank>> loadMyRanks(@Query("id")int id);

    @GET("LoadMyLessonServlet")
    Observable<ArrayList<CourseLearning>> loadMyLessons(@Query("id")int id, @Query("start")int start);

    @GET("GetMyClassServlet")
    Observable<AdminClass> loadMyClass(@Query("id")int id);

    @GET("LoadDiscoverServlet")
    Observable<ArrayList<Issue>> loadDiscover(@Query("start") int start);

    @GET("LoadAnswersServlet")
    Observable<ArrayList<Answer>> loadAnswers(@Query("iid") int iid);

    @GET("AgreeAnswerServlet")
    Observable<Result> agreeAnswer(@Query("aid") int aid);

    @GET("LoadChapterServlet")
    Observable<ArrayList<Chapter>> loadChapter(@Query("cid")int cid);

    @GET("LoadQuestionsServlet")
    Observable<ArrayList<Question>> loadQuestion(@Query("lid") int lid);

    @GET("RequestTokenServlet")
    Observable<Result> requestToken(@Query("uid") String uid);

    @GET("GetContactsServlet")
    Observable<ArrayList<RosterGroup>> getContacts(@Query("uid") String uid);

    @GET("GetDetailedInfoServlet")
    Observable<InfoDetail> getDetailedInfo(@Query("uid") String uid);

    @GET("GetFriendRequestServlet")
    Observable<ArrayList<FriendRequest>> getFriendRequest(@Query("uid") String uid);

    @GET("GetRequestCountServlet")
    Observable<Result> getRequestCount(@Query("uid") String uid);

    @GET("GetGroupNamesServlet")
    Observable<ArrayList<String>> getGroupNames(@Query("uid") String uid);

    @GET("GetGroupsServlet")
    Observable<ArrayList<ChatGroup>> getGroups(@Query("uid") String uid);

    @GET("QueryContactsServlet")
    Observable<ArrayList<QueryItem>> queryContacts(@Query("uid")String uid, @Query("query") String query);

    @GET("JoinGroupServlet")
    Observable<Result> joinGroup(@Query("uid")String uid, @Query("gid") String gid);

    @GET("LearnCourseServlet")
    Observable<Result> startLearn(@Query("id")int id, @Query("cid") int cid);

    @GET("CheckIsLearningServlet")
    Observable<Result> checkIsLearning(@Query("id")int id, @Query("cid") int cid);

    @GET("DeleteAnswerServlet")
    Observable<Result> deleteAnswer(@Query("aid")int aid);

    @GET("DeleteIssueServlet")
    Observable<Result> deleteIssue(@Query("iid")int iid);

    @GET("DeleteRankServlet")
    Observable<Result> deleteRank(@Query("rid")int rid);

    @GET("GetClassMemberByClassServlet")
    Observable<ArrayList<Info>> getClassMemberByClass(@Query("clid")int clid,@Query("type")int type);

    @GET("GetExerciseCategoryServlet")
    Observable<ArrayList<ExerciseCategory>> getExerciseCategory(@Query("id")int id,@Query("cid")int cid);

    @GET("GetExerciseServlet")
    Observable<ArrayList<Exercise>> getExercise(@Query("id")int id,@Query("start") int start,@Query("type") String type,@Query("num")int num);

    @GET("LoadQuestionsServlet")
    Observable<ArrayList<Exercise>> getLessonExercise(@Query("lid")int lid);

    @POST("UploadExerciseResultServlet")
    @FormUrlEncoded
    Observable<Result> uploadExerciseResult(@Field("id")int id,@Field("content")String content,@Field("ecid")String ecid);

    @POST("JoinClassServlet")
    @FormUrlEncoded
    Observable<Result> joinClass(@Field("id")int id,@Field("clzId")int clzId);


    @GET("GetAdminClassServlet")
    Observable<AdminClass> getAdminClass(@Query("acid")String acid);



    //获得课程学习情况
    @GET("servlet/GetCourseLearning")
    Observable<ArrayList<CourseLearning>> getCourseLearning(@Query("id") int id);

    //获得课程学习总情况
    @GET("servlet/GetTotalCourseLearning")
    Observable<ArrayList<TotalCourseLearning>> getTotalCourseLearning(@Query("id") int id);

    //获得消息发送情况
    @GET("servlet/GetMessageRecord")
    Observable<ArrayList<MessageRecord>> getMessageRecord(@Query("uid") String uid);

    //获得消息发送总情况
    @GET("servlet/GetTotalMessageRecord")
    Observable<ArrayList<TotalMessageRecord>> getTotalMessageRecord(@Query("uid") String uid);

    //获得学习总情况
    @GET("servlet/GetStudyInfo")
    Observable<ArrayList<DayStudyInfo>> getStudyInfo(@Query("id") int id);

    //获得单个问题
    @GET("servlet/GetSingleIssue")
    Observable<Issue> getSingleIssue(@Query("issueId") int id);

    //获得单个回答
    @GET("servlet/GetSingleAnswer")
    Observable<Answer> getSingleAnswer(@Query("answerId") int id);

    //获得单个回答
    @GET("GetNoticeServlet")
    Observable<List<Notice>> getNotice(@Query("gid") int gid);

}
