package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.adapter.RecyclerMyIssuesAdapter;
import hl.iss.whu.edu.laboratoryproject.entity.Issue;
import hl.iss.whu.edu.laboratoryproject.entity.Result;
import hl.iss.whu.edu.laboratoryproject.entity.Student;
import hl.iss.whu.edu.laboratoryproject.listener.OnDeleteClickListener;
import hl.iss.whu.edu.laboratoryproject.listener.OnRecyclerViewItemClickListener;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UserInfo;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MyIssuesActivity extends BaseInternetWithEmptyActivity {

    @Bind(R.id.recycler_general)
    RecyclerView recyclerAnswers;
    private RecyclerMyIssuesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_issues);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        initData();
    }


    private void initView() {
        mAdapter = new RecyclerMyIssuesAdapter(new ArrayList<Issue>());
        recyclerAnswers.setLayoutManager(new LinearLayoutManager(this));
        recyclerAnswers.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<Issue>() {
            @Override
            public void onItemClick(View v, Issue data) {
                data.setUser(new Student(UserInfo.id,UserInfo.nickname,UserInfo.imageURL));
                Intent intent = new Intent(MyIssuesActivity.this,QuestionActivity.class);
                intent.putExtra("issue",data);
                startActivity(intent);
                finish();
            }
        });
        mAdapter.setOnDeleteClickListener(new OnDeleteClickListener() {
            @Override
            public void onDeleteClick(final int id) {
                new AlertDialog.Builder(MyIssuesActivity.this).setMessage("确认删除?").setNegativeButton("取消",null)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteIssue(id);
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
    }

    private void deleteIssue(final int id) {
        RetrofitUtils.getService().deleteIssue(id).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Result>() {
                    @Override
                    public void accept(Result result) throws Exception {
                        Toast.makeText(MyIssuesActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        mAdapter.remove(id);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(MyIssuesActivity.this, "删除失败"+throwable, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initData() {
        showProgress();
        RetrofitUtils.getService().loadMyQuestions(UserInfo.id).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<Issue>>() {
                    @Override
                    public void accept(ArrayList<Issue> issues) throws Exception {
                        if (issues.size()==0){
                            showEmpty("你还未提问过");
                            return;
                        }
                        mAdapter.setData(issues);
                        showSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(MyIssuesActivity.this, "错误"+throwable, Toast.LENGTH_SHORT).show();
                        showError();
                    }
                });
    }

    @Override
    protected void retry() {
        initData();
    }
}
