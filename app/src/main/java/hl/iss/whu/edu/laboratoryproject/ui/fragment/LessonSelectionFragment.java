package hl.iss.whu.edu.laboratoryproject.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;

import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.adapter.RecyclerLessonSelectAdapter;
import hl.iss.whu.edu.laboratoryproject.banner.BannerImageLoader;
import hl.iss.whu.edu.laboratoryproject.entity.Catagory;
import hl.iss.whu.edu.laboratoryproject.entity.Result;
import hl.iss.whu.edu.laboratoryproject.entity.Subject;
import hl.iss.whu.edu.laboratoryproject.listener.OnRecyclerViewItemClickListener;
import hl.iss.whu.edu.laboratoryproject.manager.FullyLinearLayoutManager;
import hl.iss.whu.edu.laboratoryproject.ui.activity.LessonDetailActivity;
import hl.iss.whu.edu.laboratoryproject.ui.view.LoadingPage;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;
import io.reactivex.Observable;

/**
 * Created by fate on 2016/10/29.
 */

public class LessonSelectionFragment extends BaseFragment<ArrayList<Catagory>> {
    private ArrayList<Integer> images = new ArrayList<>();
    @Override
    public View onCreateSuccessPage() {
        View view = UiUtils.inflate(R.layout.fragment_lesson_selection);
        images.removeAll(images);
        images.add(R.drawable.picture1);
        images.add(R.drawable.picture2);
        images.add(R.drawable.bg);
        images.add(R.drawable.buildings);

        //初始化Banner
        Banner banner = ButterKnife.findById(view,R.id.banner_recommend);
        banner.setImageLoader(new BannerImageLoader()).setImages(images)
                .setIndicatorGravity(BannerConfig.CENTER)
                .start();
        //初始化RecyclerView
        RecyclerView recyclerView = ButterKnife.findById(view, R.id.recycler_lesson_selection);
        recyclerView.setLayoutManager(new FullyLinearLayoutManager(UiUtils.getContext()));
        RecyclerLessonSelectAdapter adapter = new RecyclerLessonSelectAdapter(data);
        adapter.setOnSubjectClickListener(new OnRecyclerViewItemClickListener<Subject>() {
            @Override
            public void onItemClick(View v, Subject data) {
                Toast.makeText(UiUtils.getContext(),data.getName(),Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), LessonDetailActivity.class));
            }
        });
        recyclerView.setAdapter(adapter);
//        RecyclerView recyclerView1 = ButterKnife.findById(view,R.id.recycler_math);
//        recyclerView1.setLayoutManager(new LinearLayoutManager(UiUtils.getContext(),LinearLayoutManager.HORIZONTAL,false));
//        RecyclerLessonSelectAdapter adapter1 = new RecyclerLessonSelectAdapter(data.get(1).getSubjects());
//        adapter1.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<Subject>() {
//            @Override
//            public void onItemClick(View v, Subject data) {
//                Toast.makeText(UiUtils.getContext(),data.getName(),Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(getActivity(), LessonDetailActivity.class));
//            }
//        });
//        recyclerView1.setAdapter(adapter1);
//        recyclerView1.setOverScrollMode(View.OVER_SCROLL_NEVER);
        return view;
    }

    @Override
    public Observable<ArrayList<Catagory>> sendRequest() {
        return RetrofitUtils.getService().loadAllLessons();
    }


}
