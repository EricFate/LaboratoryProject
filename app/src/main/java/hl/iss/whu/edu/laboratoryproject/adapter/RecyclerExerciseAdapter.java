package hl.iss.whu.edu.laboratoryproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Selection;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.entity.Exercise;
import hl.iss.whu.edu.laboratoryproject.ui.view.SelectionView;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;

/**
 * Created by fate on 2017/4/9.
 */

public class RecyclerExerciseAdapter extends BaseRecyclerViewAdapter<Exercise, RecyclerExerciseAdapter.ExerciseViewHolder> {
    private OnSelectionListener mSelectionListener;

    public void setSelectionListener(OnSelectionListener selectionListener) {
        mSelectionListener = selectionListener;
    }

    public RecyclerExerciseAdapter(ArrayList<Exercise> data) {
        super(data);
    }


    @Override
    public ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ExerciseViewHolder holder, final int position) {
        Exercise exercise = data.get(position);
        holder.itemView.setTag(exercise);
        holder.mTvQuestion.setText(exercise.getTitle());
        holder.mTvAnswerA.setText(exercise.getOptionA());
        holder.mTvAnswerB.setText(exercise.getOptionB());
        holder.mTvAnswerC.setText(exercise.getOptionC());
        holder.mTvAnswerD.setText(exercise.getOptionD());
        holder.mTvAnalysis.setText(exercise.getAnalysis());
        if (exercise.getDifficulty() == 0)
            holder.mTvDifficulty.setVisibility(View.GONE);
        else
            holder.mTvDifficulty.setText("难度:" + exercise.getDifficulty());
        if (exercise.getTotal() == 0)
            holder.mTvAccuracy.setVisibility(View.GONE);
        else
            holder.mTvAccuracy.setText(String.format("正确率: %.2f",(double) exercise.getCorrect() / exercise.getTotal()*100 )+"%" );
        for (final SelectionView view : holder.mViews) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.checkedIndex != -1 && holder.checkedIndex != view.getType()) {
                        holder.mViews[holder.checkedIndex].check();
                    }
                    view.check();
                    holder.checkedIndex = view.isChecked() ? view.getType() : -1;
                    if (mSelectionListener != null)
                        mSelectionListener.onSelection(holder.checkedIndex, position);

                }
            });
        }
    }


    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_question)
        public TextView mTvQuestion;
        @Bind(R.id.option_A)
        public SelectionView mTvAnswerA;
        @Bind(R.id.option_B)
        public SelectionView mTvAnswerB;
        @Bind(R.id.option_C)
        public SelectionView mTvAnswerC;
        @Bind(R.id.option_D)
        public SelectionView mTvAnswerD;
        @Bind(R.id.tv_difficulty)
        TextView mTvDifficulty;
        @Bind(R.id.tv_accuracy)
        TextView mTvAccuracy;
        SelectionView[] mViews;
        int checkedIndex = -1;
        @Bind(R.id.ll_analysis)
        public LinearLayout mLlAnalysis;
        @Bind(R.id.tv_analysis)
        public TextView mTvAnalysis;

        public ExerciseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mViews = new SelectionView[]{mTvAnswerA, mTvAnswerB, mTvAnswerC, mTvAnswerD};
        }
    }

    public interface OnSelectionListener {
        void onSelection(int selection, int position);
    }

}
