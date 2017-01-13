package hl.iss.whu.edu.laboratoryproject.ui.fragment;

import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.adapter.ExpandableChapterAdapter;
import hl.iss.whu.edu.laboratoryproject.constant.Constant;
import hl.iss.whu.edu.laboratoryproject.entity.Chapter;
import hl.iss.whu.edu.laboratoryproject.ui.activity.VideoActivity;
import hl.iss.whu.edu.laboratoryproject.ui.view.AnimatedExpandableListView;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;
import io.reactivex.Observable;

/**
 * Created by fate on 2016/11/14.
 */

public class ChapterFragment extends BaseFragment<ArrayList<Chapter>> {
    private AnimatedExpandableListView aelvChapter;
    private ExpandableChapterAdapter mAdapter;


    @Override
    public View onCreateSuccessPage() {
        View rootView = UiUtils.inflate(R.layout.fragment_chapter);
        aelvChapter = ButterKnife.findById(rootView,R.id.aelv_chapter);
        mAdapter = new ExpandableChapterAdapter(data);
        aelvChapter.setAdapter(mAdapter);
        aelvChapter.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Chapter.Lesson lesson = (Chapter.Lesson) mAdapter.getChild(groupPosition,childPosition);
                ((VideoActivity) getActivity()).playVideo(Constant.SERVER_URL+lesson.getURL());
//                Toast.makeText(getActivity(),Constant.SERVER_URL+lesson.getURL(),Toast.LENGTH_SHORT).show();
//                aelvChapter.collapseGroupWithAnimation(groupPosition);
                return true;
            }
        });
        return rootView;
    }

    @Override
    public Observable<ArrayList<Chapter>> sendRequest() {
        return RetrofitUtils.getService().loadChapter();
    }
}
