package hl.iss.whu.edu.laboratoryproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.entity.Issue;
import hl.iss.whu.edu.laboratoryproject.entity.Rank;
import hl.iss.whu.edu.laboratoryproject.listener.OnDeleteClickListener;
import hl.iss.whu.edu.laboratoryproject.utils.TimeUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;

/**
 * Created by fate on 2017/2/19.
 */

public class RecyclerMyRanksAdapter extends BaseRecyclerViewAdapter<Rank,RecyclerMyRanksAdapter.MyRanksViewHolder> {
    private OnDeleteClickListener mOnDeleteClickListener;

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        mOnDeleteClickListener = onDeleteClickListener;
    }
    public RecyclerMyRanksAdapter(ArrayList data) {
        super(data);
    }

    @Override
    public MyRanksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_my_ranks, parent, false);
        return new MyRanksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyRanksViewHolder holder, final int position) {
        final Rank rank = data.get(position);
        holder.tvTime.setText(TimeUtils.format(rank.getTime()) );
        holder.tvContent.setText(rank.getContent());
        holder.tvCourseName.setText(rank.getCourse().getName());
        holder.rbRank.setRating(rank.getRank());
        holder.ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnDeleteClickListener!=null)
                    mOnDeleteClickListener.onDeleteClick(rank.getId(),position);
            }
        });
    }
    public void remove(final int position){
        data.remove(position);
        UiUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                notifyItemRemoved(position);
                notifyItemRangeChanged(0,data.size());
            }
        });
    }

    class MyRanksViewHolder extends RecyclerView.ViewHolder{
        TextView tvTime;
        TextView tvCourseName;
        TextView tvContent;
        ImageButton ibDelete;
        RatingBar rbRank;
        public MyRanksViewHolder(View itemView) {
            super(itemView);
            tvTime = ButterKnife.findById(itemView,R.id.tv_time);
            tvCourseName = ButterKnife.findById(itemView,R.id.tv_course_name);
            tvContent = ButterKnife.findById(itemView,R.id.tv_rank_content);
            ibDelete = ButterKnife.findById(itemView,R.id.ib_delete);
            rbRank = ButterKnife.findById(itemView,R.id.rating_rank);
        }
    }
}
