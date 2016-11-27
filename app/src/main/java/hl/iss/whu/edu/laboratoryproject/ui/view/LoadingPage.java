package hl.iss.whu.edu.laboratoryproject.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import hl.iss.whu.edu.laboratoryproject.R;
import hl.iss.whu.edu.laboratoryproject.utils.UiUtils;


/**
 * Created by fate on 2016/10/20.
 */

public abstract class LoadingPage extends FrameLayout {
    public static final int STATE_UNDO = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_FAILED = 2;
    public static final int STATE_NO_DATA = 3;
    public static final int STATE_SUCCESS = 4;

    private int currentState = STATE_UNDO;

    private View mLoadingPage = null;
    private View mErrorPage = null;
    private View mNodataPage = null;
    private View mSuccessPage = null;

    public LoadingPage(Context context) {
        this(context, null);
    }

    public LoadingPage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }
    private void initView() {
        mLoadingPage = UiUtils.inflate(R.layout.fragment_loading);
        addView(mLoadingPage);
        mErrorPage = UiUtils.inflate(R.layout.fragment_error);
        addView(mErrorPage);
        mNodataPage = UiUtils.inflate(R.layout.fragment_nodata);
        addView(mNodataPage);
        showRightPage();
    }

    public void changeState(int state){
        this.currentState = state;
        showRightPage();
    }
    public void showRightPage() {
        Log.e("Loading","step into the method");
        mLoadingPage.setVisibility((currentState == STATE_UNDO || currentState == STATE_LOADING) ? VISIBLE : GONE);
        mErrorPage.setVisibility(currentState == STATE_FAILED ? VISIBLE : GONE);
        mNodataPage.setVisibility(currentState == STATE_NO_DATA ? VISIBLE : GONE);
        loadSuccesPage();
        if (mSuccessPage != null) {
            mSuccessPage.setVisibility(currentState == STATE_SUCCESS ? VISIBLE : GONE);
            Log.e("Loading","mSuccessPage:"+(mSuccessPage.getVisibility() == VISIBLE));
        }
    }

    private void loadSuccesPage() {
        if (mSuccessPage == null && currentState == STATE_SUCCESS) {
            mSuccessPage = onCreateSuccessPage();
            if (mSuccessPage != null) {
                addView(mSuccessPage);
            }
        }
    }


//    public void loadData() {
//        if (currentState != STATE_LOADING) {
//            currentState = STATE_LOADING;
//            Log.e("current", "" + currentState);
//            new Thread() {
//                @Override
//                public void run() {
//                    final ResultState state = onLoad();
//
//                    UiUtils.runInMainThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (state != null) {
//                                currentState = state.getState();
//                                Log.e("current", "" + currentState);
//                                showRightPage();
//                            }
//                        }
//                    });
//                }
//            }.start();
//        }
//    }

    public abstract View onCreateSuccessPage();

//    public abstract ResultState onLoad();

    public enum ResultState {
        STATE_COMPLETE(STATE_SUCCESS),
        STATE_EMPTY(STATE_NO_DATA),
        STATE_ERROR(STATE_FAILED);
        private int state;

        ResultState(int state) {
            this.state = state;
        }

        public int getState() {
            return state;
        }
    }
}
