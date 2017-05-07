package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.adapter.RecyclerAnswerAdapter;
import hl.iss.whu.edu.laboratoryproject.constant.Constant;
import hl.iss.whu.edu.laboratoryproject.entity.Answer;
import hl.iss.whu.edu.laboratoryproject.entity.Issue;
import hl.iss.whu.edu.laboratoryproject.entity.Result;
import hl.iss.whu.edu.laboratoryproject.entity.Student;
import hl.iss.whu.edu.laboratoryproject.glide.GlideCircleTransform;
import hl.iss.whu.edu.laboratoryproject.listener.OnUserInfoClickListener;
import hl.iss.whu.edu.laboratoryproject.manager.FullyLinearLayoutManager;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class QuestionActivity extends BaseInternetRequestActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.etv_question_content)
    ExpandableTextView tvDetail;
    @Bind(R.id.tv_discover_number)
    TextView tvDiscoverNumber;
    @Bind(R.id.recycler_answer)
    RecyclerView recyclerAnswer;
    @Bind(R.id.iv_image)
    ImageView ivImage;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.ll_asker)
    LinearLayout llAsker;
    private RecyclerAnswerAdapter adapter;
    private int mIid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initview();

    }

    private void initview() {
        adapter = new RecyclerAnswerAdapter(new ArrayList<Answer>());
        recyclerAnswer.setLayoutManager(new FullyLinearLayoutManager(QuestionActivity.this));
        recyclerAnswer.setAdapter(adapter);
        adapter.setOnAgreeClickListener(new RecyclerAnswerAdapter.OnAgreeClickListener() {
            @Override
            public void onAgreeClick(int aid) {
                agreeAnswer(aid);
            }
        });
        adapter.setOnUserInfoClickListener(new OnUserInfoClickListener() {
            @Override
            public void onUserInfoClickListener(String uid) {
                Intent intent = new Intent(QuestionActivity.this, PersonalInfoActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });
        Issue issue = (Issue) getIntent().getSerializableExtra("issue");
        mIid = issue.getId();
        tvTitle.setText(issue.getTitle());
        tvDetail.setText(issue.getContent());
        tvDiscoverNumber.setText(issue.getAnswerNumber() + "人回答");
        if (issue.isAnonymous()) {
            tvName.setText("匿名提问");
            Glide.with(UiUtils.getContext())
                    .load(R.drawable.ic_account_circle_blue_600_24dp)
                    .transform(new GlideCircleTransform(UiUtils.getContext()))
                    .into(ivImage);
        } else {
            final Student student = issue.getUser();
            tvName.setText(student.getNickname());
            Glide.with(UiUtils.getContext()).load(Constant.SERVER_URL + student.getImageURL()).placeholder(R.drawable.ic_account_circle_blue_600_24dp)
                    .transform(new GlideCircleTransform(UiUtils.getContext()))
                    .into(ivImage);
            llAsker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(QuestionActivity.this, PersonalInfoActivity.class);
                    intent.putExtra("uid","s"+ student.getId());
                    startActivity(intent);
                }
            });
        }
        loadAnswers(mIid);
    }

    private void loadAnswers(final int iid) {
        showProgress();
        RetrofitUtils.getService().loadAnswers(iid).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<Answer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e("onSubscribe", "onSubscribe: ");
                    }

                    @Override
                    public void onNext(ArrayList<Answer> value) {
                        Log.e("onnext", "onNext:" + value.size());
                        adapter.setData(value);
                        showSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showEmpty();
                        new AlertDialog.Builder(QuestionActivity.this).setMessage("错误:" + e).setNegativeButton("取消", null)
                                .setPositiveButton("重试", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        loadAnswers(iid);
                                        dialog.dismiss();
                                    }
                                });
                    }

                    @Override
                    public void onComplete() {
                        Log.e("onComplete", "onComplete: ");
                    }
                });
    }

    private void agreeAnswer(int aid) {
        RetrofitUtils.getService().agreeAnswer(aid).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result value) {
                        if (value.getCode() == 0) {
                            Toast.makeText(QuestionActivity.this, "赞同成功", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        new AlertDialog.Builder(QuestionActivity.this).setMessage("错误:" + e).setPositiveButton("确定", null).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @OnClick({R.id.ib_answer, R.id.ib_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_answer:
                Intent intent = new Intent(this, WriteAnswerActivity.class);
                intent.putExtra("iid", mIid);
                startActivityForResult(intent, Constant.REQURST_ANSWER);
                break;
            case R.id.ib_share:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.REQURST_ANSWER && resultCode == RESULT_OK)
            refresh();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void refresh() {
        loadAnswers(mIid);
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
        loadAnswers(mIid);
    }
}
