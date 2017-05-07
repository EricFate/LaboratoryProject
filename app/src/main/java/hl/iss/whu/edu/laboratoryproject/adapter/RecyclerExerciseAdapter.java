package hl.iss.whu.edu.laboratoryproject.adapter;

import android.support.v7.widget.RecyclerView;
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
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;

/**
 * Created by fate on 2017/4/9.
 */

public class RecyclerExerciseAdapter extends BaseRecyclerViewAdapter<Exercise, RecyclerExerciseAdapter.ExerciseViewHolder> {

    private OnAnswerCheckedListener mOnAnswerCheckedListener;


    public RecyclerExerciseAdapter(ArrayList<Exercise> data) {
        super(data);
    }
    public void setOnAnswerCheckedListener(OnAnswerCheckedListener onAnswerCheckedListener) {
        mOnAnswerCheckedListener = onAnswerCheckedListener;
    }

    @Override
    public ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ExerciseViewHolder holder, int position) {
        Exercise exercise = data.get(position);
        holder.itemView.setTag(exercise);
        holder.mTvQuestion.setText(exercise.getTitle());
        holder.mTvAnswerA.setText("A: "+exercise.getOptionA());
        holder.mTvAnswerB.setText("B: "+exercise.getOptionB());
        holder.mTvAnswerC.setText("C: "+exercise.getOptionC());
        holder.mTvAnswerD.setText("D: "+exercise.getOptionD());
        holder.mTvAnalysis.setText(exercise.getAnalysis());
        holder.mRgSelection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                if (checkedId==holder.checkedId&&checkedId!=-1){
//                    group.clearCheck();
//                }
                if (mOnAnswerCheckedListener!=null)
                    mOnAnswerCheckedListener.onAnswerChecked(group,checkedId);
//                holder.checkedId=-1;
            }
        });
    }


    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_question)
        public TextView mTvQuestion;
        @Bind(R.id.tv_answer_A)
        public RadioButton mTvAnswerA;
        @Bind(R.id.tv_answer_B)
        public RadioButton mTvAnswerB;
        @Bind(R.id.tv_answer_C)
        public RadioButton mTvAnswerC;
        @Bind(R.id.tv_answer_D)
        public RadioButton mTvAnswerD;
        @Bind(R.id.rg_selection)
        public RadioGroup mRgSelection;
        @Bind(R.id.ll_analysis)
        public LinearLayout mLlAnalysis;
        @Bind(R.id.tv_analysis)
        public TextView mTvAnalysis;
        public ExerciseViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnAnswerCheckedListener {
        void onAnswerChecked(RadioGroup group,int checkedId);
    }
}
