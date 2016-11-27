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
import hl.iss.whu.edu.laboratoryproject.entity.Chatter;
import hl.iss.whu.edu.laboratoryproject.glide.GlideRoundTransform;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;

/**
 * Created by fate on 2016/11/18.
 */

public class RecyclerChatAdapter extends BaseRecyclerViewAdapter<Chatter, RecyclerChatAdapter.ChatViewHolder> {
    public RecyclerChatAdapter(ArrayList<Chatter> data) {
        super(data);
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_discuss, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        Chatter chatter = data.get(position);
        Glide.with(UiUtils.getContext())
                .load(chatter.getImageURL())
                .placeholder(R.drawable.ic_account_circle_blue_600_24dp)
                .transform(new GlideRoundTransform(UiUtils.getContext()))
                .into(holder.image);
        holder.tvName.setText(chatter.getName());
        holder.tvWord.setText(chatter.getMessage());
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView tvName;
        TextView tvWord;

        public ChatViewHolder(View itemView) {
            super(itemView);
            image = ButterKnife.findById(itemView, R.id.iv_chat_image);
            tvName = ButterKnife.findById(itemView, R.id.tv_chat_name);
            tvWord = ButterKnife.findById(itemView, R.id.tv_chat_word);
        }
    }
}