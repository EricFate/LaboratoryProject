package hl.iss.whu.edu.laboratoryproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.entity.Discover;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;

/**
 * Created by fate on 2016/12/9.
 */

public class RecyclerDiscoverAdapter extends BaseRecyclerViewAdapter<Discover,RecyclerDiscoverAdapter.MyViewHolder> {


    public RecyclerDiscoverAdapter(ArrayList<Discover> data) {
        super(data);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_dicover, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvTitle.setText(data.get(position).getTitle());
        holder.tvDetail.setText(data.get(position).getDetail());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener!=null)
                mListener.onItemClick(v, (Discover) v.getTag());
            }
        });
        holder.tvNumber.setText(new Random().nextInt(6)+"人回答");
        holder.itemView.setTag(data.get(position));
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle;
        ExpandableTextView tvDetail;
        TextView tvNumber;
        ImageView ibShare;
        View itemView;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvTitle = ButterKnife.findById(itemView, R.id.tv_title);
            tvDetail = ButterKnife.findById(itemView, R.id.etv_question_content);
            tvNumber = ButterKnife.findById(itemView, R.id.tv_discover_number);
            ibShare = ButterKnife.findById(itemView,R.id.ib_share);
            this.itemView = itemView;
        }
    }
}
