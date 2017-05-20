package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.adapter.RecyclerMyRanksAdapter;
import hl.iss.whu.edu.laboratoryproject.entity.Answer;
import hl.iss.whu.edu.laboratoryproject.entity.Rank;
import hl.iss.whu.edu.laboratoryproject.entity.Result;
import hl.iss.whu.edu.laboratoryproject.listener.OnDeleteClickListener;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UserInfo;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MyRanksActivity extends BaseInternetWithEmptyActivity {

    @Bind(R.id.recycler_general)
    RecyclerView recyclerGeneral;
    RecyclerMyRanksAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ranks);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        initData();
    }

    private void initView() {
        mAdapter = new RecyclerMyRanksAdapter(new ArrayList<Answer>());
        recyclerGeneral.setLayoutManager(new LinearLayoutManager(this));
        recyclerGeneral.setAdapter(mAdapter);

        mAdapter.setOnDeleteClickListener(new OnDeleteClickListener() {
            @Override
            public void onDeleteClick(final int id, final int position) {
                new AlertDialog.Builder(MyRanksActivity.this).setMessage("确认删除?").setNegativeButton("取消", null)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteRank(id,position);
                                dialog.dismiss();
                            }
                        }).show();
            }
        });

    }

    private void deleteRank(final int id, final int position) {
        RetrofitUtils.getService().deleteRank(id).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Result>() {
                    @Override
                    public void accept(Result result) throws Exception {
                        Toast.makeText(MyRanksActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        mAdapter.remove(position);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(MyRanksActivity.this, "删除失败" + throwable, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initData() {
        showProgress();
        RetrofitUtils.getService().loadMyRanks(UserInfo.id).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<Rank>>() {
                    @Override
                    public void accept(ArrayList<Rank> ranks) throws Exception {
                        if (ranks.size()==0){
                            showEmpty("你还未评价过");
                            return;
                        }
                        mAdapter.setData(ranks);
                        showSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(MyRanksActivity.this, "错误" + throwable, Toast.LENGTH_SHORT).show();
                        showError();
                    }
                });
    }

    @Override
    protected void retry() {
        initData();
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
}
