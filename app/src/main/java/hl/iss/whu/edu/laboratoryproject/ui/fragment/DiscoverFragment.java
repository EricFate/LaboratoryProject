package hl.iss.whu.edu.laboratoryproject.ui.fragment;

import android.view.View;

import java.util.ArrayList;

import hl.iss.whu.edu.laboratoryproject.entity.Discover;
import hl.iss.whu.edu.laboratoryproject.entity.IService;
import hl.iss.whu.edu.laboratoryproject.entity.Result;
import hl.iss.whu.edu.laboratoryproject.ui.view.LoadingPage;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by fate on 2016/10/29.
 */

public class DiscoverFragment extends BaseFragment<ArrayList<Discover>> {
    LoadingPage.ResultState state = LoadingPage.ResultState.STATE_ERROR;
    @Override
    public View onCreateSuccessPage() {
        return null;
    }

    @Override
    public Observable<ArrayList<Discover>> sendRequest() {
        return RetrofitUtils.getService().loadDiscover();
    }


}
