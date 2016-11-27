package hl.iss.whu.edu.laboratoryproject.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.yalantis.pulltorefresh.library.PullToRefreshView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.adapter.RecyclerMyLessonsAdapter;
import hl.iss.whu.edu.laboratoryproject.entity.Chapter;
import hl.iss.whu.edu.laboratoryproject.entity.Subject;
import hl.iss.whu.edu.laboratoryproject.listener.OnRecyclerViewItemClickListener;
import hl.iss.whu.edu.laboratoryproject.ui.activity.LessonDetailActivity;
import hl.iss.whu.edu.laboratoryproject.ui.view.LoadingPage;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;
import io.reactivex.Observable;

/**
 * Created by fate on 2016/10/29.
 */

public class MyLessonsFragment extends BaseFragment<ArrayList<Subject>> {

    @Override
    public View onCreateSuccessPage() {
        View view = UiUtils.inflate(R.layout.fragment_my_lessons);
        //初始化PullToRefreshView
        final PullToRefreshView pullToRefreshView = ButterKnife.findById(view,R.id.ptrv_mylesson);
        pullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                UiUtils.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefreshView.setRefreshing(false);
                        Log.e("refreshing","false");
                    }
                },1000);
            }
        });
        //初始化RecyclerView
        RecyclerView recyclerView = ButterKnife.findById(view, R.id.recycler_my_lesson);
        recyclerView.setLayoutManager(new LinearLayoutManager(UiUtils.getContext()));
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add(i + "");
        }
        RecyclerMyLessonsAdapter adapter = new RecyclerMyLessonsAdapter(data);
        adapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<String>() {
            @Override
            public void onItemClick(View v, String data) {
                Intent intent = new Intent(getActivity(), LessonDetailActivity.class);
                Toast.makeText(UiUtils.getContext(), data, Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        return view;
    }

    @Override
    public Observable<ArrayList<Subject>> sendRequest() {
        return RetrofitUtils.getService().loadMyLessons();
    }

}
