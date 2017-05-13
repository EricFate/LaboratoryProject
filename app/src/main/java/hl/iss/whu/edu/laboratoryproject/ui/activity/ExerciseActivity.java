package hl.iss.whu.edu.laboratoryproject.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.adapter.RecyclerExerciseAdapter;
import hl.iss.whu.edu.laboratoryproject.adapter.RecyclerExerciseResultAdapter;
import hl.iss.whu.edu.laboratoryproject.constant.Constant;
import hl.iss.whu.edu.laboratoryproject.entity.Exercise;
import hl.iss.whu.edu.laboratoryproject.entity.Result;
import hl.iss.whu.edu.laboratoryproject.manager.SyLinearLayoutManager;
import hl.iss.whu.edu.laboratoryproject.ui.view.DividerItemDecoration;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UserInfo;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ExerciseActivity extends BaseInternetWithEmptyActivity {

    @Bind(R.id.recycler_general)
    RecyclerView mRecyclerExercise;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.tv_total)
    TextView mTvTotal;
    @Bind(R.id.tv_progress)
    TextView mTvProgress;
    @Bind(R.id.bt_finish)
    Button mBtFinish;
    private RecyclerExerciseAdapter mAdapter;
    private int current;
    private int progress;
    private String mType;
    private int mId;
    private boolean allComplete = false;
    private int mEx_num;
    private SparseArray<Integer> checked = new SparseArray<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        ButterKnife.bind(this);
        mType = getIntent().getStringExtra("type");
        mId = getIntent().getIntExtra("id", 0);
        SharedPreferences preferences = getSharedPreferences(Constant.PREFERENCE_USERINFO, MODE_PRIVATE);
        mEx_num = preferences.getInt(Constant.KEY_EXERCISE_NUMBER, 5);
        initView();
        initData();

    }

    private void initData() {
        showProgress();
        Log.e("ExerciseActivity", "initData: " + mId);
        RetrofitUtils.getService().getExercise(mId, current, mType, mEx_num)
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<Exercise>>() {
                    @Override
                    public void accept(ArrayList<Exercise> exercises) throws Exception {
                        if (exercises.size() == 0) {
                            showEmpty(R.drawable.complete, "你已完成所有题目");
                            allComplete = true;
                            mTvTotal.setText("/" + exercises.size());
                            progress = 0;
                            mTvProgress.setText(progress + "");
                        } else {
                            mAdapter.setData(exercises);
                            current += exercises.size();
                            mTvTotal.setText("/" + exercises.size());
                            progress = 0;
                            mTvProgress.setText(progress + "");
                            showSuccess();
                            if (exercises.size() < mEx_num)
                                allComplete = true;
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        showError();
                    }
                });
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTvTitle.setText(getIntent().getStringExtra("title"));
        mAdapter = new RecyclerExerciseAdapter(new ArrayList<Exercise>());
        mRecyclerExercise.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerExercise.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerExercise.setAdapter(mAdapter);
        mAdapter.setSelectionListener(new RecyclerExerciseAdapter.OnSelectionListener() {
            @Override
            public void onSelection(int selection, int position) {
                if (selection==-1){
                    progress --;
                }else {
                    if (checked.get(position)==null||checked.get(position)==-1)
                        progress++;
                }
                updateProgress();
                checked.put(position,selection);
            }
        });
        toolbar.setTitle(getIntent().getStringExtra("title"));


    }

    private void updateProgress() {
        mTvProgress.setText(""+progress);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exercise, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.menu_finish:
//                checkAnswers();
//                break;
            case R.id.menu_next:
                loadNext();
                break;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadNext() {
        if (allComplete) {
            showEmpty(R.drawable.complete, "你已完成所有题目");
            mTvProgress.setText("0");
            mTvTotal.setText("/0");
        } else {
            initData();
        }
    }

//    private Map<Integer, Integer> checkAnswers() {
//        Map<Integer, Integer> result = new HashMap<>();
//        for (int i = 0; i < mAdapter.getItemCount(); i++) {
//            View view = mRecyclerExercise.getChildAt(i);
//            RecyclerExerciseAdapter.ExerciseViewHolder holder = (RecyclerExerciseAdapter.ExerciseViewHolder) mRecyclerExercise.getChildViewHolder(view);
//            int checkedRadioButtonId = holder.mRgSelection.getCheckedRadioButtonId();
//            if (checkedRadioButtonId == -1) continue;
////            switch (holder.mRgSelection.getCheckedRadioButtonId()) {
////                case R.id.tv_answer_A:
////                    holder.mTvAnswerA.setTextColor(getResources().getColor(android.R.color.holo_red_light));
////                    break;
////                case R.id.tv_answer_B:
////                    holder.mTvAnswerB.setTextColor(getResources().getColor(android.R.color.holo_red_light));
////                    break;
////                case R.id.tv_answer_C:
////                    holder.mTvAnswerC.setTextColor(getResources().getColor(android.R.color.holo_red_light));
////                    break;
////                case R.id.tv_answer_D:
////                    holder.mTvAnswerD.setTextColor(getResources().getColor(android.R.color.holo_red_light));
////                    break;
////            }
//
//            Log.e("?????", "checkAnswers: " + checkedRadioButtonId);
//            ((RadioButton) ButterKnife.findById(holder.itemView, checkedRadioButtonId))
//                    .setTextColor(getResources().getColor(android.R.color.holo_red_light));
//            Exercise tag = (Exercise) holder.itemView.getTag();
//            fillMap(result, tag, checkedRadioButtonId);
//            showRightAnswer(tag.getAnswer(), holder);
//            holder.mLlAnalysis.setVisibility(View.VISIBLE);
//            holder.mRgSelection.clearCheck();
//            for (int j = 0; j < holder.mRgSelection.getChildCount(); j++) {
//                holder.mRgSelection.getChildAt(j).setEnabled(false);
//            }
//        }
//        return result;
//    }

//    private void fillMap(Map<Integer, Integer> result, Exercise tag, int id) {
//        int select = 0;
//        switch (id) {
//            case R.id.tv_answer_A:
//                select = 1;
//                break;
//            case R.id.tv_answer_B:
//                select = 2;
//                break;
//            case R.id.tv_answer_C:
//                select = 3;
//                break;
//            case R.id.tv_answer_D:
//                select = 4;
//                break;
//        }
//        result.put(tag.getId(),tag.getAnswer()==select?1:0);
//    }
//
//    private void showRightAnswer(int answer, RecyclerExerciseAdapter.ExerciseViewHolder holder) {
//        switch (answer) {
//            case 1:
//                holder.mTvAnswerA.setTextColor(getResources().getColor(R.color.colorPrimary));
//                break;
//            case 2:
//                holder.mTvAnswerB.setTextColor(getResources().getColor(R.color.colorPrimary));
//                break;
//            case 3:
//                holder.mTvAnswerC.setTextColor(getResources().getColor(R.color.colorPrimary));
//                break;
//            case 4:
//                holder.mTvAnswerD.setTextColor(getResources().getColor(R.color.colorPrimary));
//                break;
//        }
//    }


    @OnClick(R.id.bt_finish)
    public void onClick() {
        Map<Integer, Integer> map = checkAnswers();
        uploadResult(map);
    }
    private void uploadResult(Map<Integer, Integer> map) {
        RetrofitUtils.getService().uploadExerciseResult(UserInfo.id,new Gson().toJson(map),null)
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Result>() {
                    @Override
                    public void accept(Result result) throws Exception {
//                        Toast.makeText(getActivity(), "上传成功", Toast.LENGTH_SHORT).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
//                        Toast.makeText(getActivity(), "失败:"+throwable, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private Map<Integer, Integer> checkAnswers() {
        Map<Integer, Integer> result = new HashMap<>();
        List<Exercise> data = mAdapter.getData();
        mRecyclerExercise.setAdapter(new RecyclerExerciseResultAdapter(data,checked));
        for (int i = 0; i < data.size(); i++) {
            int id = data.get(i).getId();
            int answer = data.get(i).getAnswer();
            int check = this.checked.get(i, -1);
            result.put(id,check==answer-1?1:0);
        }
        return result;
    }




    @Override
    protected void retry() {
        initData();
    }

    @Override
    protected void showProgress() {
        super.showProgress();
        mBtFinish.setVisibility(View.GONE);
    }

    @Override
    protected void showError() {
        super.showError();
        mBtFinish.setVisibility(View.GONE);
    }

    @Override
    protected void showSuccess() {
        super.showSuccess();
        mBtFinish.setVisibility(View.VISIBLE);
    }

    @Override
    protected void showEmpty(@DrawableRes int drawableId, String text) {
        super.showEmpty(drawableId, text);
        mBtFinish.setVisibility(View.GONE);
    }
}
