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
import hl.iss.whu.edu.laboratoryproject.entity.Answer;
import hl.iss.whu.edu.laboratoryproject.entity.Chatter;
import hl.iss.whu.edu.laboratoryproject.glide.GlideRoundTransform;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;

/**
 * Created by fate on 2016/12/9.
 */

public class RecyclerAnswerAdapter extends BaseRecyclerViewAdapter<Answer,RecyclerAnswerAdapter.MyViewHolder> {
    public RecyclerAnswerAdapter(ArrayList<Answer> data) {
        super(data);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_answer, parent, false);

        return new RecyclerAnswerAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Answer answer = data.get(position);
        Glide.with(UiUtils.getContext())
                .load(Constant.SERVER_URL+ answer.getAnswerer().getImageURL())
                .placeholder(R.drawable.ic_account_circle_blue_600_24dp)
                .transform(new GlideRoundTransform(UiUtils.getContext()))
                .into(holder.image);
        holder.tvName.setText(answer.getAnswerer().getNickname());
        holder.tvAnswer.setText(answer.getContent());
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView tvName;
        TextView tvAnswer;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = ButterKnife.findById(itemView, R.id.iv_answer_image);
            tvName = ButterKnife.findById(itemView, R.id.tv_answer_name);
            tvAnswer = ButterKnife.findById(itemView, R.id.tv_answer_content);
        }
    }
}
