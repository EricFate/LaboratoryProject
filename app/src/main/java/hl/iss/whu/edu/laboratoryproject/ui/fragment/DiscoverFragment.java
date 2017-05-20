package hl.iss.whu.edu.laboratoryproject.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.adapter.RecyclerDiscoverAdapter;
import hl.iss.whu.edu.laboratoryproject.constant.Constant;
import hl.iss.whu.edu.laboratoryproject.entity.Issue;
import hl.iss.whu.edu.laboratoryproject.listener.OnRecyclerViewItemClickListener;
import hl.iss.whu.edu.laboratoryproject.listener.OnUserInfoClickListener;
import hl.iss.whu.edu.laboratoryproject.ui.activity.AskQuestionActivity;
import hl.iss.whu.edu.laboratoryproject.ui.activity.PersonalInfoActivity;
import hl.iss.whu.edu.laboratoryproject.ui.activity.QuestionActivity;
import hl.iss.whu.edu.laboratoryproject.ui.activity.WriteAnswerActivity;
import hl.iss.whu.edu.laboratoryproject.ui.view.LoadingPage;
import hl.iss.whu.edu.laboratoryproject.utils.MyDialog;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by fate on 2016/10/29.
 */

public class DiscoverFragment extends BaseFragment<ArrayList<Issue>> {
    private int start = 0;
    private RecyclerDiscoverAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateSuccessPage() {
        start += data.size();
        View rootView = UiUtils.inflate(R.layout.fragment_discover);

        final TwinklingRefreshLayout pullToRefreshView = ButterKnife.findById(rootView, R.id.trl_discover);
        pullToRefreshView.setHeaderView(new SinaRefreshView(UiUtils.getContext()));
        if (start < Constant.DATAS_ONCE)
            pullToRefreshView.setEnableLoadmore(false);
        pullToRefreshView.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                loadMore(refreshLayout);
            }

            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                refresh(refreshLayout);
            }
        });

        mRecyclerView = ButterKnife.findById(rootView, R.id.recycler_discover);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new RecyclerDiscoverAdapter(data);
        mAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<Issue>() {
            @Override
            public void onItemClick(View v, Issue data) {
                Intent intent = new Intent(getActivity(), QuestionActivity.class);
                intent.putExtra("issue", data);
                startActivity(intent);
            }
        });
        mAdapter.setOnAnswerClickListener(new RecyclerDiscoverAdapter.OnAnswerClickListener() {
            @Override
            public void onAnswerClick(int iid) {
                Intent intent = new Intent(getActivity(), WriteAnswerActivity.class);
                intent.putExtra("iid", iid);
                startActivity(intent);
            }
        });
        mAdapter.setOnUserInfoClickListener(new OnUserInfoClickListener() {
            @Override
            public void onUserInfoClickListener(String uid) {
                Intent intent = new Intent(getActivity(), PersonalInfoActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);


        ImageButton fab = ButterKnife.findById(rootView, R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AskQuestionActivity.class);
                startActivityForResult(intent, Constant.REQURST_ASK);
            }
        });
        if (data.size() == 0) {
            showEmpty(rootView);
        }
        return rootView;
    }

    private void showEmpty(View root) {
        root.findViewById(R.id.trl_discover).setVisibility(View.GONE);
        root.findViewById(R.id.ll_nodata).setVisibility(View.VISIBLE);
    }

    private void refresh(final TwinklingRefreshLayout layout) {
        RetrofitUtils.getService().loadDiscover(0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<ArrayList<Issue>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<Issue> value) {
                        if (value.size() < Constant.DATAS_ONCE) {
                            layout.setEnableLoadmore(false);
                        }
                        mAdapter.setData(value);
                        start = 0;
                        layout.finishRefreshing();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(DiscoverFragment.this.getActivity(), "刷新失败" + e, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void loadMore(final TwinklingRefreshLayout layout) {
        RetrofitUtils.getService().loadDiscover(start)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<ArrayList<Issue>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<Issue> value) {
                        if (value.size() < Constant.DATAS_ONCE) {
                            layout.setEnableLoadmore(false);
                            Toast.makeText(DiscoverFragment.this.getActivity(), "已无更多", Toast.LENGTH_SHORT).show();
                        }
                        start += value.size();
                        mAdapter.addAll(value);
                        layout.finishLoadmore();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(DiscoverFragment.this.getActivity(), "加载失败" + e, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public Observable<ArrayList<Issue>> sendRequest() {
        return RetrofitUtils.getService().loadDiscover(0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQURST_ASK && resultCode == Activity.RESULT_OK)
            refresh();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void refresh() {
        start = 0;
        RetrofitUtils.getService().loadDiscover(0).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<Issue>>() {
                    @Override
                    public void accept(ArrayList<Issue> issues) throws Exception {
                        mAdapter.setData(issues);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
//                        MyDialog.showAlertDialgo(getActivity(),"错误"+throwable);
                        Log.e(getClass().getSimpleName(), "accept: " + throwable);
                    }
                });
    }
}
