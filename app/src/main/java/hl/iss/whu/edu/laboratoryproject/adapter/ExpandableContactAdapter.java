package hl.iss.whu.edu.laboratoryproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.constant.Constant;
import hl.iss.whu.edu.laboratoryproject.entity.Chatter;
import hl.iss.whu.edu.laboratoryproject.entity.Group;
import hl.iss.whu.edu.laboratoryproject.glide.GlideRoundTransform;
import hl.iss.whu.edu.laboratoryproject.ui.view.AnimatedExpandableListView;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;

/**
 * Created by fate on 2016/11/26.
 */

public class ExpandableContactAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
    private ArrayList<Group> data;

    public ExpandableContactAdapter(ArrayList<Group> data) {
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
        return data.get(groupPosition).getContacts().get(childPosition);
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
         convertView = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_expandable_contact_group, null);
        TextView name = ButterKnife.findById(convertView,R.id.tv_group_name);
        name.setText(data.get(groupPosition).getName());
        }

        return convertView;
    }

//    @Override
//    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//        if (convertView ==null) {
//            convertView = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_expandable_chapter_child, null);
//            TextView title = ButterKnife.findById(convertView, R.id.tv_child_title);
//            Chapter.Lesson lesson = data.get(groupPosition).getLessons().get(childPosition);
//            title.setText(lesson.getTitle());
//        }
//        return convertView;
//    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(UiUtils.getContext()).inflate(R.layout.item_expandable_contact_child, null);
            TextView name = ButterKnife.findById(convertView, R.id.tv_contact_name);
            TextView signiture = ButterKnife.findById(convertView, R.id.tv_contact_signiture);
            ImageView image = ButterKnife.findById(convertView,R.id.iv_contact_image);
            TextView number = ButterKnife.findById(convertView,R.id.tv_contacts_number);
            Chatter chatter = data.get(groupPosition).getContacts().get(childPosition);
            name.setText(chatter.getName());
            signiture.setText(chatter.getSigniture());
            Glide.with(UiUtils.getContext())
                    .load(Constant.SERVER_URL+chatter.getImageURL())
                    .placeholder(R.drawable.ic_account_circle_blue_600_24dp)
                    .transform(new GlideRoundTransform(UiUtils.getContext()))
                    .into(image);
            ButterKnife.findById(convertView,R.id.ll_presence).setVisibility(chatter.getState()==0?View.VISIBLE:View.GONE);
            ButterKnife.findById(convertView,R.id.ll_absence).setVisibility(chatter.getState()==1?View.VISIBLE:View.GONE);

        }
        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return data.get(groupPosition).getContacts().size();
    }
}
