package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.adapter.RecyclerExerciseCategoryAdapter;
import hl.iss.whu.edu.laboratoryproject.constant.Constant;
import hl.iss.whu.edu.laboratoryproject.entity.ExerciseCategory;
import hl.iss.whu.edu.laboratoryproject.listener.OnRecyclerViewItemClickListener;
import hl.iss.whu.edu.laboratoryproject.manager.SyLinearLayoutManager;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UserInfo;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ExerciseCategoryActivity extends BaseInternetRequestActivity {

    @Bind(R.id.recycler_exercise_category)
    RecyclerView mRecyclerCategoryExercise;
    private int mCid;
    private RecyclerExerciseCategoryAdapter mExerciseCategoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_category);
        ButterKnife.bind(this);
        mCid = getIntent().getIntExtra("cid", 0);
        initView();
        initData();
    }

    private void initData() {
        showProgress();
        RetrofitUtils.getService().getExerciseCategory(UserInfo.id,mCid)
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<ExerciseCategory>>() {
                    @Override
                    public void accept(ArrayList<ExerciseCategory> categories) throws Exception {
                        mExerciseCategoryAdapter.setData(categories);
                        showSuccess();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showError();
                    }
                });
//        observable.filter(new Predicate<ExerciseCategory>() {
//            @Override
//            public boolean test(ExerciseCategory category) throws Exception {
//                return category.getType() == 0;
//            }
//        }).toList()
//                .subscribe(new Consumer<List<ExerciseCategory>>() {
//                    @Override
//                    public void accept(List<ExerciseCategory> categories) throws Exception {
//                        mExerciseCategoryAdapter.setData(categories);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//
//                    }
//                });
//        observable.filter(new Predicate<ExerciseCategory>() {
//            @Override
//            public boolean test(ExerciseCategory category) throws Exception {
//                return category.getType() == 1;
//            }
//        }).toList()
//                .subscribe(new Consumer<List<ExerciseCategory>>() {
//                    @Override
//                    public void accept(List<ExerciseCategory> categories) throws Exception {
//                        mPeriodExerciseAdapter.setData(categories);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                    }
//                });

    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mExerciseCategoryAdapter = new RecyclerExerciseCategoryAdapter(new ArrayList<ExerciseCategory>());
        mRecyclerCategoryExercise.setLayoutManager(new SyLinearLayoutManager(this));
        mRecyclerCategoryExercise.setAdapter(mExerciseCategoryAdapter);
        mExerciseCategoryAdapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<ExerciseCategory>() {
            @Override
            public void onItemClick(View v, ExerciseCategory data) {
                Intent intent = new Intent(ExerciseCategoryActivity.this,ExerciseActivity.class);
                intent.putExtra("type", Constant.INTENT_TYPE_NORMAL);
                intent.putExtra("id",data.getId());
                intent.putExtra("title",data.getName());
                startActivityForResult(intent,Constant.REQURST_EXERCISE);
            }
        });
//        mPeriodExerciseAdapter = new RecyclerExerciseCategoryAdapter(new ArrayList<ExerciseCategory>());
//        mRecyclerPeriodExercise.setLayoutManager(new SyLinearLayoutManager(this));
//        mRecyclerPeriodExercise.setAdapter(mPeriodExerciseAdapter);
    }

    @OnClick(R.id.tv_train)
    public void onClick() {
        Intent intent = new Intent(ExerciseCategoryActivity.this,ExerciseActivity.class);
        intent.putExtra("type", Constant.INTENT_TYPE_RECOMMENDED);
        intent.putExtra("id",mCid);
        intent.putExtra("title","章节针对性训练");
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==Constant.REQURST_EXERCISE && resultCode == RESULT_OK){
            initData();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void retry() {
        initData();
    }
}
