package hl.iss.whu.edu.laboratoryproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.entity.Exercise;
import hl.iss.whu.edu.laboratoryproject.ui.view.SelectionView;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;

/**
 * Created by fate on 2017/5/10.
 */

public class RecyclerExerciseResultAdapter extends BaseRecyclerViewAdapter<Exercise, RecyclerExerciseResultAdapter.ViewHolder> {

    private SparseArray<Integer> checked;

    public RecyclerExerciseResultAdapter(List<Exercise> data, SparseArray<Integer> checked) {
        super(data);
        this.checked = checked;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_exercise, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Exercise exercise = data.get(position);
        holder.itemView.setTag(exercise);
        holder.mTvQuestion.setText(exercise.getTitle());
        holder.mOptionA.setText(exercise.getOptionA());
        holder.mOptionB.setText(exercise.getOptionB());
        holder.mOptionC.setText(exercise.getOptionC());
        holder.mOptionD.setText(exercise.getOptionD());
        holder.mTvAnalysis.setText(exercise.getAnalysis());
        holder.mLlAnalysis.setVisibility(View.VISIBLE);
        if (exercise.getDifficulty() == 0)
            holder.mTvDifficulty.setVisibility(View.GONE);
        else
            holder.mTvDifficulty.setText("难度:" + exercise.getDifficulty());
        if (exercise.getTotal() == 0)
            holder.mTvAccuracy.setVisibility(View.GONE);
        else
            holder.mTvAccuracy.setText(String.format("正确率: %.2f",(double) exercise.getCorrect() / exercise.getTotal()*100 )+"%");
        Integer index = checked.get(position);
        if (index != -1)
            holder.mViews[index].setError();
        holder.mViews[exercise.getAnswer() - 1].check();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_question)
        TextView mTvQuestion;
        @Bind(R.id.option_A)
        SelectionView mOptionA;
        @Bind(R.id.option_B)
        SelectionView mOptionB;
        @Bind(R.id.option_C)
        SelectionView mOptionC;
        @Bind(R.id.option_D)
        SelectionView mOptionD;
        SelectionView[] mViews;
        @Bind(R.id.tv_difficulty)
        TextView mTvDifficulty;
        @Bind(R.id.tv_accuracy)
        TextView mTvAccuracy;
        @Bind(R.id.tv_analysis)
        TextView mTvAnalysis;
        @Bind(R.id.ll_analysis)
        LinearLayout mLlAnalysis;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mViews = new SelectionView[]{mOptionA, mOptionB, mOptionC, mOptionD};
        }


    }
}
