package hl.iss.whu.edu.laboratoryproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.entity.ExerciseCategory;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;

/**
 * Created by fate on 2017/4/2.
 */

public class RecyclerExerciseCategoryAdapter extends BaseRecyclerViewAdapter<ExerciseCategory,RecyclerExerciseCategoryAdapter.ExerciseViewHolder> {


    public RecyclerExerciseCategoryAdapter(List<ExerciseCategory> data) {
        super(data);
    }

    @Override
    public ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_chapter_exercise, parent, false);
        return new ExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExerciseViewHolder holder, int position) {
        ExerciseCategory category = data.get(position);
        holder.itemView.setTag(category);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!=null)
                    mListener.onItemClick(v, (ExerciseCategory) v.getTag());
            }
        });
        holder.tvName.setText(category.getName());
        double accuracy = category.getTotalAnswer()==0?0:((double)category.getCorrect()/category.getTotalAnswer())*100;
        holder.tvAccuracy.setText("正确率:"+String.format("%.2f",accuracy)+"%");
        holder.tvTotal.setText(category.getTotal()+"");
        holder.tvComplete.setText(category.getProgress()+"");
        holder.progressBar.setMax(category.getTotal());
        holder.progressBar.setProgress(category.getProgress());
    }

    static class ExerciseViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        TextView tvComplete;
        TextView tvTotal;
        TextView tvAccuracy;
        ProgressBar progressBar;
        public ExerciseViewHolder(View itemView) {
            super(itemView);
            tvName = ButterKnife.findById(itemView, R.id.tv_title);
            tvComplete = ButterKnife.findById(itemView, R.id.tv_complete);
            tvTotal = ButterKnife.findById(itemView, R.id.tv_total);
            tvAccuracy = ButterKnife.findById(itemView, R.id.tv_accuracy);
            progressBar = ButterKnife.findById(itemView,R.id.progressBar);
        }
    }
}
