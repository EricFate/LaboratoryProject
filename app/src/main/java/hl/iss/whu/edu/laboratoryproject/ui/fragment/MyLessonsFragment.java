package hl.iss.whu.edu.laboratoryproject.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.adapter.RecyclerMyLessonsAdapter;
import hl.iss.whu.edu.laboratoryproject.constant.Constant;
import hl.iss.whu.edu.laboratoryproject.entity.Course;
import hl.iss.whu.edu.laboratoryproject.entity.CourseLearning;
import hl.iss.whu.edu.laboratoryproject.listener.OnRecyclerViewItemClickListener;
import hl.iss.whu.edu.laboratoryproject.ui.activity.LessonDetailActivity;
import hl.iss.whu.edu.laboratoryproject.ui.view.LoadingPage;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UserInfo;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by fate on 2016/10/29.
 */

public class MyLessonsFragment extends BaseFragment<ArrayList<CourseLearning>> {
    private int start = 0;
    private RecyclerMyLessonsAdapter mAdapter;

    @Override
    public View onCreateSuccessPage() {
        if (data.size()==0){
            mLoadingPage.changeState(LoadingPage.STATE_NO_DATA);
            return null;
        }
        View view = UiUtils.inflate(R.layout.fragment_my_lessons);
        //初始化PullToRefreshView
        final TwinklingRefreshLayout pullToRefreshView = ButterKnife.findById(view,R.id.tkrv_mylesson);
        pullToRefreshView.setHeaderView(new SinaRefreshView(UiUtils.getContext()));
        pullToRefreshView.setEnableLoadmore(false);
        pullToRefreshView.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                refresh(refreshLayout);
            }

            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                loadMore(refreshLayout);
            }
        });
        //初始化RecyclerView
        RecyclerView recyclerView = ButterKnife.findById(view, R.id.recycler_my_lesson);
        recyclerView.setLayoutManager(new LinearLayoutManager(UiUtils.getContext()));

        mAdapter = new RecyclerMyLessonsAdapter(data);
        mAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<CourseLearning>() {
            @Override
            public void onItemClick(View v, CourseLearning data) {
                Intent intent = new Intent(getActivity(), LessonDetailActivity.class);
                Course course = data.getCourse();
                intent.putExtra("cid",course.getId()) ;
                intent.putExtra("name",course.getName());
                Toast.makeText(UiUtils.getContext(), course.getName(), Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(mAdapter);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        return view;
    }

    @Override
    public Observable<ArrayList<CourseLearning>> sendRequest() {
        return RetrofitUtils.getService().loadMyLessons(UserInfo.id,0);
    }
    private void refresh(final TwinklingRefreshLayout layout) {
        RetrofitUtils.getService().loadMyLessons(UserInfo.id,0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<ArrayList<CourseLearning>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<CourseLearning> value) {

                        if (value.size()< Constant.DATAS_ONCE){
                            layout.setEnableLoadmore(false);
                        }
                        mAdapter.setData(value);
                        start = value.size();
                        layout.finishRefreshing();
                    }
                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MyLessonsFragment.this.getActivity(), "刷新失败"+e, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void loadMore(final TwinklingRefreshLayout layout) {
        RetrofitUtils.getService().loadMyLessons(UserInfo.id,start)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<ArrayList<CourseLearning>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<CourseLearning> value) {
                        if (value.size()<Constant.DATAS_ONCE){
                            layout.setEnableLoadmore(false);
                            Toast.makeText(MyLessonsFragment.this.getActivity(), "已无更多", Toast.LENGTH_SHORT).show();
                        }
                        start+=value.size();
                        mAdapter.addAll(value);
                        layout.finishLoadmore();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MyLessonsFragment.this.getActivity(), "加载失败"+e, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

}
