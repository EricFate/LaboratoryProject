package hl.iss.whu.edu.laboratoryproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.entity.Chapter;
import hl.iss.whu.edu.laboratoryproject.ui.view.AnimatedExpandableListView;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;

/**
 * Created by fate on 2016/11/26.
 */

public class ExpandableChapterAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
    private ArrayList<Chapter> data;

    public ExpandableChapterAdapter(ArrayList<Chapter> data) {
        this.data = data;
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }


    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).getLessons().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_expandable_chapter_group, null);
            holder = new ViewHolder();
            holder.tvTitle = ButterKnife.findById(convertView, R.id.tv_group_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvTitle.setText(data.get(groupPosition).getTitle());
        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_expandable_chapter_child, null);
            viewHolder = new ViewHolder();
            viewHolder.tvTitle = ButterKnife.findById(convertView, R.id.tv_child_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Chapter.Lesson lesson = data.get(groupPosition).getLessons().get(childPosition);
        viewHolder.tvTitle.setText(lesson.getTitle());
        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return data.get(groupPosition).getLessons().size();
    }

    static class ViewHolder {
        TextView tvTitle;
    }


}
