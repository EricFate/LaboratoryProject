package hl.iss.whu.edu.laboratoryproject.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.ParameterizedType;

import hl.iss.whu.edu.laboratoryproject.constant.Constant;
import hl.iss.whu.edu.laboratoryproject.ui.view.LoadingPage;
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
    protected T data;
    private String cachePath;
    private File cacheFile;
    private Gson gson = new Gson();
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
            @Override
            protected void loadData() {
                BaseFragment.this.loadDataFromServer();
            }
        };
        loadData();
    }

    public void loadData() {
        cachePath = getContext().getCacheDir().getAbsolutePath();
        cacheFile = new File(cachePath+File.separator+getClass().getSimpleName());
        if (!cacheFile.exists())
            loadDataFromServer();
        else {
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new FileReader(cacheFile));
                String line = bufferedReader.readLine();
                long lastTime = Long.parseLong(line);
                if (System.currentTimeMillis()-lastTime< Constant.CACHE_DURATION){
                    StringBuilder builder = new StringBuilder();
                    while((line = bufferedReader.readLine())!=null){
                        builder.append(line);
                    }
                    data = gson.fromJson(builder.toString(),((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
                    mLoadingPage.changeState(LoadingPage.STATE_SUCCESS);
                }else {
                    cacheFile.delete();
                    loadDataFromServer();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void loadDataFromServer() {

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
                e.printStackTrace();
                mLoadingPage.changeState(LoadingPage.STATE_FAILED);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void handleResult(T value) {
        data = value;
        PrintWriter writer = null;
        try {
            writer =new PrintWriter( cacheFile) ;
            writer.println(System.currentTimeMillis());
            writer.print(gson.toJson(value));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            writer.close();
        }

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