package hl.iss.whu.edu.laboratoryproject.ui.fragment;

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
import hl.iss.whu.edu.laboratoryproject.entity.Result;
import hl.iss.whu.edu.laboratoryproject.entity.Subject;
import hl.iss.whu.edu.laboratoryproject.listener.OnRecyclerViewItemClickListener;
import hl.iss.whu.edu.laboratoryproject.ui.view.LoadingPage;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;
import io.reactivex.Observable;

/**
 * Created by fate on 2016/10/29.
 */

public class LessonSelectionFragment extends BaseFragment<ArrayList<Subject>> {
    private ArrayList<Integer> images = new ArrayList<>();
    @Override
    public View onCreateSuccessPage() {
        View view = UiUtils.inflate(R.layout.fragment_lesson_selection);
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
        RecyclerView recyclerView = ButterKnife.findById(view, R.id.recycler_cs);
        recyclerView.setLayoutManager(new LinearLayoutManager(UiUtils.getContext(),LinearLayoutManager.HORIZONTAL,false));
        ArrayList<Subject> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add(new Subject(i + "", i + "", i + "", i + ""));
        }
        RecyclerLessonSelectAdapter adapter = new RecyclerLessonSelectAdapter(data);
        adapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<Subject>() {
            @Override
            public void onItemClick(View v, Subject data) {
                Toast.makeText(UiUtils.getContext(),data.getName(),Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        return view;
    }

    @Override
    public Observable<ArrayList<Subject>> sendRequest() {
        return RetrofitUtils.getService().loadAllLessons();
    }


}
