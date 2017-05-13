package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.adapter.RecyclerNoticeAdapter;
import hl.iss.whu.edu.laboratoryproject.entity.Notice;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NoticeActivity extends BaseInternetWithEmptyActivity {

    @Bind(R.id.recycler_general)
    RecyclerView mRecyclerNotice;
    private RecyclerNoticeAdapter mAdapter;
    private int mGid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        ButterKnife.bind(this);
        mGid = getIntent().getIntExtra("gid", 0);
        initView();
        initData();
    }

    private void initData() {
        showProgress();
        RetrofitUtils.getService().getNotice(mGid)
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Notice>>() {
                    @Override
                    public void accept(List<Notice> notices) throws Exception {
                        if (notices.size()==0){
                            showEmpty("暂无公告");
                        }else {
                            mAdapter.setData(notices);
                            showSuccess();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (throwable instanceof NullPointerException){
                            showEmpty("暂无公告");
                        }else {
                            showError();
                        }
                    }
                });
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAdapter = new RecyclerNoticeAdapter(new ArrayList<Notice>());
        mRecyclerNotice.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerNotice.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void retry() {
        initData();
    }
}
