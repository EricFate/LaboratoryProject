package hl.iss.whu.edu.laboratoryproject.adapter;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.entity.Chapter;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;

/**
 * Created by fate on 2016/11/26.
 */

public class ExpandableChapterAdapter extends BaseExpandableListAdapter {
    private ArrayList<Chapter> data;

    public ExpandableChapterAdapter(ArrayList<Chapter> data) {
        this.data = data;
    }


    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data.get(groupPosition).getLessons().size();
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
        if (convertView ==null){
         convertView = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_expandable_chapter_group, parent);
        TextView title = ButterKnife.findById(convertView,R.id.tv_group_title);
        title.setText(data.get(groupPosition).getTitle());
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView ==null) {
            convertView = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_expandable_chapter_child, parent);
            TextView title = ButterKnife.findById(convertView, R.id.tv_child_title);
            Chapter.Lesson lesson = data.get(groupPosition).getLessons().get(childPosition);
            title.setText(lesson.getTitle());
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
