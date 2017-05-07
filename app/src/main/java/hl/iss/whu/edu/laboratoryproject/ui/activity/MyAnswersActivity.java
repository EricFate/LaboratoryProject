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
import hl.iss.whu.edu.laboratoryproject.adapter.RecyclerMyAnswersAdapter;
import hl.iss.whu.edu.laboratoryproject.entity.Answer;
import hl.iss.whu.edu.laboratoryproject.entity.Issue;
import hl.iss.whu.edu.laboratoryproject.entity.Result;
import hl.iss.whu.edu.laboratoryproject.listener.OnDeleteClickListener;
import hl.iss.whu.edu.laboratoryproject.listener.OnRecyclerViewItemClickListener;
import hl.iss.whu.edu.laboratoryproject.listener.OnUserInfoClickListener;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UserInfo;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MyAnswersActivity extends BaseInternetWithEmptyActivity {

    @Bind(R.id.recycler_general)
    RecyclerView recyclerAnswers;
    private RecyclerMyAnswersAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_answers);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        initData();
    }


    private void initView() {
        mAdapter = new RecyclerMyAnswersAdapter(new ArrayList<Answer>());
        recyclerAnswers.setLayoutManager(new LinearLayoutManager(this));
        recyclerAnswers.setAdapter(mAdapter);
        mAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<Answer>() {
            @Override
            public void onItemClick(View v, Answer data) {
                Intent intent = new Intent(MyAnswersActivity.this,QuestionActivity.class);
                intent.putExtra("issue",data.getIssue());
                startActivity(intent);
            }
        });
        mAdapter.setOnDeleteClickListener(new OnDeleteClickListener() {
            @Override
            public void onDeleteClick(final int id) {
                new AlertDialog.Builder(MyAnswersActivity.this).setMessage("确认删除?").setNegativeButton("取消",null)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteAnswer(id);
                                dialog.dismiss();
                            }
                        }).show();

            }
        });
        mAdapter.setOnUserInfoClickListener(new OnUserInfoClickListener() {
            @Override
            public void onUserInfoClickListener(String uid) {
                Intent intent = new Intent(MyAnswersActivity.this,PersonalInfoActivity.class);
                intent.putExtra("uid",uid);
                startActivity(intent);
            }
        });
    }

    private void deleteAnswer(final int id) {
        RetrofitUtils.getService().deleteAnswer(id).subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Result>() {
                    @Override
                    public void accept(Result result) throws Exception {
                        Toast.makeText(MyAnswersActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        mAdapter.remove(id);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(MyAnswersActivity.this, "删除失败"+throwable, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initData() {
        showProgress();
        RetrofitUtils.getService().loadMyAnswer(UserInfo.uid).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<Answer>>() {
                    @Override
                    public void accept(ArrayList<Answer> answers) throws Exception {
                        if (answers.size()==0){
                            showEmpty("你还未提问过");
                            return;
                        }
                        mAdapter.setData(answers);
                        showSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(MyAnswersActivity.this, "错误"+throwable, Toast.LENGTH_SHORT).show();
                        showError();
                    }
                });
    }


    @Override
    protected void retry() {
        initData();
    }
}
