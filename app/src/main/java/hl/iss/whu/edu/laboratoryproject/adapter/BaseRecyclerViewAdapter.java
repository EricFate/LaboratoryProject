package hl.iss.whu.edu.laboratoryproject.adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hl.iss.whu.edu.laboratoryproject.listener.OnRecyclerViewItemClickListener;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;

/**
 * Created by fate on 2016/10/30.
 */

public abstract class BaseRecyclerViewAdapter<E,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected List<E> data;
    protected OnRecyclerViewItemClickListener<E> mListener;
    protected int size;
    public BaseRecyclerViewAdapter(List<E> data) {
        this.data = data;
        size = data.size();
    }

    public List<E> getData() {
        return data;
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener<E> listener){
        this.mListener = listener;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<E> data) {
        this.data = data;
        UiUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    public void addAll(List<E> data) {
        this.data.addAll(data);
        UiUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }
}
