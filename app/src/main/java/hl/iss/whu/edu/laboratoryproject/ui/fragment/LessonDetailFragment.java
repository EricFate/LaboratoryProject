package hl.iss.whu.edu.laboratoryproject.ui.fragment;

import android.view.View;

import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.entity.SubjectDetail;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;
import io.reactivex.Observable;

/**
 * Created by fate on 2016/12/2.
 */

public class LessonDetailFragment extends BaseFragment<SubjectDetail> {
    @Override
    public View onCreateSuccessPage() {
        View rootView = UiUtils.inflate(R.layout.fragment_lesson_detail);

        return rootView;
    }

    @Override
    public Observable<SubjectDetail> sendRequest() {
        return RetrofitUtils.getService().loadSubjectDetail();
    }
}
