package hl.iss.whu.edu.laboratoryproject.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by fate on 2016/11/14.
 */

public class VideoFragmentPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments;
    private ArrayList<String> titles;
    public VideoFragmentPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments,ArrayList<String> titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
