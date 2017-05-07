package hl.iss.whu.edu.laboratoryproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.constant.Constant;
import hl.iss.whu.edu.laboratoryproject.entity.VideoInfo;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;

/**
 * Created by fate on 2017/2/28.
 */

public class RecyclerVideosAdapter extends BaseRecyclerViewAdapter<VideoInfo,RecyclerVideosAdapter.VideoViewHolder> {
    public RecyclerVideosAdapter(ArrayList<VideoInfo> data) {
        super(data);
    }



    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_videos, parent, false);

        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        VideoInfo info = data.get(position);
        Glide.with(UiUtils.getContext())
                .load(Constant.SERVER_URL+ info.getImageUrl())
                .placeholder(R.drawable.bg)
                .dontTransform()
                .into(holder.ivImage);
        String decoded = "";
        try {
            decoded = URLDecoder.decode(info.getVideoTitle(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        holder.tvName.setText(decoded);
        holder.tvPlays.setText("播放"+info.getPlaynumber()+"次");
        holder.itemView.setTag(info);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!=null)
                    mListener.onItemClick(v, (VideoInfo) v.getTag());
            }
        });
    }


    class VideoViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        TextView tvPlays;
        ImageView ivImage;
        public VideoViewHolder(View itemView) {
            super(itemView);
            tvName = ButterKnife.findById(itemView, R.id.tv_video_name);
            tvPlays = ButterKnife.findById(itemView, R.id.tv_video_plays);
            ivImage = ButterKnife.findById(itemView, R.id.iv_video);


        }
    }
}
