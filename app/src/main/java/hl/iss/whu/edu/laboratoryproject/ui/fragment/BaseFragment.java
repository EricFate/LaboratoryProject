package hl.iss.whu.edu.laboratoryproject.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hl.iss.whu.edu.laboratoryproject.ui.view.LoadingPage;
import hl.iss.whu.edu.laboratoryproject.utils.RetrofitUtils;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by fate on 2016/10/29.
 */

public abstract class BaseFragment<T> extends Fragment {
    private LoadingPage mLoadingPage;
    private  T data;
    public BaseFragment(){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoadingPage = new LoadingPage(UiUtils.getContext()) {

            @Override
            public View onCreateSuccessPage() {
                return BaseFragment.this.onCreateSuccessPage();
            }
        };
        sendRequest().subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<T>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(T value) {
                handleResult(value);
            }

            @Override
            public void onError(Throwable e) {
                mLoadingPage.changeState(LoadingPage.STATE_FAILED);
            }

            @Override
            public void onComplete() {

            }
        });

    }

    private void handleResult(T value) {
        data = value;
        if (data == null)
            mLoadingPage.changeState(LoadingPage.STATE_NO_DATA);
        if (data!=null)
            mLoadingPage.changeState(LoadingPage.STATE_SUCCESS);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return mLoadingPage;
    }
    //当网络请求返回STATE_SUCCESS时才会回调,运行在主线程
    public abstract  View onCreateSuccessPage();
    //运行在子线程
    public abstract Observable<T> sendRequest();


}