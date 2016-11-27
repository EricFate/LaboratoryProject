package hl.iss.whu.edu.laboratoryproject.utils;

import java.util.HashMap;
import java.util.Map;

import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.ui.fragment.BaseFragment;
import hl.iss.whu.edu.laboratoryproject.ui.fragment.DiscoverFragment;
import hl.iss.whu.edu.laboratoryproject.ui.fragment.LessonSelectionFragment;
import hl.iss.whu.edu.laboratoryproject.ui.fragment.MyLessonsFragment;

/**
 * Created by fate on 2016/10/29.
 */

public class FragmentFactory {
    private static Map<Integer, BaseFragment> map = new HashMap<>();

    public static BaseFragment getFragmentById(int id) {
        BaseFragment fragment = map.get(id);
        if (fragment == null) {
            switch (id) {
                case R.id.rb_lessons:
                    fragment = new MyLessonsFragment();
                    break;
                case R.id.rb_select:
                    fragment = new LessonSelectionFragment();
                    break;
                case R.id.rb_discover:
                    fragment = new DiscoverFragment();
                    break;
            }
            map.put(id,fragment);
        }
        return fragment;
    }
}
