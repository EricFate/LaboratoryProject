package hl.iss.whu.edu.laboratoryproject.adapter;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;

import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.entity.Chatter;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;

/**
 * Created by fate on 2016/12/8.
 */

public class RecyclerMessageAdapter extends BaseRecyclerViewAdapter<Chatter,RecyclerMessageAdapter.MyViewHolder> {


    public RecyclerMessageAdapter(ArrayList<Chatter> data) {
        super(data);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_message,parent,false);
        return new MyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(position);
                UiUtils.runInMainThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        });
        holder.llMessage.setTag(data.get(position));
        holder.llMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerMessageAdapter.this.mListener.onItemClick(v, (Chatter) v.getTag());
            }
        });
        holder.slMessage.setShowMode(SwipeLayout.ShowMode.PullOut);
        holder.slMessage.addDrag(SwipeLayout.DragEdge.Left,holder.tvDelete);

    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        View itemView;
        TextView tvName;
        TextView tvContent;
        ImageView ivImage;
        LinearLayout llMessage;
        TextView tvDelete;
        SwipeLayout slMessage;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView= itemView;
            tvName = ButterKnife.findById(itemView, R.id.tv_message_name);
            tvContent = ButterKnife.findById(itemView, R.id.tv_message_content);
            tvDelete = ButterKnife.findById(itemView, R.id.tv_delete);
            ivImage = ButterKnife.findById(itemView, R.id.iv_message);
            llMessage = ButterKnife.findById(itemView,R.id.ll_message);
            slMessage = ButterKnife.findById(itemView,R.id.sl_message);
        }
    }
}
