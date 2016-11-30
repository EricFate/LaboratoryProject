package hl.iss.whu.edu.laboratoryproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.constant.Constant;
import hl.iss.whu.edu.laboratoryproject.entity.Subject;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;

/**
 * Created by fate on 2016/11/13.
 */

public class RecyclerLessonSelectAdapter extends BaseRecyclerViewAdapter<Subject,RecyclerLessonSelectAdapter.MyViewHolder> {

    public RecyclerLessonSelectAdapter(ArrayList<Subject> data) {
        super(data);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_lesson_selection,parent,false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(v, (Subject) v.getTag());
            }
        });
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Subject subject = data.get(position);
        Glide.with(UiUtils.getContext())
                .load(Constant.SERVER_URL+subject.getImgURL())
                .placeholder(R.drawable.bg)
                .into(holder.ivLesson);
        holder.tvName.setText(subject.getName());
        holder.tvDuration.setText(subject.getDuration());
        holder.tvNumber.setText(subject.getNumber());
        holder.itemView.setTag(data.get(position));
    }

     static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivLesson;
        TextView tvName;
        TextView tvDuration;
        TextView tvNumber;
         View itemView;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ivLesson = ButterKnife.findById(itemView, R.id.iv_item_lesson_seletion);
            tvName = ButterKnife.findById(itemView, R.id.tv_item_selection_lesson_name);
            tvDuration = ButterKnife.findById(itemView, R.id.tv_item_selection_lesson_duration);
            tvNumber = ButterKnife.findById(itemView, R.id.tv_item_selection_lesson_numbers);

        }
    }
}
