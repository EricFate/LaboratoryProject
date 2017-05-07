package hl.iss.whu.edu.laboratoryproject.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import butterknife.ButterKnife;
import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.entity.Lesson;
import hl.iss.whu.edu.laboratoryproject.utils.MyDialog;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by fate on 2016/12/2.
 */

public class LessonDetailFragment extends Fragment {

    private TextView mTvKownledge;
    private TextView mTvDescription;
    private LinearLayout llAlert;
    private NestedScrollView nsMain;
    private int flid;
    private boolean firstEnter = true;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lesson_detail, container, false);
        mTvKownledge = ButterKnife.findById(view,R.id.tv_knowledge);
        mTvDescription = ButterKnife.findById(view,R.id.tv_description);
        llAlert = ButterKnife.findById(view,R.id.ll_please_select);
        nsMain = ButterKnife.findById(view,R.id.ns_main);
        nsMain.setVisibility(View.INVISIBLE);
        llAlert.setVisibility(View.VISIBLE);
        return view;
    }

    public void refresh(final int lid){
        if (lid!=0)
            RetrofitUtils.getService().loadLessonDetail(lid).subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<Lesson>() {
                @Override
                public void accept(Lesson lesson) throws Exception {
                    refreshUI(lesson);
                    flid = lid;
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    MyDialog.showAlertDialgo(getActivity(),"刷新错误"+throwable);
                }
            });
    }

    private void refreshUI(Lesson lesson) {
            mTvDescription.setText(lesson.getDescription());
            mTvKownledge.setText(lesson.getKnowledgePoint());
        if (firstEnter) {
            nsMain.setVisibility(View.VISIBLE);
            llAlert.setVisibility(View.INVISIBLE);
        }
    }
}
