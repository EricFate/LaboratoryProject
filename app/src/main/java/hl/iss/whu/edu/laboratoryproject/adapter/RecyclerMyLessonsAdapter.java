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
import hl.iss.whu.edu.laboratoryproject.entity.Course;
import hl.iss.whu.edu.laboratoryproject.entity.CourseLearning;
import hl.iss.whu.edu.laboratoryproject.entity.Teacher;
import hl.iss.whu.edu.laboratoryproject.utils.TimeUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;

/**
 * Created by fate on 2016/10/29.
 */

public class RecyclerMyLessonsAdapter extends BaseRecyclerViewAdapter<CourseLearning, RecyclerMyLessonsAdapter.ListViewHoder> {


    public RecyclerMyLessonsAdapter(ArrayList<CourseLearning> data) {
        super(data);
    }


    @Override
    public ListViewHoder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_my_lessons, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RecyclerMyLessonsAdapter.this.mListener != null)
                    RecyclerMyLessonsAdapter.this.mListener.onItemClick(v, (CourseLearning) v.getTag());
            }
        });
        return new ListViewHoder(view);

    }

    @Override
    public void onBindViewHolder(ListViewHoder holder, int position) {
        CourseLearning learning = data.get(position);
        Course course = learning.getCourse();
        holder.itemView.setTag(learning);
        holder.tvName.setText(course.getName());
        holder.tvChapterNumber.setText(course.getChapterNumber()+"个章节");
        holder.tvDuration.setText("已学习"+ TimeUtils.getLearningTime(learning.getDuration()));
        Teacher teacher = course.getTeacher();
        holder.tvTeacher.setText(teacher.getRealname());
        Glide.with(UiUtils.getContext())
                .load(Constant.SERVER_URL+ course.getCoverURL())
                .placeholder(R.drawable.ic_account_circle_blue_600_24dp)
                .into(holder.ivImage);

    }

    static class ListViewHoder extends RecyclerView.ViewHolder {
        TextView tvName;
        View itemView;
        TextView tvTeacher;
        TextView tvChapterNumber;
        ImageView ivImage;
        TextView tvDuration;
        public ListViewHoder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvName = ButterKnife.findById(itemView,R.id.tv_class_name);
            tvChapterNumber = ButterKnife.findById(itemView,R.id.tv_chapter_number);
            tvTeacher = ButterKnife.findById(itemView,R.id.tv_class_teacher);
            ivImage = ButterKnife.findById(itemView,R.id.iv_class);
            tvDuration = ButterKnife.findById(itemView,R.id.tv_duration);

        }
    }


}



