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
import hl.iss.whu.edu.laboratoryproject.entity.Chapter;
import hl.iss.whu.edu.laboratoryproject.entity.Result;
import hl.iss.whu.edu.laboratoryproject.listener.OnRecyclerViewItemClickListener;
import hl.iss.whu.edu.laboratoryproject.ui.activity.LessonDetailActivity;
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
    private LoadingPage mLoadingPage ;



    @Override
    public View onCreateSuccessPage() {
        return null;
    }

    @Override
    public Observable<ArrayList<Chapter>> sendRequest() {
        return RetrofitUtils.getService().loadChapter();
    }
}
