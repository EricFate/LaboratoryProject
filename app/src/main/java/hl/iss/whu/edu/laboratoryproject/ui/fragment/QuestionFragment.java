package hl.iss.whu.edu.laboratoryproject.ui.fragment;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.adapter.RecyclerExerciseAdapter;
import hl.iss.whu.edu.laboratoryproject.adapter.RecyclerExerciseResultAdapter;
import hl.iss.whu.edu.laboratoryproject.adapter.RecyclerQuestionAdapter;
import hl.iss.whu.edu.laboratoryproject.entity.Exercise;
import hl.iss.whu.edu.laboratoryproject.entity.Lesson;
import hl.iss.whu.edu.laboratoryproject.entity.Question;
import hl.iss.whu.edu.laboratoryproject.entity.Result;
import hl.iss.whu.edu.laboratoryproject.manager.SyLinearLayoutManager;
import hl.iss.whu.edu.laboratoryproject.ui.activity.ExerciseActivity;
import hl.iss.whu.edu.laboratoryproject.ui.view.DividerItemDecoration;
import hl.iss.whu.edu.laboratoryproject.ui.view.LoadingPage;
import hl.iss.whu.edu.laboratoryproject.utils.MyDialog;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UserInfo;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by fate on 2016/12/8.
 */

public class QuestionFragment extends Fragment implements View.OnClickListener{
    private RecyclerView recyclerQuestion;
    private RecyclerExerciseAdapter mAdapter;
    private View rootView;
    private SyLinearLayoutManager mLayoutManager;
    private SparseArray<Integer> checked = new SparseArray<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_question, container, false);
        recyclerQuestion = ButterKnife.findById(rootView,R.id.recycler_general);
        ButterKnife.findById(rootView,R.id.bt_finish).setOnClickListener(this);
        initView();
        showEmpty("你尚未选择课时");
        return rootView;
    }



    private void initView() {
        mAdapter = new RecyclerExerciseAdapter(new ArrayList<Exercise>());
        mAdapter.setSelectionListener(new RecyclerExerciseAdapter.OnSelectionListener() {
            @Override
            public void onSelection(int selection, int position) {
                checked.put(position,selection);
            }
        });
        mLayoutManager = new SyLinearLayoutManager(getActivity());
        recyclerQuestion.setLayoutManager(mLayoutManager);
        recyclerQuestion.setAdapter(mAdapter);
        recyclerQuestion.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL_LIST));
    }

    public void refresh(final int lid){
        Log.e("questionfragment", "refresh: ======="+lid );
        if (lid!=0) {
            showProgress();
            RetrofitUtils.getService().getLessonExercise(lid).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ArrayList<Exercise>>() {
                        @Override
                        public void accept(ArrayList<Exercise> exercises) throws Exception {
                            Log.e("questionfragment", "accept: "+new Gson().toJson(exercises) );
                            if (exercises.size() == 0)
                                showEmpty("此课时无习题");
                            else {
                            refreshUI(exercises);
                            showSuccess();
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
//                            MyDialog.showAlertDialgo(getActivity(), "刷新错误" + throwable);
                            showError(lid);
                        }
                    });
        }
    }
    protected void showProgress(){
        rootView.findViewById(R.id.ll_loading).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.recycler_general).setVisibility(View.GONE);
        rootView.findViewById(R.id.ll_error).setVisibility(View.GONE);
        rootView.findViewById(R.id.ll_nodata).setVisibility(View.GONE);
        rootView.findViewById(R.id.bt_finish).setVisibility(View.GONE);
    }
    protected void showSuccess(){
        rootView.findViewById(R.id.ll_loading).setVisibility(View.GONE);
        rootView.findViewById(R.id.recycler_general).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.ll_error).setVisibility(View.GONE);
        rootView.findViewById(R.id.ll_nodata).setVisibility(View.GONE);
            rootView.findViewById(R.id.bt_finish).setVisibility(View.VISIBLE);
    }
    protected void showError(final int lid){
        rootView.findViewById(R.id.ll_loading).setVisibility(View.GONE);
        rootView.findViewById(R.id.recycler_general).setVisibility(View.GONE);
        rootView.findViewById(R.id.ll_error).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.ll_nodata).setVisibility(View.GONE);
        rootView.findViewById(R.id.bt_finish).setVisibility(View.GONE);
        rootView.findViewById(R.id.bt_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retry(lid);
            }
        });
    }

    private void retry(int lid) {
        refresh(lid);
    }

    protected void showEmpty( String text){
        rootView.findViewById(R.id.ll_loading).setVisibility(View.GONE);
        rootView.findViewById(R.id.recycler_general).setVisibility(View.GONE);
        rootView.findViewById(R.id.ll_error).setVisibility(View.GONE);
        rootView.findViewById(R.id.ll_nodata).setVisibility(View.VISIBLE);
        TextView textView = (TextView) rootView.findViewById(R.id.tv_nodata);
        textView.setText(text);
        rootView.findViewById(R.id.bt_finish).setVisibility(View.GONE);
    }
    private void refreshUI(ArrayList<Exercise> exercises) {
        mAdapter.setData(exercises);
    }

    @Override
    public void onClick(View v) {
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
        recyclerQuestion.setAdapter(new RecyclerExerciseResultAdapter(data,checked));
        for (int i = 0; i < data.size(); i++) {
            int id = data.get(i).getId();
            int answer = data.get(i).getAnswer();
            int check = this.checked.get(i, -1);
            result.put(id,check==answer-1?1:0);
        }
        return result;
    }

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
}
