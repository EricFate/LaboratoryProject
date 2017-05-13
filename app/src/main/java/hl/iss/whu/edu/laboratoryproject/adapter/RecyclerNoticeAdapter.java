package hl.iss.whu.edu.laboratoryproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.entity.Notice;
import hl.iss.whu.edu.laboratoryproject.utils.TimeUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;

/**
 * Created by fate on 2017/5/9.
 */

public class RecyclerNoticeAdapter extends BaseRecyclerViewAdapter<Notice,RecyclerNoticeAdapter.ViewHolder> {

    public RecyclerNoticeAdapter(List<Notice> data) {
        super(data);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_notice, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Notice notice = data.get(position);
        holder.mTvTitle.setText(notice.getName());
        holder.mEtvNoticeContent.setText(notice.getContent());
        holder.mTvTime.setText(TimeUtils.format(notice.getDate()));
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.tv_title)
        TextView mTvTitle;
        @Bind(R.id.etv_notice_content)
        ExpandableTextView mEtvNoticeContent;
        @Bind(R.id.tv_time)
        TextView mTvTime;
        public ViewHolder(View itemView) {
            super(itemView);
        ButterKnife.bind(this, itemView);
        }


    }
}
