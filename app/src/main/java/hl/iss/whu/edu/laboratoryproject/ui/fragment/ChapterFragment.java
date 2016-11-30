package hl.iss.whu.edu.laboratoryproject.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yalantis.pulltorefresh.library.PullToRefreshView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.adapter.ExpandableChapterAdapter;
import hl.iss.whu.edu.laboratoryproject.adapter.RecyclerMyLessonsAdapter;
import hl.iss.whu.edu.laboratoryproject.constant.Constant;
import hl.iss.whu.edu.laboratoryproject.entity.Chapter;
import hl.iss.whu.edu.laboratoryproject.entity.Result;
import hl.iss.whu.edu.laboratoryproject.listener.OnRecyclerViewItemClickListener;
import hl.iss.whu.edu.laboratoryproject.ui.activity.LessonDetailActivity;
import hl.iss.whu.edu.laboratoryproject.ui.activity.VideoActivity;
import hl.iss.whu.edu.laboratoryproject.ui.view.LoadingPage;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by fate on 2016/11/14.
 */

public class ChapterFragment extends BaseFragment<ArrayList<Chapter>> {
    private ExpandableListView elvChapter;
    private ExpandableChapterAdapter mAdapter;


    @Override
    public View onCreateSuccessPage() {
        View rootView = UiUtils.inflate(R.layout.fragment_chapter);
        elvChapter = ButterKnife.findById(rootView,R.id.elv_chapter);
        mAdapter = new ExpandableChapterAdapter(data);
        elvChapter.setAdapter(mAdapter);
        elvChapter.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Chapter.Lesson lesson = (Chapter.Lesson) mAdapter.getChild(groupPosition,childPosition);
                ((VideoActivity) getActivity()).playVideo(Constant.SERVER_URL+lesson.getURL());
                Toast.makeText(getActivity(),Constant.SERVER_URL+lesson.getURL(),Toast.LENGTH_SHORT).show();
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
