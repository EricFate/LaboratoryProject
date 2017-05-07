package hl.iss.whu.edu.laboratoryproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.entity.Issue;
import hl.iss.whu.edu.laboratoryproject.listener.OnDeleteClickListener;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;

/**
 * Created by fate on 2017/2/18.
 */

public class RecyclerMyIssuesAdapter extends BaseRecyclerViewAdapter<Issue,RecyclerMyIssuesAdapter.MyAnswersViewHolder> {
    public RecyclerMyIssuesAdapter(ArrayList<Issue> data) {
        super(data);
    }
    private OnDeleteClickListener mOnDeleteClickListener;

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        mOnDeleteClickListener = onDeleteClickListener;
    }

    @Override
    public MyAnswersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_my_issues, parent, false);

        return new MyAnswersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyAnswersViewHolder holder, int position) {
        final Issue issue = data.get(position);
        holder.tvTitle.setText(issue.getTitle());
        holder.itemView.setTag(issue);
        holder.tvNumber.setText(issue.getAnswerNumber()+"人回答");
        holder.tvDetail.setText(issue.getContent());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!=null)
                    mListener.onItemClick(v, (Issue) v.getTag());
            }
        });
        holder.ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnDeleteClickListener!=null)
                    mOnDeleteClickListener.onDeleteClick(issue.getId());
            }
        });
    }
    public void remove(int id){
        for (Issue issue : data) {
            if (issue.getId()==id)
                data.remove(issue);
        }
        UiUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }
    class MyAnswersViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle;
        TextView tvNumber;
        ExpandableTextView tvDetail;
        ImageButton ibDelete;
        public MyAnswersViewHolder(View itemView) {
            super(itemView);
            tvTitle = ButterKnife.findById(itemView, R.id.tv_title);
            tvNumber = ButterKnife.findById(itemView, R.id.tv_discover_number);
            tvDetail = ButterKnife.findById(itemView, R.id.etv_question_content);
            ibDelete = ButterKnife.findById(itemView, R.id.ib_delete);
        }
    }
}
