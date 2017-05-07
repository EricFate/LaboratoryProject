package hl.iss.whu.edu.laboratoryproject.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by fate on 2017/2/27.
 */

public class RecyclerClassCourseAdapter extends BaseRecyclerViewAdapter {

    public RecyclerClassCourseAdapter(ArrayList data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    class ClassCourseViewHolder extends RecyclerView.ViewHolder{

        public ClassCourseViewHolder(View itemView) {
            super(itemView);
        }
    }
}
