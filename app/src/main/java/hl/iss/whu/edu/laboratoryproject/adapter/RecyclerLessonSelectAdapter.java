package hl.iss.whu.edu.laboratoryproject.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.entity.Catagory;
import hl.iss.whu.edu.laboratoryproject.entity.Subject;
import hl.iss.whu.edu.laboratoryproject.listener.OnRecyclerViewItemClickListener;
import hl.iss.whu.edu.laboratoryproject.manager.FullyLinearLayoutManager;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;

/**
 * Created by fate on 2016/11/13.
 */

public class RecyclerLessonSelectAdapter extends BaseRecyclerViewAdapter<Catagory,RecyclerLessonSelectAdapter.ViewHolder> {

    private OnRecyclerViewItemClickListener<Subject> onSubjectClickListener;
    public RecyclerLessonSelectAdapter(ArrayList<Catagory> data) {
        super(data);
    }

//    @Override
//    public int getItemViewType(int position) {
//        return position%2;
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_recycler_lesson_selection,parent,false);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mListener.onItemClick(v, (Subject) v.getTag());
//            }
//        });
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Catagory catagory = data.get(position);

        holder.tvTitle.setText(catagory.getTitle());
        RecyclerView recyclerSubject = holder.recyclerSubject;
        RecyclerSubjectAdapter adapter = new RecyclerSubjectAdapter(catagory.getSubjects());
        recyclerSubject.setLayoutManager(new FullyLinearLayoutManager(UiUtils.getContext(), LinearLayoutManager.HORIZONTAL,false));
        recyclerSubject.setAdapter(adapter);
        adapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener<Subject>() {
            @Override
            public void onItemClick(View v, Subject data) {
                if (onSubjectClickListener !=null)
                    onSubjectClickListener.onItemClick(v,data);
            }
        });
//        holder.itemView.setTag(data.get(position));
    }
    public void setOnSubjectClickListener(OnRecyclerViewItemClickListener<Subject> onSubjectClickListener){
        this.onSubjectClickListener = onSubjectClickListener;
    }

     static class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerSubject;
        TextView tvTitle;

         View itemView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvTitle = ButterKnife.findById(itemView,R.id.tv_title);
            recyclerSubject = ButterKnife.findById(itemView,R.id.recycler_subject);
        }
    }
}
