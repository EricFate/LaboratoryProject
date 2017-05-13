package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.style.Wave;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.adapter.ExpandableChapterAdapter;
import hl.iss.whu.edu.laboratoryproject.constant.Constant;
import hl.iss.whu.edu.laboratoryproject.entity.Chapter;
import hl.iss.whu.edu.laboratoryproject.entity.Course;
import hl.iss.whu.edu.laboratoryproject.entity.ExerciseCategory;
import hl.iss.whu.edu.laboratoryproject.entity.Lesson;
import hl.iss.whu.edu.laboratoryproject.entity.Result;
import hl.iss.whu.edu.laboratoryproject.entity.Teacher;
import hl.iss.whu.edu.laboratoryproject.glide.GlideRoundTransform;
import hl.iss.whu.edu.laboratoryproject.ui.view.CustomExpandableListView;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UserInfo;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LessonDetailActivity extends BaseInternetRequestActivity {

    @Bind(R.id.tv_description)
    TextView tvDescription;
    @Bind(R.id.rating_rank)
    RatingBar ratingRank;
    @Bind(R.id.iv_teacher_image)
    ImageView ivTeacherImage;
    @Bind(R.id.tv_teacher_info)
    TextView tvTeacherInfo;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.bt_start_learning)
    Button btStartLearning;
    @Bind(R.id.ll_rank)
    LinearLayout llRank;
    @Bind(R.id.aelv_chapter)
    CustomExpandableListView aelvChapter;
    @Bind(R.id.apl_title)
    AppBarLayout mAplTitle;
    @Bind(R.id.bt_exercise)
    Button mBtExercise;
    @Bind(R.id.ll_button_group)
    LinearLayout mLlButtonGroup;
    private int cid;
    private int gid;
    private boolean isLearning = true;
    private boolean detailFinish;
    private boolean isLearningFinish;
    private ExpandableChapterAdapter mAdapter;
    private Wave mWave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_detail);
        mWave = new Wave();
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_lesson_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        llRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LessonDetailActivity.this, RanksActivity.class);
                intent.putExtra("cid", cid);
                startActivity(intent);
            }
        });
        mAdapter = new ExpandableChapterAdapter(new ArrayList<Chapter>());
        aelvChapter.setAdapter(mAdapter);
        Intent intent = getIntent();
        if (intent != null) {
            cid = intent.getIntExtra("cid", 0);
            String name = intent.getStringExtra("name");
            collapsingToolbar.setTitle(name);
            toolbar.setTitle(name);
        }
        initData();

    }

    private void initData() {
        showProgress();
        if (cid != 0) {
            RetrofitUtils.getService().loadSubjectDetail(cid).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Course>() {
                        @Override
                        public void accept(Course detail) throws Exception {
                            gid = detail.getChatGroup().getId();
                            setView(detail);
                            detailFinish = true;
                            if (isLearningFinish) {
                                showSuccess();
//                                collapsingToolbar.setVisibility(View.VISIBLE);
                                mLlButtonGroup.setVisibility(View.VISIBLE);
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
//                            new AlertDialog.Builder(LessonDetailActivity.this).setMessage("错误:" + throwable).setPositiveButton("确定", null)
//                                    .show();
                            showError();
//                            collapsingToolbar.setVisibility(View.GONE);
                            mLlButtonGroup.setVisibility(View.GONE);
                        }
                    });
            RetrofitUtils.getService().checkIsLearning(UserInfo.id, cid).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Result>() {
                        @Override
                        public void accept(Result result) throws Exception {
                            isLearning = result.isLearning();
                            btStartLearning.setText(isLearning ? "继续学习" : "开始学习");
                            if (isLearning)showButtonComplete();
                            isLearningFinish = true;
                            if (detailFinish) {
                                showSuccess();
//                                collapsingToolbar.setVisibility(View.VISIBLE);
                                mLlButtonGroup.setVisibility(View.VISIBLE);
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
//                            new AlertDialog.Builder(LessonDetailActivity.this).setMessage("错误" + throwable).setPositiveButton("确定", null)
//                                    .show();
                            showError();
//                            collapsingToolbar.setVisibility(View.GONE);
                            mLlButtonGroup.setVisibility(View.GONE);
                        }
                    });
        }
    }

    private void setView(Course detail) {
        tvDescription.setText(detail.getDescription());
//        tvKownledge.setText(detail.getKnowledge());
        sortChapters(detail);
        mAdapter.setData(detail.getChapters());
        Teacher teacher = detail.getTeacher();
        tvTeacherInfo.setText(teacher.getRealname());
        Glide.with(UiUtils.getContext()).load(Constant.SERVER_URL + teacher.getImageURL())
                .transform(new GlideRoundTransform(this))
                .placeholder(R.drawable.ic_account_circle_blue_600_24dp)
                .into(ivTeacherImage);
        ratingRank.setRating(detail.getAvgRank());
        collapsingToolbar.setTitle(detail.getName());
    }

    private void sortChapters(Course detail) {
        Collections.sort(detail.getChapters(), new Comparator<Chapter>() {
            @Override
            public int compare(Chapter lhs, Chapter rhs) {
                return lhs.getId() - rhs.getId();
            }
        });
        for (Chapter chapter : detail.getChapters()) {
            Collections.sort(chapter.getLessons(), new Comparator<Lesson>() {
                @Override
                public int compare(Lesson lhs, Lesson rhs) {
                    return lhs.getId() - rhs.getId();
                }
            });
        }
    }


    @OnClick({R.id.bt_start_learning, R.id.bt_exercise})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_start_learning:
                if (!isLearning) {
                    showButtonLoading();
                    RetrofitUtils.getService().startLearn(UserInfo.id, cid).subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<Result>() {
                                @Override
                                public void accept(Result result) throws Exception {
//                                    Intent intent = new Intent(LessonDetailActivity.this, VideoActivity.class);
//                                    intent.putExtra("cid", cid);
//                                    intent.putExtra("gid", gid);
//                                    startActivity(intent);
                                    showButtonComplete();
                                    isLearning = true;
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
//                                    Toast.makeText(LessonDetailActivity.this, "错误:" + throwable, Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Intent intent = new Intent(LessonDetailActivity.this, VideoActivity.class);
                    intent.putExtra("cid", cid);
                    intent.putExtra("gid", gid);
                    startActivity(intent);
                }
                break;
            case R.id.bt_exercise:
                Intent intent = new Intent(this, ExerciseCategoryActivity.class);
                intent.putExtra("cid",cid);
                startActivity(intent);
                break;
        }
    }

    private void showButtonComplete() {
        btStartLearning.setText("继续学习");
        btStartLearning.setCompoundDrawables(null, null, null, null);
        mBtExercise.setVisibility(View.VISIBLE);
    }

    private void showButtonLoading() {
        btStartLearning.setText("加载中");
        btStartLearning.setCompoundDrawables(mWave, null, null, null);
        mBtExercise.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
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
