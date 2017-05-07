package hl.iss.whu.edu.laboratoryproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.constant.Constant;
import hl.iss.whu.edu.laboratoryproject.entity.Info;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;

/**
 * Created by fate on 2017/3/2.
 */

public class RecyclerClassMemberAdapter extends BaseRecyclerViewAdapter<Info, RecyclerClassMemberAdapter.ClassMemberViewHolder> {



    public RecyclerClassMemberAdapter(ArrayList<Info> data) {
        super(data);
    }

    @Override
    public ClassMemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_class_member, parent, false);
        return new ClassMemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ClassMemberViewHolder holder, int position) {
        Info info = data.get(position);
        holder.itemView.setTag(info);
        holder.tvName.setText(info.getNickname());
        Glide.with(UiUtils.getContext()).load(Constant.SERVER_URL+info.getImageURL())
                .placeholder(R.drawable.ic_account_circle_blue_600_24dp)
                .into(holder.ivImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!=null)
                    mListener.onItemClick(v, (Info) v.getTag());
            }
        });
    }

    class ClassMemberViewHolder extends RecyclerView.ViewHolder {
    ImageView ivImage;
    TextView tvName;
        public ClassMemberViewHolder(View itemView) {
            super(itemView);
            ivImage = ButterKnife.findById(itemView,R.id.iv_image);
            tvName = ButterKnife.findById(itemView,R.id.tv_name);
        }
    }
}
