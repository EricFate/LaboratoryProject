package hl.iss.whu.edu.laboratoryproject.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.adapter.RecyclerLessonSelectAdapter;
import hl.iss.whu.edu.laboratoryproject.adapter.RecyclerVideosAdapter;
import hl.iss.whu.edu.laboratoryproject.other.BannerImageLoader;
import hl.iss.whu.edu.laboratoryproject.entity.Major;
import hl.iss.whu.edu.laboratoryproject.entity.Course;
import hl.iss.whu.edu.laboratoryproject.entity.VideoInfo;
import hl.iss.whu.edu.laboratoryproject.listener.OnRecyclerViewItemClickListener;
import hl.iss.whu.edu.laboratoryproject.manager.SyLinearLayoutManager;
import hl.iss.whu.edu.laboratoryproject.ui.activity.FullscreenVideoActivity;
import hl.iss.whu.edu.laboratoryproject.ui.activity.LessonDetailActivity;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UserInfo;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by fate on 2016/10/29.
 */

public class LessonSelectionFragment extends BaseFragment<ArrayList<Major>> {
    private ArrayList<Integer> images = new ArrayList<>();
    private Context mContext;
    private ArrayList<String> grades;
    private ArrayList<String> subjects;
    private ArrayList<Major> filtered;
    private RecyclerLessonSelectAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerVideosAdapter mVideosAdapter;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mSpGrade.setSelection(grades.indexOf(UserInfo.grade));
        }
    };
    private Spinner mSpGrade;
    private Spinner mSpSubject;

    @Override
    public View onCreateSuccessPage() {
        filtered = new ArrayList<>();
        filtered.add(new Major("必修", new ArrayList<Course>()));
        filtered.add(new Major("辅修", new ArrayList<Course>()));
        filtered.add(new Major("辅导课程", new ArrayList<Course>()));
        mContext = getContext();
        View view = UiUtils.inflate(R.layout.fragment_lesson_selection);
        images.removeAll(images);
        images.add(R.drawable.timg);
        images.add(R.drawable.timg1);
        images.add(R.drawable.timg2);
        images.add(R.drawable.timg3);

        //初始化Banner
        Banner banner = ButterKnife.findById(view, R.id.banner_recommend);
        WindowManager windowManager = (WindowManager)mContext.getSystemService(mContext.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        final ViewGroup.LayoutParams params = banner.getLayoutParams();
        params.width = metrics.widthPixels;
        params.height = (int) (params.width * 0.5625);
        banner.setLayoutParams(params);
        banner.setImageLoader(new BannerImageLoader()).setImages(images)
                .setIndicatorGravity(BannerConfig.CENTER)
                .start();
        //初始化RecyclerView
        mRecyclerView = ButterKnife.findById(view, R.id.recycler_lesson_selection);
        mRecyclerView.setLayoutManager(new SyLinearLayoutManager(UiUtils.getContext()));
        mAdapter = new RecyclerLessonSelectAdapter(filtered);
        mAdapter.setOnSubjectClickListener(new OnRecyclerViewItemClickListener<Course>() {
            @Override
            public void onItemClick(View v, Course data) {
//                Toast.makeText(UiUtils.getContext(), data.getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), LessonDetailActivity.class);
                intent.putExtra("cid", data.getId());
                intent.putExtra("name",data.getName());
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        //初始化筛选匡
        mSpGrade = ButterKnife.findById(view, R.id.spinner_grade);
        mSpSubject = ButterKnife.findById(view, R.id.spinner_subject);
        grades = new ArrayList<>(Arrays.asList(mContext.getResources().getStringArray(R.array.grades)) ) ;
        grades.add(0,"全部");
        subjects = new ArrayList<>(Arrays.asList(mContext.getResources().getStringArray(R.array.subjects))) ;
        subjects.add(0,"全部");
        mSpGrade.setAdapter(new ArrayAdapter<>(mContext,R.layout.item_spinner, grades));
        mSpSubject.setAdapter(new ArrayAdapter<>(mContext,R.layout.item_spinner, subjects));
        mSpGrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter(grades.get(position), mSpSubject.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filter(mSpGrade.getSelectedItem().toString(), subjects.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
//        Object itemGrade = spGrade.getSelectedItem();
//        Object itemSubject = spSubject.getSelectedItem();
//        filter(itemGrade ==null?"全部":itemGrade.toString(), itemSubject==null?"全部":itemSubject.toString());
        RecyclerView recyclerVideo = (RecyclerView) view.findViewById(R.id.recycler_videos);
        recyclerVideo.setLayoutManager(new SyLinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        mVideosAdapter = new RecyclerVideosAdapter(new ArrayList<VideoInfo>());
        recyclerVideo.setAdapter(mVideosAdapter);
        mVideosAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<VideoInfo>() {
            @Override
            public void onItemClick(View v, VideoInfo data) {
                Intent intent = new Intent(getActivity(), FullscreenVideoActivity.class);
                intent.putExtra("url",data.getVideoUrl());
                startActivity(intent);
            }
        });
        handler.sendEmptyMessageDelayed(0,100);
        loadVideos();
        return view;
    }

    private void loadVideos() {
        RetrofitUtils.getService().getVideo(UserInfo.id).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<VideoInfo>>() {
                    @Override
                    public void accept(ArrayList<VideoInfo> infos) throws Exception {
                        mVideosAdapter.setData(infos);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(mContext, "获取视频失败"+throwable, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void filter(final String grade, final String subject) {

        for (int i = 0; i < data.size(); i++) {
            filtered.get(i).getCourses().clear();
            final int finalI = i;
            Flowable.fromIterable(data.get(i).getCourses()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .filter(new Predicate<Course>() {
                        @Override
                        public boolean test(Course course) throws Exception {
                            return (grade.equals("全部")?true:course.getGrade().equals(grade))&&(subject.equals("全部")?true:course.getSubject().equals(subject)) ;
                        }
                    }).subscribe(new Subscriber<Course>() {

                @Override
                public void onSubscribe(Subscription s) {
                    s.request(Long.MAX_VALUE);
                }

                @Override
                public void onNext(Course course) {
                    filtered.get(finalI).getCourses().add(course);
                }

                @Override
                public void onError(Throwable t) {
                }
                @Override
                public void onComplete() {
                    mAdapter.setData(filtered);
                }
            });
        }
    }

    @Override
    public Observable<ArrayList<Major>> sendRequest() {
        return RetrofitUtils.getService().loadAllLessons();
    }
}
